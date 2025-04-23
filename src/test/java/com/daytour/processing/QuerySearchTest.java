package com.daytour.processing;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Query class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @version 1.0
 * @since 2023-03-26
 **/
public class QuerySearchTest {
    private static final LocalDate DATE = LocalDate.parse("2023-04-02");
    private static final int TOUR_ID = 91140;
    // Variables.
    private Query q;

    /**
     * Sets up the test.
     */
    @BeforeEach
    public void setUp() {
        q = new Query();
    }

    /**
     * Tear down the test.
     */
    @AfterEach
    public void tearDown() {
        q = null;
    }

    /**
     * Test fetching available tour.
     */
    @Test
    public void searchAvailableTourTest() {
        DayTour[] tours = q.searchTours(String.valueOf(TOUR_ID), DATE, DATE, 'd', null);

        assertEquals(tours[0].getID(), TOUR_ID);
    }

    /**
     * Test fetching nonexisting tour.
     */
    @Test
    public void searchUnavailableTourTest() {
        DayTour[] tours = q.searchTours("0", DATE, DATE, 'd', null);

        assertEquals(tours.length, 0);
    }

    /**
     * Test fetching tours where search string is null.
     */
    @Test
    public void searchToursNullTest() {
        DayTourDetails[] tours = q.searchTourDetails(null, DATE, DATE, 'd', null);

        assertEquals(tours[0].getDates()[0], DATE);
    }

    /**
     * Test searching tours on date range.
     */
    @Test
    public void searchToursRangeTest() {
        LocalDate[] dates = { DATE, DATE.plusDays(1), DATE.plusDays(2), DATE.plusDays(3) };
        DayTourDetails[] tours = q.searchTourDetails(null, dates[0], dates[3], 'd', null);

        assertAll(
                () -> assertEquals(tours[0].getDates()[0], dates[0]),
                () -> assertEquals(tours[0].getDates()[1], dates[1]),
                () -> assertEquals(tours[0].getDates()[2], dates[2]),
                () -> assertEquals(tours[0].getDates()[3], dates[3]));
    }

    @Test
    public void addBookingTest() {
        DayTourDetails tour = q.searchTourDetails(String.valueOf(TOUR_ID), DATE, DATE, 'd', null)[0];
        LocalTime time = tour.getTimes(DATE)[0];
        Booking b = new Booking(tour.getID(), "Andri", "Afk6@hi.is", DATE, time,
                tour.getAvailable(DATE, time), false, "Card", 5990);
        q.addBooking(b);

        Booking[] bookings = q.getTourBookings(TOUR_ID, DATE);
        DayTourDetails[] dtt = q.searchTourDetails(String.valueOf(TOUR_ID), DATE, DATE, 'd', null);

        assertAll(
                () -> assertEquals(bookings[0].getName(), "Andri"),
                () -> assertNotEquals(dtt[0].getTimes(DATE)[0], time));

    }
}
