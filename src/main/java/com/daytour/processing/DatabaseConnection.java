package com.daytour.processing;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * Communicates with SQLITE database.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is.
 * @since 2023-01-26
 * @version 3.0
 **/
public class DatabaseConnection {
   private String PATH_TO_DATABASE = "src/main/java/resources/com/daytour/processing/daytours.db";
   private final String JDBC_DRIVER = "org.sqlite.JDBC";
   private final String DB_URL = "jdbc:sqlite:";
   private final String SELECT_TIME = "SELECT date, time, availability FROM DayTourDates WHERE tourID=? AND date=?";
   private final String DELETE_TIME = "DELETE FROM DayTourDates WHERE tourID=? AND date=? AND time=?";
   private final String SELECT_REVIEWS = "SELECT name, title, review, like FROM Reviews WHERE tourID=?";

   /**
    * Create a new DatabaseConnection object with default database path.
    */
   public DatabaseConnection() {
   }

   /**
    * Create a new DatabaseConnection object.
    *
    * @param databasePath Path to database.
    */
   public DatabaseConnection(String databasePath) {
      PATH_TO_DATABASE = databasePath;
   }

   /**
    * Connects to database and executes query or update.
    *
    * @param statement The string to execute on the database.
    * @param insert    Insert into table.
    * @param details   Include complete details.
    * @param booking   Result should be Booking objects.
    * @return List of DayTour objects found.
    */
   public List<Object> connectDatabase(String statement, boolean insert, boolean details, boolean booking) {
      // Connect to JDBC.
      try {
         Class.forName(JDBC_DRIVER);
      } catch (ClassNotFoundException e) {
         System.err.println("Could not load JDBC driver: " + e.getMessage());
      }

      // Create return list.
      List<Object> itemArray = new LinkedList<>();

      // Try to connect to database.
      try (Connection connection = DriverManager.getConnection(DB_URL + PATH_TO_DATABASE)) {
         // Create a statement to be executed.
         PreparedStatement s = connection.prepareStatement(statement);

         // If insert is true, then execute update.
         if (insert) {
            s.executeUpdate();
         } else {
            try (ResultSet rs = s.executeQuery()) {
               // If booking is true, create Booking objects.
               if (booking) {
                  itemArray.addAll(getBookingList(rs));
               }
               // Else if details is true, create DayTourDetails objects.
               else if (details) {
                  itemArray.addAll(getDayTourDetailsList(connection, rs));
               }
               // Else create DayTour objects.
               else {
                  itemArray.addAll(getDayTourList(rs));
               }
            }
         }
      } catch (Exception e) {
         System.err.println(e.getMessage());
      }
      return itemArray;
   }

   /**
    * Gets a list of Booking objects from the database.
    *
    * @param rs The ResultSet of the executed query.
    * @return Returns a list of Booking objects.
    */
   private List<Booking> getBookingList(ResultSet rs) throws SQLException {
      // Create return list.
      List<Booking> bookingList = new LinkedList<>();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H");

      while (rs.next()) {
         // Create Booking objects while ResultSet is not empty.
         bookingList.add(new Booking(rs.getLong(9), rs.getLong(10), rs.getString(1),
               rs.getString(5), LocalDate.parse(rs.getString(2)), LocalTime.parse(rs.getString(3), formatter),
               rs.getInt(4), rs.getBoolean(6), rs.getString(7), rs.getInt(8)));
      }

      return bookingList;
   }

   /**
    * Gets a list of DayTour objects from the database.
    *
    * @param rs The ResultSet of the executed query.
    * @return Returns a list of DayTour objects.
    */
   private List<DayTour> getDayTourList(ResultSet rs) throws SQLException {
      // Create return list.
      List<DayTour> dayTourList = new LinkedList<>();

      while (rs.next()) {
         // Create DayTour objects while ResultSet is not empty.
         dayTourList.add(new DayTour(rs.getLong(1), rs.getString(2), rs.getString(3),
               rs.getString(4), rs.getDouble(5), rs.getInt(6), rs.getInt(7),
               rs.getString(8)));
      }

      return dayTourList;
   }

   /**
    * Gets a list of DayTourDetails objects from the database.
    *
    * @param connection The connection to the database.
    * @param rs         The ResultSet of the executed query.
    * @return Returns a list of DayTourDetails objects.
    */
   private List<DayTourDetails> getDayTourDetailsList(Connection connection, ResultSet rs) throws SQLException {
      DayTourDetails tour;
      List<DayTourDetails> dayTourDetailsList = new LinkedList<>();

      while (rs.next()) {
         String date = rs.getString(14);
         tour = createDayTourDetails(rs);

         // Checks if the tour has already been added to the list.
         if (!dayTourDetailsList.contains(tour)) {
            dayTourDetailsList.add(tour);
         }

         // Adds the date the tour was found on to the object.
         tour = dayTourDetailsList.get(dayTourDetailsList.indexOf(tour));
         tour.addDate(LocalDate.parse(date));
      }

      // Create times and availability for all dates that were added to the tour as
      // well as Reviews.
      for (DayTourDetails dtd : dayTourDetailsList) {
         addTimes(connection, dtd);
         addReviews(connection, dtd);
      }

      return dayTourDetailsList;
   }

   /**
    * Creates a DayTourDetails object.
    *
    * @param rs The ResultSet of the executed query.
    * @return Returns a DayTourDetails object.
    */
   private DayTourDetails createDayTourDetails(ResultSet rs) throws SQLException {
      return new DayTourDetails(rs.getLong(1), rs.getString(2), rs.getString(3),
            rs.getString(4), rs.getDouble(5), rs.getInt(6), rs.getInt(7),
            rs.getString(8), rs.getInt(9), rs.getString(10), rs.getString(11),
            rs.getString(12), rs.getString(13));
   }

   /**
    * Adds times to a DayTourDetails object.
    *
    * @param connection The connection to the database.
    * @param tour       The DayTourDetails object to add the times to.
    */
   private void addTimes(Connection connection, DayTourDetails tour) throws SQLException {
      PreparedStatement ds = connection.prepareStatement(SELECT_TIME);
      ds.setLong(1, tour.getID());

      for (LocalDate date : tour.getDates()) {
         ds.setString(2, date.toString());

         // Get times and availability for a date.
         try (ResultSet trs = ds.executeQuery()) {

            while (trs.next()) {
               int available = trs.getInt(3);
               int time = trs.getInt(2);

               // If the availability is less than or equal to 0, delete it from the database.
               if (available <= 0) {
                  PreparedStatement ts = connection.prepareStatement(DELETE_TIME);
                  ts.setLong(1, tour.getID());
                  ts.setString(2, date.toString());
                  ts.setInt(3, time);
                  ts.executeUpdate();
               } else {
                  // If it is not less than or equal to 0, add it to the tour.
                  tour.addTime(LocalDate.parse(trs.getString(1)), LocalTime.of(time, 0), available);
               }
            }
         }
      }
   }

   /**
    * Adds Review objects to DayTourDetails objects.
    *
    * @param connection The connection to the database.
    * @param tour       The DayTourDetails object to add the Review objects to.
    */
   private void addReviews(Connection connection, DayTourDetails tour) throws SQLException {
      PreparedStatement rws = connection.prepareStatement(SELECT_REVIEWS);
      rws.setLong(1, tour.getID());

      // Add all reviews to the tour.
      try (ResultSet rrs = rws.executeQuery()) {
         while (rrs.next()) {
            tour.addReview(new Review(rrs.getString(1), rrs.getString(2), rrs.getString(3),
                  Boolean.parseBoolean(rrs.getString(4))));
         }
      }
   }
}