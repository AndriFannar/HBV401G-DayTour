package com.daytour.processing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Optional;

/**
 * Object to hold detailed version of DayTour.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @since   2023-02-01
 * @version 1.2
 **/
public class DayTourDetails extends DayTour
{
    //Variables.
    private final List<DayTourTimes> dates;
    private final List<Review> reviews;
    
    private final String company, filters, description, openingHours;
    private final int length;

    /**
     * Creates a new DayTourDetails object.
     *
     * @param tourID       The unique ID of the tour.
     * @param region       The region the tour takes place in.
     * @param town         The nearest town where the tour takes place.
     * @param name         The name of the tour.
     * @param rating       The overall rating of the tour.
     * @param popularity   How many bookings in the last week.
     * @param pictures     A picture of the tour.
     * @param length       The length of the tour.
     * @param company      The company responsible for the tour.
     * @param filters      Filters that apply to the tour.
     * @param description  The tour's description.
     * @param openingHours Opening hours of the company's premises.
     */
    public DayTourDetails(long tourID, String region, String town, String name, double rating, int popularity, int price, String pictures,
                          int length, String company, String filters, String description, String openingHours)
    {
        super(tourID, region, town, name, rating, popularity, price, pictures);

        this.length = length;
        this.company = company;
        this.description = description;
        this.openingHours = openingHours;

        this.filters = filters;

        dates = new LinkedList<>();
        reviews = new LinkedList<>();
    }

    /**
     * Creates a new DayTourDetails object.
     *
     * @param region       The region the tour takes place in.
     * @param town         The nearest town where the tour takes place.
     * @param name         The name of the tour.
     * @param rating       The overall rating of the tour.
     * @param popularity   How many bookings in the last week.
     * @param pictures     A picture of the tour.
     * @param length       The length of the tour.
     * @param company      The company responsible for the tour.
     * @param filters      Filters that apply to the tour.
     * @param description  The tour's description.
     * @param openingHours Opening hours of the company's premises.
     */
    public DayTourDetails(String region, String town, String name, double rating, int popularity, int price, String pictures,
                          int length, String company, String filters, String description, String openingHours)
    {
        this(((long) (Math.random() * 100000)), region, town, name, rating, popularity, price, pictures,
                length, company, filters, description, openingHours);
    }

    public int getLength()          { return length; }

    public String getCompany()      { return company; }

    public String getFiltersStr()   { return filters; }

    public String getDescription()  { return description; }

    public String getOpeningHours() { return openingHours; }

    public Review[] getReviews()    { return reviews.toArray(new Review[0]); }

    public LocalDate[] getDates()
    {
        return dates.stream()
                .map(DayTourTimes::getDate)
                .sorted()
                .toArray(LocalDate[]::new);
    }

    /**
     * Gets the available times at the specified date.
     * @param date The date when the tour takes place.
     * @return     Array of ints representing the available dates.
     */
    public LocalTime[] getTimes(LocalDate date)
    {
        var dtt = findDate(date);

        return dtt.map(dt -> Arrays.stream(dt.getTimes())
                                .sorted()
                                .toArray(LocalTime[]::new))
                                .orElse(new LocalTime[0]);
    }

    /**
     * Gets the availability at given time.
     *
     * @param date The day when the tour takes place.
     * @param time The time to check availability on.
     * @return     Returns the availability at given time.
     */
    public int getAvailable(LocalDate date, LocalTime time)
    {
        var dtt = findDate(date);

        return dtt.map(dt -> dt.getAvailable(time)).orElse(0);
    }

    /**
     * Finds the correct date inside the list.
     *
     * @param date The date to be found.
     * @return     The DayTourTimes objects that fits the date.
     */
    private Optional<DayTourTimes> findDate(LocalDate date)
    {
        return dates.stream()
                .filter(dtt -> dtt.getDate().equals(date))
                .findFirst();
    }


    /**
     * Adds times and availability to the day tour.
     *
     * @param date      The date when the tour takes place.
     * @param time      Times at which booking is possible. If time already exists, it will be ignored.
     * @param available Availability at each time.
     */
    public void addTime(LocalDate date, LocalTime time, Integer available)
    {
        var dtt = addDate(date);

        dtt.addTime(time, available);
    }

    /**
     * Adds a new date to the day tour.
     *
     * @param date The date to add. If it exists, it will not be added again.
     * @return New DayTourTimes object with new date, if it was added.
     */
    public DayTourTimes addDate(LocalDate date)
    {
        return dates.stream()
                .filter(dtt -> dtt.getDate().equals(date))
                .findFirst()
                .orElseGet(() -> {
                    var newDtt = new DayTourTimes(date);
                    dates.add(newDtt);
                    return newDtt;
                });
    }

    private void updateRating()
    {
        long dislikes = reviews.stream()
                .filter(rw -> !rw.getLiked())
                .count();

        double rating = (1 - ((double)dislikes / reviews.size())) * 100;

        super.setRating(rating);
    }

    public void addReview(Review review)
    {
        reviews.add(review);
        updateRating();
    }

    @Override
    public String toString()
    {
        return super.getID() + "|" + super.getRegion() + "|" + super.getTown() + "|" +
                super.getName() + "|" + super.getRating() + "|" + super.getPopularity() + "|" +
                super.getPrice() + "|" + super.getPictures()[0] + "|" + length + "|" + company +
                "|" + this.getFiltersStr() + "|" + description + "|" + openingHours;

    }

}