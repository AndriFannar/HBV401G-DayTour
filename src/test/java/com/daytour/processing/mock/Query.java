package com.daytour.processing.mock;

import com.daytour.processing.Booking;
import com.daytour.processing.DayTour;
import com.daytour.processing.DayTourDetails;

import java.time.LocalDate;

/**
 * Communicates with repository class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @version 1.1
 * @since 2023-03-23
 **/
public class Query
{
    // Creates an instance of the repository controller class.
    final DatabaseConnectionMock dc = new DatabaseConnectionMock();

    /**
     * Adds a tour to the repository.
     *
     * @param tour Tour to be added.
     */
    public void addTour(DayTourDetails tour)
    {
        dc.addTour(tour);
    }

    /**
     * Adds a booking to the repository.
     *
     * @param booking Booking to be added.
     */
    public void addBooking(Booking booking)
    {
        dc.addBooking(booking);
    }

    /**
     * Returns a booking that matches the given ID.
     *
     * @param bookingID The ID of the requested booking.
     * @return Returns the Booking object that has a matching ID.
     */
    public Booking getBookingByID(long bookingID) { return dc.getBooking(bookingID); }

    /**
     * Searches for available day tours.
     *
     * @param searchString Search string. Can be Region, Town, Name or ID.
     * @param date The date to search tours on.
     * @param filter Filters to apply to search, if any.
     * @return Returns an array of DayTour objects that matched the search parameters.
     */
    public DayTour[] searchTours(String searchString, LocalDate date, String filter)
    {
        return dc.searchTours(searchString, date, filter).toArray(new DayTour[0]);
    }

    /**
     * Searches for available day tours.
     *
     * @param searchString Search string. Can be Region, Town, Name or ID.
     * @param date The date to search tours on.
     * @param filter Filters to apply to search, if any.
     * @return Returns an array of DayTourDetails objects that matched the search parameters.
     */
    public DayTourDetails[] searchTourDetails(String searchString, LocalDate date, String filter)
    {
        return dc.searchTours(searchString, date, filter).toArray(new DayTourDetails[0]);
    }
}
