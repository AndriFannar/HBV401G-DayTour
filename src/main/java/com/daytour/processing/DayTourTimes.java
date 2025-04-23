package com.daytour.processing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * Object to hold times for DayTour objects.
 * 
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @since   2023-02-02
 * @version 2.0
 */
public class DayTourTimes
{
    //Variables
    private final LocalDate d;
    private final HashMap<LocalTime, Integer> a;

    /**
     * Object to hold times for DayTour objects.
     * 
     * @param date Date of the tour.
     */
    public DayTourTimes(LocalDate date)
    {
        d = date;
        a = new HashMap<>();
    }

    /**
     * Adds a time slot.
     *
     * @param time      Time for the new slot.
     * @param available How many people can book.
     */
    public void addTime(LocalTime time, int available)
    {
        a.put(time, available);
    }

    public LocalDate getDate() { return d; }

    public LocalTime[] getTimes() {
        return a.keySet().stream()
                .map(LocalTime::from)
                .toArray(LocalTime[]::new);
    }

    /**
     * Gets the availability at the specified time.
     *
     * @param time The time to get the availability on.
     * @return     Availability at the specified time.
     */
    public int getAvailable(LocalTime time) { return a.get(time); }
}