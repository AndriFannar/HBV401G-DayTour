package com.daytour.processing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Communicates with repository class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is.
 * @since 2023-01-26
 * @version 2.4
 **/
public class Query implements QueryInterface {
   private final DatabaseConnection dc;

   /**
    * Create a new Query class instance.
    * Default database location will be used.
    */
   public Query() {
      dc = new DatabaseConnection();
   }

   /**
    * Create a new Query class instance.
    * 
    * @param databasePath Path to database.
    */
   public Query(String databasePath) {
      dc = new DatabaseConnection(databasePath);
   }

   /**
    * Adds tour to database.
    *
    * @param tour DayTour object to add to the database.
    */
   public void addTour(DayTourDetails tour) {
      String query = String.format(
            "INSERT INTO DayTours(tourID, region, town, name, rating, popularity, price, pictures, length, company, filters, description, openingHours) VALUES(%d, '%s', '%s', '%s', %f, %d, %d, '%s', %d, '%s', '%s', '%s', '%s')",
            tour.getID(), tour.getRegion(), tour.getTown(), tour.getName(), tour.getRating(), tour.getPopularity(),
            tour.getPrice(), tour.getPicturesStr(), tour.getLength(), tour.getCompany(), tour.getFiltersStr(),
            tour.getDescription(), tour.getOpeningHours());

      // Inserts the details into DayTour table.
      dc.connectDatabase(query, true, false, false);

      // Creates time and date in DayTourDates table.
      String queryTime;

      for (LocalDate date : tour.getDates()) {
         for (LocalTime time : tour.getTimes(date)) {
            queryTime = String.format("INSERT INTO DayTourDates VALUES(%d, '%s', '%s', %d)", tour.getID(), date,
                  time.getHour(), tour.getAvailable(date, time));
            dc.connectDatabase(queryTime, true, false, false);
         }
      }
   }

   /**
    * Adds a booking to the database, and updates availability and popularity.
    *
    * @param booking The booking to be added.
    */
   public void addBooking(Booking booking) {
      // Create base string.
      String query = String.format("INSERT INTO Bookings VALUES('%s', '%s', '%s', %d, '%s', '%s', '%s', %d, %d, %d)",
            booking.getName(), booking.getDate(), booking.getTime().getHour(), booking.getNoPersons(),
            booking.getEmail(),
            booking.getHotelPickup(), booking.getPayment(), booking.getTotalPrice(), booking.getTourID(),
            booking.getBookingID());

      // Add booking to database.
      dc.connectDatabase(query, true, false, false);

      // Set new availability.
      query = String.format(
            "UPDATE DayTourDates SET availability=availability -%d WHERE tourID=%d AND date='%s' AND time='%s'",
            booking.getNoPersons(), booking.getTourID(), booking.getDate(), booking.getTime().getHour());

      dc.connectDatabase(query, true, false, false);

      // Update popularity.
      query = String.format("UPDATE DayTours SET popularity=popularity +1 WHERE tourID=%d", booking.getTourID());

      dc.connectDatabase(query, true, false, false);
   }

   /**
    * Adds a review to the tour database.
    *
    * @param tour   The tour to add review to.
    * @param review The review string.
    */
   public void addReview(DayTourDetails tour, Review review) {
      // Inserts review into the database.
      String query = String.format("INSERT INTO Reviews VALUES(%d, '%s', '%s', '%s', '%s')",
            tour.getID(), review.getName(), review.getTitle(), review.getReview(), review.getLiked());

      dc.connectDatabase(query, true, false, false);

      tour.addReview(review);

      // Updates the rating.
      query = String.format("UPDATE DayTours SET rating=%f WHERE tourID=%d", tour.getRating(), tour.getID());

      dc.connectDatabase(query, true, false, false);
   }

