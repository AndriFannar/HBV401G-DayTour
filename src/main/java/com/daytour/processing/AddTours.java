package com.daytour.processing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * Adds Day Tours to database by reading in CSV files.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @since   2023-03-01
 * @version 1.1
 **/
public class AddTours 
{
    //File Paths.
    private static final Path PATH_TO_FILE = Paths.get("src/main/java/resources/com/daytour/processing/tours.csv");
    private static final Path PATH_TO_DATES = Paths.get("src/main/java/resources/com/daytour/processing/dates.csv");

    public static void main(String[] args) throws IOException
    {
        Query q = new Query();

        System.out.println("INFO: Adding new tours...");

        // Tries to create a new scanner for each file.
        try ( Scanner sc = new Scanner(Files.newInputStream(PATH_TO_FILE));
              Scanner tsc = new Scanner(Files.newInputStream(PATH_TO_DATES)) )
        {
            //Sets a delimiter.
            sc.useDelimiter(";");
            tsc.useDelimiter(";");

            //Creates required arrays to store information.
            String[] info;
            String[] dates = null;
            String[] times = null;
            String[] available = null;
            DayTourDetails dtt;


            //Reads every line of the document.
            while (sc.hasNextLine())
            {
                //Reads info about Day Tour and puts into array.
                info = sc.nextLine().split(";");

                //Replaces every occurrence of the character ' to ease database insertion.
                for (int i = 0; i < info.length; i++)
                {
                    info[i] = info[i].replace("'", "");
                }

                //Creates a new DayTourDetails object from the information.
                dtt = new DayTourDetails(info[0], info[1], info[2], Double.parseDouble(info[3]), Integer.parseInt(info[4]), Integer.parseInt(info[5]), info[6], Integer.parseInt(info[7]), info[8], info[9], info[10], info[11]);

                System.out.printf("INFO: Adding tour %s...\n", dtt.getName());

                //Gets the dates, times, and availability for the tour.
                if (tsc.hasNextLine())
                {
                    dates = tsc.nextLine().split(";");
                    times = tsc.nextLine().split(";");
                    available = tsc.nextLine().split(";");
                }

                //Adds the dates and times to the DayTourDetails object.
                if (dates != null)
                {
                    for (String date : dates)
                    {
                        for (int i = 0; i < times.length; i++)
                        {
                            dtt.addTime(LocalDate.parse(date), LocalTime.of(Integer.parseInt(times[i]), 0), Integer.parseInt(available[i]));
                        }
                    }
                }

                //Adds the Day Tour to the database.
                q.addTour(dtt);
            }
        }

        System.out.println("INFO: All tours have been added.");
    }
}
