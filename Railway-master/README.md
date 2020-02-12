Student: Nick Schinke/Joseph Denzer
Course: CS485
Date: 5-Feb-2019
Assignment: Railway Database
Instructor: John Bartucz

2/5/2019
Clients Jon Collins and Andrew Poss.

Client Demands: 
Trains with an ID and a name
Stations, schedules, routes, departure and arrival times
Sequenced numbered stations and schedules
Passenger booking consisting of seat, from station, to station, coach class, route, passenger name
Graph of train routes

Project Decisions: 
Programming Language: Used Java for familiarity and because Java provides use of prepared statements which are essential to protect against SQL Injections, and Java provides the password tools needed to encrypt user passwords.

2/14/19 Discussion on Railway Database Project.  
Attendees: Nick Schinke, Joe Denzer
 
Discussed what queries, methods and operations the database should perform.
Entered in train information gathered from the clients.
 
The following is an initial list that we would incorporate. More will be added further into construction. We thought through each query and whether our relations were set up correctly to be able to run the queries. This is our list, and not the demands of our clients. This is what we felt we could do. 
1.     Passenger contacts agent to buy a ticket.
2.     Passenger registers.
3.     Passenger views train schedule through the agent.
4.     Search if train runs to a certain station.
5.     Search for a certain passenger
6.     List of passengers on a certain train
7.     Cancel a ticket
8.     What is the train status?
9.     Check a passenger’s seat number 
10.    List all routes

 
Began construction on Java program to be run with Eclipse.
# Railway
Railway DB Project

2/21/2019 - Updated ER Diagram https://github.com/NKSchinke/Railway/blob/master/UpdatedERDiagram.png

Schema
Train((pk)trainID,trainName,bookedPassengers, capacity, activeTSID)
TicketAgent((pk)AgentID, firstName, lastName, stationID)
TrainSchedule((pk)TSID, trainID, routeID, departureTime, arrivalTime)
Route((pk)routeID, routeName, departureStation, arrivalStation)
Station((pk)stationID, stationPhoneNo, stationName)
Passenger((pk)ticketID, firstName, lastName, phoneNo, passengerID, login, hash, salt)
Ticket((pk)ticketID, TSID, ticketType, seatNo, passengerID}


Began construction on Java program to be run with Eclipse.

2/22/2019 Joe
I am experiencing problems loading new passengers into the database after startup because the passengerID and ticketID reset back to the starting point and that gives me a primary key duplicate warning. We need to come up with something different for a primary key or make the system remember what the last issued ID numbers were. 
I’m thinking we will have to do a query to get the last entry of these ID’s, and increment them from there. 

2/25/2019 
Having issues getting the queries to run in java code, so for now will just write out statements and the SQL query that works for it.

Get the highest passengerID & ticketID number out of the database. This will be needed to assign the next highest numbers to the next passenger when issuing a ticket.
select MAX(passengerID) AS passID from Passenger
select MAX(ticketID) AS maxTicketID from Passenger


2. /*list all passengers departing from a certain city*/
SELECT firstName, lastName  
FROM Passenger, Ticket, TrainSchedule, Route
WHERE Passenger.passengerID = Ticket.passengerID
AND Ticket.TSID = TrainSchedule.TSID
AND TrainSchedule.routeID = Route.routeID
AND Route.routeName LIKE 'Chicago%'

3. /*See if a someone is a passenger on a train */
Select ticketID, firstName, lastName, phoneNo, passengerID
FROM Passenger
WHERE Passenger.firstName LIKE 'Groucho' AND Passenger.lastName LIKE 'Marx'

4. /* See how to get to a certain city from another one */
/*need to ask for current and destination station*/
/*first make sure that trains service these 2 stations */

SELECT stationID AS Departure FROM Station
WHERE stationName LIKE 'Miami'
********************************
/* See how to get to a certain city from another one */
/*need to ask for current and destination station*/
/*first make sure that trains service these 2 stations */

SELECT stationID AS Arrival FROM Station
WHERE stationName LIKE 'reno'

/* See if there is a direct route to destination that leaves
out of Passenger's stated departure city(Reno) */
SELECT * FROM Route
WHERE departureStationID = 430
  
//the above query will show only route is to Chicago(13)

/* See which route goes to Reno that leaves
out of a Chicago (13)  */
SELECT * FROM Route
WHERE departureStationID = 13

Now need to book the passenger on a train to Chicago and a train from Chicago to Reno.

