package com.daytour.processing;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Object to hold bookings for Day Tours.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @since   2023-02-03
 * @version 1.3
 **/
public class Booking 
{
    //Variables
    private final long tourID, bookingID;
    private final int noPersons, price;
    private final String name, email, payment;
    private final boolean pickUp;
    private final LocalDate date;
    private final LocalTime time;

    /**
     * Creates a new booking.
     *
     * @param tourID     The ID of the DayTour to which the booking belongs to.
     * @param bookingID  The ID of the booking.
     * @param name       The name of the person booking the tour.
     * @param email      E-Mail of the person booking the tour.
     * @param date       The date when the tour takes place.
     * @param time       The time when the tour takes place.
     * @param noPersons  The number of persons going on the tour.
     * @param pickUp     If hotel pickup is picked.
     * @param payment    The method of payment.
     * @param totalPrice Total price of the booking.
     */
    public Booking(long tourID, long bookingID, String name, String email, LocalDate date, LocalTime time,
                   int noPersons, boolean pickUp, String payment, int totalPrice)
    {
        this.tourID = tourID;
        this.name = name;
        this.email = email;
        this.date = date;
        this.payment = payment;
        this.time = time;
        this.noPersons = noPersons;
        this.price = totalPrice;
        this.pickUp = pickUp;

        this.bookingID = bookingID;
    }

    /**
     * Creates a new booking with a randomly generated booking ID.
     * 
     * @param tourID     The ID of the DayTour to which the booking belongs to.
     * @param name       The name of the person booking the tour.
     * @param email      E-Mail of the person booking the tour.
     * @param date       The date when the tour takes place.
     * @param time       The time when the tour takes place.
     * @param noPersons  The number of persons going on the tour.
     * @param pickUp     If hotel pickup is picked.
     * @param payment    The method of payment.
     * @param totalPrice Total price of the booking.
     */
    public Booking(long tourID, String name, String email, LocalDate date, LocalTime time,
                   int noPersons, boolean pickUp, String payment, int totalPrice)
    {
        this(tourID, ((long) (Math.random() * 10000000)), name, email, date, time, noPersons, pickUp, payment, totalPrice);
    }

    public long getTourID()         { return tourID; }

    public long getBookingID()      { return bookingID; }

    public String getName()         { return name; }

    public String getEmail()        { return email; }

    public LocalDate getDate()      { return date; }

    public String getPayment()      { return payment; }

    public LocalTime getTime()      { return time; }

    public int getNoPersons()       { return noPersons; }

    public int getTotalPrice()      { return price; }

    public boolean getHotelPickup() { return pickUp; }
}
