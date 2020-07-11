/*
    Mason Davis
    CS 330 Winter 2020
    Program #4

    Description:
    This program makes use of the JDBC to connect to a MySQL server and
    perform actions by providing statements to insert,update, etc into the
    tables within the dbDavisTrack database.
*/

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DavisP4 
{
	private String hostname = "hostname";
    private	String port     = "port";
    private String database = "databaseName";
    private String user     = "username";
	private String password = "password";
	private String flags = "flags";

    public static void main(String args[]) throws Exception 
    {
        Class.forName("com.mysql.jdbc.Driver");

        PreparedStatement prepStmt;
        CallableStatement cStmt;
        
        String userChoice;

        String firstName;
        String lastName;
        String gender;
        String athleteID;
        String eventID;
        String schoolID;
        String mark;

        String query;
        boolean gotResults;
        ResultSet rs;

        try 
        {
			//Connection conn = DriverManager.getConnection("jdbc:mysql://"
			//	+ hostname + "/" + database + flags, user, password);

            Connection conn = DriverManager.getConnection("jdbc:mysql://host:port/databaseName?autoReconnect=true&useSSL=false", "username", "password");
            
            do
            {
                // Display options to user
                System.out.println("\nPlease select an option:");
                System.out.println("1: Add a new Athlete");
                System.out.println("2: Enter a new result");
                System.out.println("3: Get the results for an event");
                System.out.println("4: Score an event");
                System.out.println("5: Disqualify an athlete for one event");
                System.out.println("6: Disqualify an athlete for the meet");
                System.out.println("7: Score the meet");
                System.out.println("8: QUIT");
                System.out.print("Enter number: ");

                // get user input
                userChoice = System.console().readLine();

                switch(Integer.parseInt(userChoice))
                { 
/*
    Case 1: INSERT an athlete into the athlete table. Take a first and last
    name, a gender, and a schoolID number. If the athlete already exists, let
    the user know, and prompt them to re-enter
*/
                    case 1:
                    do
                    {  
                        // Get appropriate input
                        System.out.print("\nEnter Athlete's first name: ");
                        firstName = System.console().readLine();

                        System.out.print("Enter Athlete's last name: ");
                        lastName = System.console().readLine();

                        System.out.print("Enter Athlete's gender (m or f): ");
                        gender = System.console().readLine();

                        System.out.print("Enter Athlete's school ID: ");
                        schoolID = System.console().readLine();

                        // Check if athlete already exists;
                        query = "SELECT * from athlete where firstName = (?) and lastName = (?) and schoolID = (?)";
                        
                        // Create statement to execute query
                        prepStmt = conn.prepareStatement(query);

                        // Set values in query
                        prepStmt.setString(1, firstName);
                        prepStmt.setString(2, lastName);
                        prepStmt.setString(3, schoolID);

                        prepStmt.execute();

                        rs = prepStmt.getResultSet();

                        // if there is already a record for that athlete
                        if(rs.first() == true)
                        {
                            System.out.println("\nDuplicate athlete. Try again.");
                        }
                    }
                    while(rs.first() == true);

                        // Create SQL query to INSERT an athlete
                        query = "INSERT into athlete (firstName, lastName, gender, schoolID) values (?, ?, ?, ?)";
                        
                        // Create statement to execute query
                        prepStmt = conn.prepareStatement(query);

                        // Set values in query
                        prepStmt.setString(1, firstName);
                        prepStmt.setString(2, lastName);
                        prepStmt.setString(3, gender);
                        prepStmt.setString(4, schoolID);

                        // Execute query
                        prepStmt.executeUpdate();

                        break;

/*
    Case 2: INSERT a new result for an athlete into the results table. Take an
    athleteID number, an eventID number, and the mark that the athlete
    received for the event.
*/
                    case 2: 
                        // Get appropriate input
                        System.out.print("\nEnter Athlete's ID Number: ");
                        athleteID = System.console().readLine();

                        System.out.print("Enter Event's ID Number: ");
                        eventID = System.console().readLine();

                        System.out.print("Enter Athlete's mark: ");
                        mark = System.console().readLine();

                        // Create SQL query to INSERT an event
                        query = "INSERT into results (athleteID, eventID, mark)" + "values (?, ?, ?)";
                        
                        // create statement to execute query
                        prepStmt = conn.prepareStatement(query);
                         
                        // set values in query
                        prepStmt.setString(1, athleteID);
                        prepStmt.setString(2, eventID);
                        prepStmt.setString(3, mark);

                        // execute query
                        prepStmt.executeUpdate();

                        break; 

/*
    Case 3: RETRIEVE results from an event. Take an event's ID number, and
    retrieve (SELECT) all of the results for that event
*/
                    case 3: 
                        // Get appropriate input
                        System.out.print("\nEnter Event's ID Number: ");
                        eventID = System.console().readLine();

                        // Create SQL query to select the required fields
                        query = "SELECT results.place, athlete.firstName, athlete.lastName, event.eventID, event.eventName, results.mark, results.points, school.name, results.disqualified from school join athlete on athlete.schoolID = school.ID join results on athlete.athleteID = results.athleteID join event on results.eventID = event.eventID where event.eventID = (?) order by results.place";
                        
                        // create statement to execute query
                        prepStmt = conn.prepareStatement(query);

                        // set values in query
                        prepStmt.setString(1, eventID);

                        // execute query
                        gotResults = prepStmt.execute();

                        // get the result set from query
                        rs = prepStmt.getResultSet();

                        // go to first record in result set
                        rs.first();

                        // print out column headers for table
                        System.out.printf("\n|%-6s|%-8s|%-9s|%-8s|%-12s|%-7s|%-7s|%-7s|%-12s|", "Place", "First", "Last", "EventID", "Event", "Result", "Points", "School", "Disqualified");
                        System.out.println("\n--------------------------------------------------------------------------------------");
                        
                        // print out the data for each athlete in that event
                        while (gotResults)
                        {
                            int athletePlace = rs.getInt("place");
                            String fName = rs.getString("firstName");
                            String lName = rs.getString("lastName");
                            int eventIDNumber = rs.getInt("eventID");
                            String eventName = rs.getString("eventName");
                            float athleteMark = rs.getFloat("mark");
                            int points = rs.getInt("points");
                            String schoolName = rs.getString("name");
                            String DQ = rs.getString("disqualified");

                            System.out.printf("|%-6s|%-8s|%-9s|%-8s|%-12s|%-7.5s|%-7s|%-7s|%-12s|\n", athletePlace, fName, lName, eventIDNumber, eventName, athleteMark, points, schoolName, DQ);
                            
                            gotResults = rs.next();
                        }
                        System.out.println();

                        break; 

/*
    Case 4: SCORE a single event. Take an event's ID number to determine which
    event it is (Track/Field). Depending on the results, call a stored
    procedure to assign a place to the athletes based on the marks they
    received for a result. Then call a procedure to assign a point total based
    on the place the athletes have.
*/
                    case 4:
                        // Get appropriate input
                        System.out.print("\nEnter Event's ID Number: ");
                        eventID = System.console().readLine();

                        // Create SQL query to determine event type
                        query = "SELECT eventType from event where eventID = (?)";
                        
                        // create statement to execute query
                        prepStmt = conn.prepareStatement(query);

                        // set values in query
                        prepStmt.setString(1, eventID);

                        // Execute query
                        prepStmt.execute();

                        // get the result set from query
                        rs = prepStmt.getResultSet();

                        // go to first record in result set
                        rs.first();

                        // Assign an athlete's place in an event based on the
                        // type of event it is 
                        if(rs.getString("eventType").equals("track"))
                        {
                            cStmt = conn.prepareCall
                                ("{call assignPlaceTrack(?)}");
                            cStmt.setString(1, eventID);
                            cStmt.execute();

                            // score the event
                            cStmt = conn.prepareCall
                                ("{call scoreEventTrack(?)}");
                            cStmt.setString(1, eventID);
                            cStmt.execute();
                        }
                        else
                        {
                            cStmt = conn.prepareCall
                                ("{call assignPlaceField(?)}");
                            cStmt.setString(1, eventID);
                            cStmt.execute();

                            // score the event
                            cStmt = conn.prepareCall
                                ("{call scoreEventField(?)}");
                            cStmt.setString(1, eventID);
                            cStmt.execute();
                        }

                        break; 

/*
    Case 5: DISQUALIFY an athlete for ONE event. take an athlete's ID number
    and an event ID number. Update their disqualified status to "Yes" in the
    results table for that specific event
*/
                    case 5:
                        // Get appropriate input
                        System.out.print("\nEnter Athlete's ID Number: ");
                        athleteID = System.console().readLine();

                        System.out.print("Enter Event's ID Number: ");
                        eventID = System.console().readLine();

                        // Create SQL query to update disqualified status
                        query = "UPDATE results set disqualified = 'Yes' where athleteID = (?) and eventID = (?)";
                        
                        // create statement to execute query
                        prepStmt = conn.prepareStatement(query);
                         
                        // set values in query
                        prepStmt.setString(1, athleteID);
                        prepStmt.setString(2, eventID);
                         
                        // execute query
                        prepStmt.executeUpdate(); 

                        break;
                        
/*
    Case 6: DISQUALIFY an athlete for the entire meet. Take an athlete's ID
    number and call a procedure to update their disqualified status in the
    athlete's table, then call a procedure to update their disqualified status
    in the results table for all of the results that the athlete participated
    in. 
*/
                    case 6: 
                        // Get appropriate input
                        System.out.print("\nEnter Athlete's ID Number: ");
                        athleteID = System.console().readLine();

            // ****** Update disqualified status for athlete ******

                        cStmt = conn.prepareCall
                            ("{call disqualifyAthlete(?)}");
                        cStmt.setString(1, athleteID);
                        cStmt.execute();

            // ****** Update disqualified status for all events ******

                        cStmt = conn.prepareCall
                        ("{call disqualifyAllEvents(?)}");
                        cStmt.setString(1, athleteID);
                        cStmt.execute();

                        break; 

/*
    Case 7: SCORE the entire meet. Total up all of the points each athlete has
    based on their place in each event. Separate the points by school, and
    further separate them again by gender.
*/
                    case 7: 
                        // Create SQL query to get the number of schools
                        query = "SELECT count(*) from school";

                        // create statement to execute query
                        prepStmt = conn.prepareStatement(query);
                        
                        // Execute query
                        prepStmt.execute();

                        // get the result set from query
                        rs = prepStmt.getResultSet();

                        // go to first record in result set
                        rs.first();

                        int numOfSchools = rs.getInt(1);

                        // Set up arrays to hold the separate schools point
                        // total. Each index correlates to a schoolID. Not
                        // using index zero
                        int mensTotals[] = new int[numOfSchools + 1];
                        int womensTotals[] = new int[numOfSchools + 1];

                        query = "SELECT event.gender, athlete.schoolID, results.points, results.disqualified from event join results on event.eventID = results.eventID join athlete on athlete.athleteID = results.athleteID";

                        // create statement to execute query
                        prepStmt = conn.prepareStatement(query);
                        
                        // Execute query
                        gotResults = prepStmt.execute();

                        // get the result set from query
                        rs = prepStmt.getResultSet();

                        // go to first record in result set
                        rs.first();

                        int sum;

                        // Go through each record one by one. take the
                        // athlete's points, and add them to whatever value is
                        // in the index of the appropriate array that
                        // correlates to their schoolID for a sum total.
                        // Place the calculated sum back into the array at the
                        // appropriate index
                        while(gotResults)
                        {
                            // Detect gender and disqualified status
                            if(rs.getString("gender").equals("M") && rs.getString("disqualified").equals("No"))
                            {
                                // add points to the previous value in array
                                sum = mensTotals[rs.getInt("schoolID")] + rs.getInt("points");
                                // insert new value in array
                                mensTotals[rs.getInt("schoolID")] = sum;
                            }
                            else if(rs.getString("gender").equals("F") && rs.getString("disqualified").equals("No"))
                            {
                                sum = womensTotals[rs.getInt("schoolID")] + rs.getInt("points");
                                womensTotals[rs.getInt("schoolID")] = sum;                                
                            }

                            gotResults = rs.next();
                        }
                        int i = 1;
                        while(i < mensTotals.length)
                        {
                // ****** Update mens points in school table ******

                            query = "UPDATE school set mensTotal = (?) where ID = (?)";
                            
                            // create statement to execute query
                            prepStmt = conn.prepareStatement(query);

                            // set values in query
                            prepStmt.setString(1, 
                                String.valueOf(mensTotals[i]));
                            prepStmt.setString(2, String.valueOf(i));

                            // Execute query
                            prepStmt.executeUpdate();

                // ****** Update womans points in school table ******

                            query = "UPDATE school set womensTotal = (?) where ID = (?)";
                            
                            // create statement to execute query
                            prepStmt = conn.prepareStatement(query);

                            // set values in query
                            prepStmt.setString(1, 
                                String.valueOf(womensTotals[i]));
                            prepStmt.setString(2, String.valueOf(i));

                            // Execute query
                            prepStmt.executeUpdate();

                            i++;
                        }

                // ****** Score the meet / print mens category ******
                        cStmt = conn.prepareCall
                        ("{call scoreMeetMen}");
                        cStmt.execute();  
                        
                        query = "SELECT place, name, mensTotal from school order by mensTotal desc";
                        
                        // create statement to execute query
                        prepStmt = conn.prepareStatement(query);

                        // Execute query
                        gotResults = prepStmt.execute();

                        // get the result set from query
                        rs = prepStmt.getResultSet();

                        // go to first record in result set
                        rs.first();

                        System.out.print("\nMen's Team Scores");

                        System.out.printf("\n|%-6s|%-8s|%-6s|", "Place", "School", "Points");
                        System.out.println("\n--------------------------");

                        while(gotResults)
                        {
                            int place = rs.getInt("place");
                            String school = rs.getString("name");
                            String point = rs.getString("mensTotal");

                            System.out.printf("|%-6s|%-8s|%-6s|\n", place, school, point);

                            gotResults = rs.next();
                        }


                // ****** Score the meet / print womans category ******
                        cStmt = conn.prepareCall
                        ("{call scoreMeetWoman}");
                        cStmt.execute();  
                        
                        query = "SELECT place, name, womensTotal from school order by womensTotal desc";
                        
                        // create statement to execute query
                        prepStmt = conn.prepareStatement(query);

                        // Execute query
                        gotResults = prepStmt.execute();

                        // get the result set from query
                        rs = prepStmt.getResultSet();

                        // go to first record in result set
                        rs.first();

                        System.out.print("\nWomen's Team Scores");

                        System.out.printf("\n|%-6s|%-8s|%-6s|", "Place", "School", "Points");
                        System.out.println("\n--------------------------");

                        while(gotResults)
                        {
                            int place = rs.getInt("place");
                            String school = rs.getString("name");
                            String point = rs.getString("womensTotal");

                            System.out.printf("|%-6s|%-8s|%-6s|\n", place, school, point);

                            gotResults = rs.next();
                        }

                        break;  
                }
            }
            while(Integer.parseInt(userChoice) != 8);

            conn.close();
        }
        catch (SQLException ex)
        {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			ex.printStackTrace();
		}
	}
}