   /**
    * Searches database for tours by region, town or name on date.
    *
    * @param tour      What to search for. Leave blank for all locations on date.
    * @param dateStart Date of tour.
    * @param dateEnd   End date of date range.
    * @param details   Detailed version.
    * @param order     Set 'p' for popular, 'v' for value, 't' for time, or 'd' for
    *                  default.
    * @param filter    Only show tours with filter.
    * @return Array of DayTour objects (DayTourDetails if details is true). Returns
    *         null if nothing was found.
    */
   private DayTour[] searchTours(String tour, LocalDate dateStart, LocalDate dateEnd, boolean details, char order,
         String filter) {
      // Create base query.
      StringBuilder query = new StringBuilder(
            "SELECT DISTINCT dt.tourID, region, town, name, rating, popularity, price, pictures ");

      if (details)
         query.append(", length, company, filters, description, openingHours, date ");

      // Create search string based on tour input.
      if (tour == null || tour.isEmpty()) {
         query.append(String.format(
               "FROM DayTours AS dt JOIN DayTourDates AS dtd ON dt.tourID=dtd.tourID WHERE date between '%s' AND '%s'",
               dateStart.toString(), dateEnd.toString()));
      } else
         query.append(String.format(
               "FROM DayTours AS dt JOIN DayTourDates AS dtd ON dt.tourID=dtd.tourID WHERE date='%s' AND ( region LIKE '%%%s%%' OR town LIKE '%%%s%%' OR name LIKE '%%%s%%' OR dt.tourID='%s')",
               dateStart.toString(), tour, tour, tour, tour));

      // Checks if filtering should be applied.
      if (filter != null)
         query.append(String.format(" AND filters LIKE '%%%s%%'", filter));

      // Checks if order is specified.
      switch (order) {
         case 'p' -> query.append(" ORDER BY popularity DESC");
         case 't' -> query.append(" ORDER BY time ASC");
         case 'v' -> query.append(" ORDER BY price ASC");
      }

      List<Object> dt = dc.connectDatabase(query.toString(), false, details, false);

      if (details)
         return dt.toArray(new DayTourDetails[0]);
      return dt.toArray(new DayTour[0]);
   }

   /**
    * Searches database for tours by region, town or name on date.
    *
    * @param tour      What to search for. Leave blank for all locations on date.
    * @param dateStart Date of tour.
    * @param dateEnd   End date of date range.
    * @param order     Set 'p' for popular or 't' for time, or 'd' for default.
    * @param filter    Only show tours with filter.
    * @return Array of DayTour objects. Returns null if nothing was found.
    */
   public DayTourDetails[] searchTourDetails(String tour, LocalDate dateStart, LocalDate dateEnd, char order,
         String filter) {
      return (DayTourDetails[]) searchTours(tour, dateStart, dateEnd, true, order, filter);
   }

   /**
    * Searches database for tours by region, town or name on date.
    *
    * @param tour      What to search for. Leave blank for all locations on date.
    * @param dateStart Date of tour.
    * @param dateEnd   End date of date range.
    * @param order     Set 'p' for popular or 't' for time, or 'd' for default.
    * @param filter    Only show tours with filter.
    * @return Array of DayTourDetails objects. Returns null if nothing was found.
    */
   public DayTour[] searchTours(String tour, LocalDate dateStart, LocalDate dateEnd, char order, String filter) {
      return searchTours(tour, dateStart, dateEnd, false, order, filter);
   }

   /**
    * Gets all bookings for a tour on a specific date.
    *
    * @param tourID The tour to which the bookings belong.
    * @param date   The date the bookings are on.
    * @return Returns a list of bookings for a tour on date, if any.
    */
   public Booking[] getTourBookings(long tourID, LocalDate date) {
      String query = String.format("SELECT * FROM Bookings WHERE tourID=%d AND date='%s'", tourID, date);

      List<Object> bookings = dc.connectDatabase(query, false, false, true);
      
      return bookings.toArray(new Booking[0]);
   }

   /**
    * Gets a booking with a specific booking ID.
    *
    * @param bookingID The ID of the booking.
    * @return Returns the booking to which the ID belongs, if any.
    */
   public Booking getBookingByID(long bookingID) {
      String query = String.format("SELECT * FROM Bookings WHERE bookingID=%d", bookingID);

      List<Object> bookings = dc.connectDatabase(query, false, false, true);

      return bookings.toArray(new Booking[0])[0];
   }

}
