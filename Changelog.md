# Changelog

## V2.2

### Optimized code for better performance and readability.

## V2.1.1

### Database Changes
* All tours and dates have now been added to the database.
  * A backup of a clean version of the database can be found in the "Database Backup" folder.

### DatabaseConnection.java
* It is now possible to set a custom path to the database.

### DayTourDetails.java
* Fixed how ratings are calculated.

### Query.java
* It is now possible to set a custom path to the database.
* Added sorting by price.


## V2.1

### Java Changes

#### Booking.java
* Booking ID is now automatically generated when creating a new Booking object.
* New function getBookingID() returns a type long with the booking ID.
* Booking now requires a tourID instead of a DayTourDetails object in its constructor.
  * The function getTour() has been replaced with getTourID().
* Changed how dates and times are stored.
  * No longer stored as Strings and Integers, now stored as LocalDate and LocalTime.
  * The Booking constructor now requires LocalDate and LocalTime.
  * The functions getDate() and getTime() return LocalDate and LocalTime.
* Added a new constructor that also takes bookingID as an argument to allow the Query class to create an existing booking.

### DatabaseConnection.java
* The function connectDatabase() is no longer static.
* The function connectDatabase() now returns a list of generic objects rather than a list of DayTour objects.
  * This is so the function can return a list of Booking objects.
  * The function now takes a boolean 'booking' as an argument, which signals if it should return Booking objects.
* Added functionality to get bookings from database.
* Changed how DayTourDetails objects are created to allow adding of multiple dates to the same object.
  * This makes it possible to search for tours on a date range.

### DayTour.java
* The constructor now requires different data types as input for some values.
  * This is done so the constructor does not have to handle converting from String to other data types.
* Added a new constructor that does not require tourID as input to ease creation of tours not in the system.
* Defined the equals function.
* The getID() function now returns a long data type.

### DayTourDetails.java
* The constructor has the same changes as in the DayTour object.
  * A new constructor without tourID as an input has also been created.
* Dates and times are now stored as LocalDate and LocalTime.
  * findDate() now requires LocalDate instead of String.
  * getTimes() now returns a sorted LocalTime array instead of an int array.  
  * getAvailable() now requires LocalDate and LocalTime as input instead of a String and an int.
  * addTime() now requires a LocalDate and Local Time as input instead of a String and an int.
  * getDates() now returns an array of sorted LocalDate objects.

### DayTourTimes.java
* Now stores dates and times as LocalDate and LocalTime.
  * The constructor now requires LocalDate instead of String.
  * addTime() now requires LocalTime instead of an int.
  * getDate() now returns LocalDate.
  * getTimes() now returns an array of LocalTime.
  * getAvailable() now requires LocalTime instead of an int

### Query.java
* Now implements an interface.
* All functions have been changed to no longer being static.
* Refined addBooking() function.
* Changed the searchTours() function
  * Now three separate functions.
  * The private function searchTours() has all the same functionality as the previous public searchTours() function.
  * Two new public functions, searchTours() and searchTourDetails(), which serve as 'wrapper' functions, and simplify the search.
  * Added the ability to search for tours in a specified date range, with dateStart and dateEnd.
    * Dates are now of type LocalDate.
* New function getTourBookings().
  * Returns a list of bookings for a specified tour on a specific date.
* New function getBookingByID.
  * Returns a booking matching the given ID.

### QueryInterface.java
* New interface for the Query class.

### Review.java
* Refactored variable names.

## V2.0

### File Structure Changes
* Removed /database folder.
* Split Database class into two separate classes.
* Split DayTour into two separate classes.
* Put DayTourDates into its own class.
* Added a new class that adds tours to database from file, AddTours.java.
* Added a new class for bookings, Bookings.java.

### Database Schema Changes

#### DayTours Table
* Price and Pictures columns have been moved to a new location.
  * This is to ease creation of basic DayTour objects.
