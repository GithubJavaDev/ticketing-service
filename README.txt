README
--------

Ticket Service is a Spring Boot based command line application for ticket reservation. It allows to view avaialable seats, hold  and reserve a group of seats. 

Since venue seats data should be available for this application to work, taken the approach to load that during app initialization. Based on the seating chart picture int the problem statement, number of seats data is dependent on two configured properties, row-count and column-count. Total number of seats are prodcut of these two properties. There will be in total 25 seats with the default configuration.

Uses local cache for storing seats, holds and reservation data. Every time application started, starts with a clean slate, holds and reservation data is cleared.

Since there is no clear definition for best seats, went with consecutive seats as best seats. This service can be enhanced once there are more requirements around that.

Some areas still needs some tightening around input validations (like not providing number of seats or email). have handled validations in some cases for demonstrating one of the approaches for that.

This is a console application, depends on three commands : seats, hold and reserve. Once the application started, intro message guides you through the flow. 

Below is the output on the console once the app is started.

Welcome to XYZ ticket service application. Please use the below commands to reserve seats.
- Input 'seats' for finding the number of available seats
- Input 'hold' for holding the seats.
- Input 'reserve' for reserving the seats

Type, seats for viewing available seats and the other commands as mentioned in the intro message. A reminder that no input validations added, enter number of seats, email or hold id values without missing.


Configuration
-------------
Hold expiration duration (hold-expiry-duration), row and column count of seats (row-count,column-count) are configurable parameters. They are defined in application.yml file.

hold-expiry-duration : value is in seconds. Default is 30 seconds.
row-count : Default is 5
column-count : Default is 5

Build
-----
Build the application using the command : gradle build

Run
----
Couple of ways to start the application :

	1. Using gradle, gradle bootRun -q from the root of the application

	2. Build generates a jar file under /ticketing-service/build/libs/ticketing-0.0.1-SNAPSHOT.jar. Execute the jar file from libs folder using, 
	java -jar ./ticketing-0.0.1-SNAPSHOT.jar


Tests
-----
Some more tests can be added for 100% coverage. Added tests for the three successful operations scnearios as well as couple of failed scenarios.

Execute the tests using the command : gradle test
