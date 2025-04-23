package com.daytour.processing;

import java.time.LocalDate;

/**
 * Interface for Query search class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @version 1.0
 * @since 2023-03-25
 **/
public interface QueryInterface
{
    /**
     * Adds tour to database.
     *
     * @param tour Tour information.
     */
    void addTour(DayTourDetails tour);

    /**
     * Adds a booking to the database, and updates availability in database.
     *
     * @param booking The booking to be added.
     */
    void addBooking(Booking booking);

    /**
     * Adds a review to the tour database.
     *
     * @param review The review string.
     * @param tour The tour to add review to.
     */
    void addReview(DayTourDetails tour, Review review);

    /**
     * Searches database for tours by region, town or name on date.
     *
     * @param tour What to search for. Leave blank for all locations on date.
     * @param dateStart Date of tour.
     * @param dateEnd End date of date range.
     * @param order Set 'p' for popular or 't' for time, or 'd' for default.
     * @param filter Only show tours with filter.
     * @return Array of DayTour objects. Returns null if nothing was found.
     */
    DayTour[] searchTours(String tour, LocalDate dateStart, LocalDate dateEnd, char order, String filter);

    /**
     * Searches database for tours by region, town or name on date.
     *
     * @param tour What to search for. Leave blank for all locations on date.
     * @param dateStart Date of tour.
     * @param dateEnd End date of date range.
     * @param order Set 'p' for popular or 't' for time, or 'd' for default.
     * @param filter Only show tours with filter.
     * @return Array of DayTourDetails objects. Returns null if nothing was found.
     */
    DayTourDetails[] searchTourDetails(String tour, LocalDate dateStart, LocalDate dateEnd, char order, String filter);

    /**
     * Gets all bookings for a tour on a specific date.
     *
     * @param tourID The tour ID to which the bookings belong.
     * @param date The date the bookings are on.
     * @return Returns a list of bookings for a tour on date, if any.
     */
    Booking[] getTourBookings(long tourID, LocalDate date);

    /**
     * Gets a booking with a specific booking ID.
     *
     * @param bookingID The ID of the booking.
     * @return Returns the booking to which the ID belongs, if any.
     */
    Booking getBookingByID(long bookingID);
}