* Reviews column has been removed.
  * Reviews are now kept in a separate table.

#### Reviews Table
* New table to hold reviews.
* Linked to DayTour by tourID foreign key.
* Stores more information than old review implementation.
  * Stores name of person reviewing the tour.
  * Stores a title for the review.
  * Stores the review itself.
  * Stores like/dislike of tour with simple true/false.

### Java Changes

#### AddTours.java
* Adds Day Tours to database.
  * Reads tours.csv and dates.csv located inside /processing folder in the resources branch.

#### Booking.java
* New class to hold booking info.
* Is passed to database function to add booking to database.
* Is linked to a DayToursDetails object.
* Stores all information required for a booking.

#### DatabaseConnection.java
* New class that has the single purpose of connecting to the database and executing statements.
* Built on connectDatabase function from old Database class.
* Insertion is now selected by setting the insert boolean to true.
  * This replaces the old behaviour of the operation char parameter.
* The resultSize parameter has been removed.
  * This is due to the implementation of a new data structure.
* The boolean details has been added.
  * Replaces the behaviour of resultSize set as 1.
  * This is to make detailed results possible for more than one tour at a time.

#### DayTour.java
* The inner class DayTourTime has been given its own class, DayTourTimes.
* The functionality of creating detailed DayTour objects has been moved to a separate class, DayTourDetails.
* Changed how information is stored.
* A tourID is now automatically generated if parameter is null.
* The getID function now returns type int.
* The getPopularity function now returns type int.
* The getPrice function now returns type int.
* Removed function getRatingRaw.
  * This is possible due to new rating format.
* Removed all functions associated with the detailed version of DayTour
  * This includes time and availability functions.

#### DayTourDetails.java
* New class that holds detailed DayTour objects.
  * Extends the DayTour class.
* The function getReviews now returns an array of Review objects.
* Now holds a list of DayTourTimes objects.
  * These represent different dates and available time slots on those dates.
* The function getTimes now requires a date String as an argument.
  * This is necessary due to new time data structure that can hold times for multiple dates inside the same DayTourDetails object.
* The function getAvailable now requires a date String as an argument for the same reason as stated above.
* The function addTimes has been renamed to addTime.
  * The function now requires a date String for the same reason as stated above.
  * The function now only adds one time slot at a time and thus only takes integers for time and availability.
    * As opposed to the old requirement of an integer array.
* New function getFiltersStr returns all filters in one string, separated by pipe ("|") symbol.
* New function addReview adds a given review to the tour and updates the rating.

#### DayTourTimes.java
* New class that holds times and availability for a DayTourDetails object.
* The usage of the class has been changed somewhat.
  * The constructor now only takes a String date as an argument.
  * The class now holds all time slots for a given date.
  * All times are added through a separate addTime function.
* New function addTime adds a time slot and availability.
* New function getDate returns the date of the tour the time slots are meant for.
* The getTime function has been renamed to getTimes
  * It now returns an array of integers, representing available time slots.
* The getAvailable function has been changed.
  * It now requires a time parameter to return availability for that given time slot.

#### Query.java
* Includes all previous functions from Database.java, except connectDatabase which has been moved to its own class.
* The function addTour now only takes a DayTourDetails object as a parameter. 
* The function addBooking now only takes a Booking object as a parameter.
* The function addReview now takes a Review object instead of a review string.
* The addRating function has been removed.
  * The rating will from now on be calculated from like to dislike ratio of the reviews.
* The searchTours function has been overhauled:
  * It is no longer necessary to specify resultSize, due to implementation of a new data structure.
  * Detailed tour info can now be acquired by setting the details boolean parameter as true.
    * This replaces the old behaviour that detailed info is only accessible by setting the resultSize as 1.
  * By choosing to search for detailed tour info, the function will return DayTourDetails array.

#### Review.java
* Changed the likeable variable to boolean.
  * Due to this the constructor now requires a boolean as an argument for likeable.