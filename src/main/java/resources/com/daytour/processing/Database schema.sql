CREATE TABLE DayTours (  
	tourID integer PRIMARY KEY AUTOINCREMENT,
	region, 
	town, 
	name, 
	rating, 
	popularity integer, 
	price integer,
	pictures,
	length,  
	company,  
	filters, 
	description, 
	openingHours
);


CREATE TABLE DayTourDates ( 
	tourID integer, 
	date, 
	time integer, 
	availability integer, 
	PRIMARY KEY(tourID, date, time), 
	FOREIGN KEY (tourID) REFERENCES DayTours(tourID)
);

CREATE TABLE Bookings ( 
	name, 
	date, 
	time integer, 
	noPersons integer, 
	email, 
	hotelPickup, 
	payment, 
	totalPrice,
	tourID integer,
	bookingID integer PRIMARY KEY AUTOINCREMENT,
	FOREIGN KEY(tourID) REFERENCES DayTours(tourID)
);

CREATE TABLE Reviews (
	tourID integer,
	name,
	title,
	review,
	like,
	FOREIGN KEY(tourID) REFERENCES DayTours(tourID)
);