5. Book a passenger on a train. I think it may be too difficult to have them pick a seat number. We may have to go with general seating for either first class or coach.

This query mostly works, but I can’t retrieve the MAX ticket and passenger ID’s to increment them by 1.

6. List passengers on a certain train by train name

SELECT firstName, lastName, Train.trainName
FROM Passenger, Train, Ticket, TrainSchedule
WHERE Passenger.passengerID = Ticket.ticketID
AND Ticket.TSID = TrainSchedule.TSID
AND TrainSchedule.trainID = Train.trainID
AND Train.trainName = 'Polar Express'

7. Void a ticket if passenger cancels trip

UPDATE Ticket
SET ticketStatus='Void'
WHERE ticketID = 18;

Then must also remove passenger from passenger list

DELETE FROM Passenger
WHERE firstName LIKE 'Johan' 
AND lastName LIKE 'Smith'

2/26/2019
Figured out how to return the highest ticket and passenger ID numbers as integers. Still need to work on case 2 to determine if the station the customer is at is on a direct route to their destination or if they need to travel to Chicago first to get on another train. Plus need to update the ticket table. 



3/3/2019
Will probably need to delete the ticket ID from the passenger table. It will be confusing if one passenger purchases more than on ticket. Plus, I don't believe it meets 3NF anyway. 

3/3/2109
Discovered late that we should probably remove the ticketID from the Passenger table. This would be to avoid duplicating passengers every time they purchase another ticket, and it probably is violating 3NF. Decided that it was too late to make this change as our project was too far behind schedule due to programming issues earlier. 

3/3/2019 - Valid Username / Password for logging in as TA is "USERNAME = AGENT1" & "PASSWORD = LOGIN1"

3/3/2109
Client Feedback: We emailed, and sent messages on Discord to our clients requesting consultation and feedback to our project, but received no response.

3/4/2019
Still no response from clients. No doubt they are busy with other matters. 

Design Decisions:
3/4/2019
Keys: Foreign keys are used simply between tables with relations, nothing too special was done.
      Primary keys were some incremental, but in some instances bunched logically. Routes that were apart of one large transit have keys that are logically close. For example, Chicago to SanDiego is #21, SanDiego to Albuquerque is #22, and Alburquerque to Chicago is 23. Chicago to Reno is 31, Reno to SanFrancisco is 32, and SanFrancisco to Chicago is 33, leaving 34-40 empty incase that route expands. This is similarly done with TrainScheduleIDs, but with 100s, rather than 10s.

Normalization: We did basic normalization (1F/2F/3F), putting things into seperate tables as needed to avoid insertion/deleting/modification anomolies. Passengers are seperate from their Tickets so that a single passenger can own many tickets without needing to repeat that passenger info. Train Schedules can reuse routes through the Routes table, which also allows routes to exist even if no train is currently running that route. There are also Agents that work at each station but are seperated into their own tables, in case the event where a agent moves to a different station occurs.

Not much was done with denormalization in this project, we didn't see any instance where it would be super beneficial.

Groupwork Documentation:
Submitted independently.

Demands Met:
Trains with an ID and a name
Stations, schedules, routes, departure and arrival times
Sequenced numbered stations and schedules
Passenger booking consisting of seat, from station, to station, coach class, route, passenger name
Passenger chooses seat* this was not done as it would be 

Demands we felt were beyond the scope of this project:
Graph of train routes
Keeping track of how many people were booked on a train. We left that as an attribute in the Train table, but didn't make it work. 

Bonus Capabilities:
Passenger user options:
View routes, trains, schedules, times, look up seat number
Ticket Agent options: 
List all passengers (in the event of an emergency)

3/4/19
Here is our original list again, with what we could and could not do.
1.     Passenger contacts agent to buy a ticket. //Completed
2.     Passenger registers. //Completed
3.     Passenger views train schedule through the agent.//Completed & added functionality of passenger viewing schedule alone
4.     Search if train runs to a certain station.//Completed
5.     Search for a certain passenger. //Completed
6.     List of passengers on a certain train. This was not completed on an individual train basis, but as all passengers total
7.     Cancel a ticket. //This was not completed due to time constraints.
8.     What is the train status? //This avenue was dropped to wish-list status. We felt that the railroad most likely had back-up trains to fill in anyway. 
9.     Check a passenger’s seat number.//This was completed as an option for the ticket agent, and the passenger. 
10.    List all routes. //Completed


