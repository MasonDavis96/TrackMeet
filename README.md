# Track Meet
A program that connects to a database (meant to emulate a school of athletes attending a track meet) to allow officials to score athletes and the track meet. Includes options for adding individual athletes, disqualifying athletes, getting results for a particular event, and scoring an event or the entire meet<br><br>


# What I Learned
* Database creation/format (Entities, Attributes, Relationships)<br>
* Stored Procedures<br>
* Triggers<br>
* Database connection through code (Running queries/manipulating data)<br><br>


# Usage
Run Command:<br>

`javac -cp .:mysql-connector-java-5.1.38-bin.jar DavisP4.java`<br>
`java -cp .:mysql-connector-java-5.1.38-bin.jar DavisP4`<br><br>


# Application Example<br>

## Main Menu
```
1. Add a new Athlete
2. Enter a new result
3. Get the results for an event
4. Score an event
5. Disqualify an athlete for one event
6. Disqualify an athlete for the meet
7. Score the meet
8. QUIT
Enter number:
```
<br>



## 1. Add a new Athlete
```
Enter Athlete's first name: Super
Enter Athlete's last name: Man  
Enter Athlete's gender (m or f): m
Enter Athlete's school ID: 1
```
<br>



## 3. Get the results for an event
```
Enter Event's ID Number: 1

|Place |First   |Last     |EventID |Event       |Result |Points |School |Disqualified|
--------------------------------------------------------------------------------------
|1     |Reggie  |Walker   |1       |100m Sprint |3.23   |10     |OSU    |No          |
|1     |Percy   |Williams |1       |100m Sprint |2.32   |10     |UO     |Yes         |
|2     |Bobby   |Morrow   |1       |100m Sprint |4.56   |8      |OIT    |No          |
|3     |Thomas  |Burke    |1       |100m Sprint |5.03   |6      |EOU    |No          |
|4     |Jesse   |Owens    |1       |100m Sprint |6.12   |4      |OIT    |No          |
|5     |Carl    |Lewis    |1       |100m Sprint |6.76   |2      |WOU    |No          |
|6     |Frank   |Jarvis   |1       |100m Sprint |7.56   |1      |EOU    |No          |
|7     |Jim     |Hines    |1       |100m Sprint |9.87   |0      |WOU    |No          |
|7     |Archie  |Hahn     |1       |100m Sprint |9.02   |0      |EOU    |Yes         |

```
<br>



## 7. Score the meet
```
Men's Team Scores
|Place |School  |Points|
--------------------------
|1     |OIT     |12    |
|2     |OSU     |10    |
|3     |EOU     |7     |
|4     |WOU     |2     |
|5     |UO      |0     |

Women's Team Scores
|Place |School  |Points|
--------------------------
|1     |OIT     |10    |
|2     |EOU     |8     |
|3     |OSU     |6     |
|4     |UO      |0     |
|5     |WOU     |0     |

```
<br>
