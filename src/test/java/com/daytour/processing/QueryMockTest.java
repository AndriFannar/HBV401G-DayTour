package com.daytour.processing;

import com.daytour.processing.mock.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Tests the Query class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @version 1.4
 * @since 2023-03-23
 **/
public class QueryMockTest {
    // Variables.
    private Query qm;

    /**
     * Sets up the test.
     */
    @BeforeEach
    public void setUp() {
        qm = new Query();
    }

    /**
     * Tear down the test.
     */
    @AfterEach
    public void tearDown() {
        qm = null;
    }

    /**
     * Tests adding a new tour to the repository.
     */
    @Test
    public void testNewTour() {
        // Create a new DayTourDetails object.
        DayTourDetails dtt = new DayTourDetails(1111, "Norðurland", "Mývatn", "Jarðböðin",
                95, 87, 6500, "Myvatn.png", 3, "Myvatn Nature Baths",
                "Hot Spring|Swimming|Relaxation|Family Friendly", "Myvatn er...", "10:00-16:00");

        // Add some arbitrary time to the tour.
        dtt.addTime(LocalDate.parse("2023-03-20"), LocalTime.of(10, 0), 100);

        // Add to the repository.
        qm.addTour(dtt);

        // Search for the tour and check if it is found.
        DayTourDetails[] results = qm.searchTourDetails("1111", LocalDate.parse("2023-03-20"), null);
        assert results[0].getTown().equals("Mývatn");
    }

    /**
     * Tests adding a tour that already exists.
     */
    @Test
    public void testAddExistingTour() {
        // Create a new DayTourDetails object.
        DayTourDetails dtt = new DayTourDetails(1234, "Suðurland", "Jökulsárlón",
                "South coast and Jökulsárlón glacier lagoon day tour from Reykjavík", 75, 84,
                24000, "Jokulsarlon.png", 14, "Gray Line Iceland", "All-day|Family Friendly",
                "Ice-strewn Jokulsarlon is one of Iceland's top...", "00:00-00:00");

        // Add some arbitrary time to the tour.
        dtt.addTime(LocalDate.parse("2023-03-20"), LocalTime.of(10, 0), 100);

        // Add the tour to the repository.
        qm.addTour(dtt);

        // Search for the tour and make sure it only returns one.
        DayTourDetails[] results = qm.searchTourDetails("Suðurland", LocalDate.parse("2023-03-20"), null);
        assert results.length <= 1;
    }

    /**
     * Test adding and getting a booking.
     */
    @Test
    public void testAddBooking() {
        DayTourDetails[] results = qm.searchTourDetails("Suðurland", LocalDate.parse("2023-03-20"), null);

        Booking booking = new Booking(results[0].getID(), "Andri", "Afk6@Hi.is", LocalDate.parse("2023-03-20"),
                LocalTime.of(11, 0), 1, false, "Card", 10000);
        long bookingID = booking.getBookingID();

        qm.addBooking(booking);

        assert qm.getBookingByID(bookingID).getName().equals("Andri");
    }

    /**
     * Test if search by ID works as intended.
     */
    @Test
    public void testSearchID() {
        DayTourDetails[] results = qm.searchTourDetails("1234", LocalDate.parse("2023-03-20"), null);
        assert results[0].getTown().equals("Jökulsárlón");
    }

    /**
     * Test if search by Region works as intended.
     */
    @Test
    public void testSearchRegion() {
        DayTourDetails[] results = qm.searchTourDetails("Suðurland", LocalDate.parse("2023-03-20"), null);
        assert results[0].getTown().equals("Jökulsárlón");
    }

    /**
     * Test if search by Town works as intended.
     */
    @Test
    public void testSearchTown() {
        DayTourDetails[] results = qm.searchTourDetails("Jökulsárlón", LocalDate.parse("2023-03-20"), null);
        assert results[0].getTown().equals("Jökulsárlón");
    }

    /**
     * Test if search by Name works as intended.
     */
    @Test
    public void testSearchName() {
        DayTourDetails[] results = qm.searchTourDetails("South coast and Jökulsárlón", LocalDate.parse("2023-03-20"),
                null);
        assert results[0].getTown().equals("Jökulsárlón");
    }

    /**
     * Test searching for tour the does not exist.
     */
    @Test
    public void testSearchNotExists() {
        DayTourDetails[] results = qm.searchTourDetails("Flyover", LocalDate.parse("2023-03-20"), null);
        assert results.length == 0;
    }

    /**
     * Test searching for tour that is null. Should return all tours on the date.
     */
    @Test
    public void testSearchNull() {
        DayTourDetails[] results = qm.searchTourDetails(null, LocalDate.parse("2023-03-20"), null);
        assert results.length == 2;
    }

    /**
     * Tests searching for tour where date does not match.
     */
    @Test
    public void testSearchNoDate() {
        DayTourDetails[] results = qm.searchTourDetails("Jökulsárlón", LocalDate.parse("1998-03-20"), null);
        assert results.length == 0;
    }

    /**
     * Test searching for a tour where date is null.
     */
    @Test
    public void testSearchNullDate() {
        DayTourDetails[] results = qm.searchTourDetails("Jökulsárlón", null, null);
        assert results.length == 0;
    }

    /**
     * Test searching with a filter.
     */
    @Test
    public void testFilter() {
        // Add a tour similar to another existing tour to check filter function.
        DayTourDetails dtt = new DayTourDetails(1111, "Norðurland", "Mývatn", "Jarðböðin",
                95, 87, 6500, "Myvatn.png", 3, "Myvatn Nature Baths",
                "Hot Spring|Swimming|Relaxation|Family Friendly", "Myvatn er...", "10:00-16:00");

        dtt.addTime(LocalDate.parse("2023-03-20"), LocalTime.of(10, 0), 100);

        qm.addTour(dtt);

        // Check if the returned tour matches the filter.
        DayTourDetails[] results = qm.searchTourDetails("Norðurland", LocalDate.parse("2023-03-20"), "Hot Spring");
        assert results[0].getTown().equals("Mývatn");
    }

    /**
     * Test searching for a tour where filter matches no result.
     */
    @Test
    public void testSearchNoFilter() {
        DayTourDetails[] results = qm.searchTourDetails("Jökulsárlón", LocalDate.parse("2023-03-20"), "Spa");
        assert results.length == 0;
    }

    /**
     * Tests when all parameters are null.
     */
    @Test
    public void testAllNull() {
        DayTourDetails[] results = qm.searchTourDetails(null, null, null);
        assert results.length == 0;
    }

    /**
     * Test search where the return should be basic DayTour object.
     */
    @Test
    public void testSearchTour() {
        assert qm.searchTours("Norðurland", LocalDate.parse("2023-03-21"), null)[0].getRegion().equals("Norðurland");
    }
}
