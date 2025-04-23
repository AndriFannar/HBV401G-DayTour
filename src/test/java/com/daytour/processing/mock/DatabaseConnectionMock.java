package com.daytour.processing.mock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.daytour.processing.Booking;
import com.daytour.processing.DayTour;
import com.daytour.processing.DayTourDetails;

/**
 * A Mock database class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @version 1.3
 * @since 2023-03-23
 **/
public class DatabaseConnectionMock
{
    //Data storage.
    private final List<DayTourDetails> tours;
    private final List<Booking> bookings;

    /**
     * Creates a new instance of the mock database.
     */
    public DatabaseConnectionMock()
    {
        // Variables for times of tours.
        String[] dates = {"2023-03-19", "2023-03-20", "2023-03-21", "2023-03-22", "2023-03-23"};
        int[] times = {10, 11, 12, 13, 14};
        int availability = 100;

        // Initialize the data storage.
        tours = new ArrayList<>();
        bookings = new ArrayList<>();

        // Create two tours to add to storage.
        DayTourDetails dtt1 = new DayTourDetails(1234, "Suðurland", "Jökulsárlón",
                "South coast and Jökulsárlón glacier lagoon day tour from Reykjavík", 75, 84,
                24000, "Jokulsarlon.png", 14, "Gray Line Iceland", "All-day|Family Friendly",
                "Ice-strewn Jokulsarlon is one of Iceland's top...", "00:00-00:00");

        DayTourDetails dtt2 = new DayTourDetails(5678, "Norðurland", "Húsavík", "Geosea",
                80, 100, 5990, "Geosea.png", 3, "GeoSea",
                "Spa|Swimming|Relaxation|Family Friendly", "The GeoSea sea baths enable you to enjoy...",
                "12:00-22:00");

        // Add dates to the tours.
        for (String d : dates)
        {
            for(int t : times)
            {
                dtt1.addTime(LocalDate.parse(d), LocalTime.of(t, 0), availability);
                dtt2.addTime(LocalDate.parse(d), LocalTime.of(t, 0), availability);
            }
        }

        // Add tours to storage.
        tours.add(dtt1);
        tours.add(dtt2);
    }

    /**
     * Adds a tour to the database.
     *
     * @param tour Tour to be added.
     */
    public void addTour(DayTourDetails tour)
    {
        if (tour == null) return;
        for (DayTourDetails dtt : tours)
        {
            if (dtt.getID() == tour.getID()) return;
        }
        tours.add(tour);
    }

    /**
     * Adds a booking to the database.
     *
     * @param booking Booking to be added.
     */
    public void addBooking(Booking booking)
    {
        if (booking == null) return;
        for (Booking b : bookings)
        {
            if (b.getBookingID() == booking.getBookingID()) return;
        }
        bookings.add(booking);
    }

    /**
     * Gets a booking that has a matching ID.
     *
     * @param bookingID The ID of the requested booking.
     * @return Returns a booking with matching ID.
     */
    public Booking getBooking(long bookingID)
    {
        for (Booking b : bookings)
        {
            if (b.getBookingID() == bookingID) return b;
        }
        return null;
    }

    /**
     * Searches available tours.
     *
     * @param searchString Search string. Can be Region, Town, Name or ID.
     * @param date The date to search tours on.
     * @param filter Filters to apply to search, if any.
     * @return Returns a List of DayTour (or DayTourDetails) objects that matched the search parameters.
     */
    public List<DayTour> searchTours(String searchString, LocalDate date, String filter)
    {
        // Create a list for the results.
        List<DayTourDetails> nameResults = new ArrayList<>();

        // Makes sure the string is not null before proceeding.
        if(searchString != null)
        {
            for (DayTourDetails dtt : tours)
            {
                // Checks if the search string matches the tour.
                if ((searchString.equals(dtt.getID() + "")) || (dtt.getName().contains(searchString)) || (dtt.getRegion().contains(searchString)) || (dtt.getTown().contains(searchString)))
                {
                    if (filter != null)
                    {
                        // If a filter was specified, check it before adding the tour to the result.
                        if (dtt.getFiltersStr().contains(filter)) nameResults.add(dtt);
                    } else
                    {
                        nameResults.add(dtt);
                    }
                }
            }
        }
        else
        {
            nameResults.addAll(tours);
        }

        List<DayTour> finalResult = new ArrayList<>();

        for (DayTourDetails dt : nameResults)
        {
            for (LocalDate d : dt.getDates())
            {
                // Checks if the current tour has availability on inputted date.
                if (d.equals(date))
                {
                    finalResult.add(dt);
                }
            }

        }

        return finalResult;
    }
}
