package com.daytour.processing;

/**
 * Object to hold simple Day Tours.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @since   2023-02-01
 * @version 2.1
 **/
public class DayTour
{
    //Variables.
    private final int popularity, price;
    private final long tourID;
    private double rating;
    private final String region, town, name, pictures;

    /**
     * Creates a new basic DayTour object.
     *
     * @param tourID       The unique ID of the tour.
     * @param region       The region the tour takes place in.
     * @param town         The nearest town where the tour takes place.
     * @param name         The name of the tour.
     * @param rating       The overall rating of the tour.
     * @param popularity   How many bookings in the last week.
     * @param pictures     A picture of the tour.
     */
    public DayTour(long tourID, String region, String town, String name, double rating, int popularity, int price, String pictures)
    {
        this.tourID = tourID;

        this.region = region;
        this.town = town;
        this.name = name;
        this.rating = rating;
        this.popularity = popularity;
        this.price = price;
        this.pictures = pictures;
    }

    /**
     * Creates a new basic DayTour object.
     *
     * @param region       The region the tour takes place in.
     * @param town         The nearest town where the tour takes place.
     * @param name         The name of the tour.
     * @param rating       The overall rating of the tour.
     * @param popularity   How many bookings in the last week.
     * @param pictures     A picture of the tour.
     */
    public DayTour(String region, String town, String name, double rating, int popularity, int price, String pictures)
    {
        this(((long) (Math.random() * 100000)), region, town, name, rating, popularity, price, pictures);
    }

    public String getRegion()            { return region; }

    public String getTown()              { return town; }

    public String getName()              { return name; }

    public long getID()                  { return tourID; }
    
    public int getPopularity()           { return popularity; }

    public int getPrice()                { return price; }

    public String[] getPictures()        { return pictures.split(";"); }

    public String getPicturesStr()       { return pictures; }

    public double getRating()            { return rating; }

    public void setRating(double newRtg) { rating = newRtg; }
    
    public String toString()             { return tourID + ""; }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
            return true;

        if (!(o instanceof DayTourDetails dtt))
            return false;

        return dtt.getID() == this.getID();
    }
}