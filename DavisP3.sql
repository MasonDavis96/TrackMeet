--  Name: Mason Davis
--  CS 330
--	Assignment 3
--	2-24-2020

use databaseName

select 'Dropping all tables:' as 
    '=============================';
set foreign_key_checks = 0;
drop table if exists school, athlete, event, results;
set foreign_key_checks = 1;
drop procedure if exists disqualifyAthlete;
drop procedure if exists scoreEventTrack;
drop procedure if exists scoreEventField;
drop procedure if exists assignPlaceTrack;
drop procedure if exists assignPlaceField;
drop procedure if exists disqualifyAllEvents;
drop procedure if exists scoreMeetMen;
drop procedure if exists scoreMeetWoman;
drop trigger if exists moreThanFour;


select 'create table school:' as 
    '=============================';
create table school (
    place int not null default 0,
    ID int not null auto_increment,
    name varchar(50),
    mensTotal int default 0,
    womensTotal int default 0,
    primary key (ID)
    );

insert into school (name)
values ("EOU"),
        ("OSU"),
        ("UO"),
        ("OIT"),
        ("WOU");


select 'create table athlete:' as 
    '=============================';
create table athlete (
    athleteID int not null auto_increment,
    firstName varchar(20) not null,
    lastName varchar(20) not null,
    gender enum('M', 'F') not null,
    schoolID int not null,
    disqualified enum('Yes', 'No') default 'No',
    constraint nameAndSchool unique(firstName, lastName, schoolID),
    primary key (athleteID),
    foreign key (schoolID) references school(ID)
    );

insert into athlete (firstName, lastName, gender,schoolID)
values ("Thomas", "Burke", "M", 1),
        ("Frank", "Jarvis", "M", 1),
        ("Archie", "Hahn", "M", 1),
        ("Reggie", "Walker", "M", 2),
        ("Ralph", "Craig", "M", 2),
        ("Charley", "Paddock", "M", 2),
        ("Harold", "Abrahams", "M", 3),
        ("Percy", "Williams", "M", 3),
        ("Eddie", "Tolan", "M", 3),
        ("Jesse", "Owens", "M", 4),
        ("Harrison", "Dillard", "M", 4),
        ("Bobby", "Morrow", "M", 4),
        ("Jim", "Hines", "M", 5),
        ("Allan", "Wells", "M", 5),
        ("Carl", "Lewis", "M", 5),
        ("Betty", "Robinson", "F", 1),
        ("Helen", "Stephens", "F", 1),
        ("Wilma", "Rudolph", "F", 1),
        ("Evelyn", "Ashford", "F", 2),
        ("Florence", "Griffith", "F", 2),
        ("Edith", "McGuire", "F", 2),
        ("Irena", "Eckert", "F", 3),
        ("Gwen", "Torrence", "F", 3),
        ("Veronica", "Campbell", "F", 3),
        ("Allyson", "Felix", "F", 4),
        ("Elaine", "Thompson", "F", 4),
        ("Betty", "Robinson", "F", 4),
        ("Cathy", "Freeman", "F", 5),
        ("Christine", "Hooks", "F", 5),
        ("Sanya", "Ross", "F", 5);


select 'create table event:' as 
    '=============================';
create table event (
    gender enum('M', 'F') not null,
    eventType enum('track', 'field') not null,
    eventName varchar(50) not null,
    eventID int not null unique,
    primary key(eventID)
    );

insert into event
values ("M", "Track", "100m Sprint", 1),
        ("F", "Track", "100m Sprint", 2),
        ("M", "Field", "Long Jump", 3),
        ("F", "Field", "Long Jump", 4),
        ("M", "Track", "100m Hurdle", 5),
        ("F", "Track", "100m Hurdle", 6),
        ("M", "Field", "Shot Put", 7),
        ("F", "Field", "Shot Put", 8),
        ("M", "Track", "200m Sprint", 9),
        ("F", "Track", "200m Sprint", 10),
        ("M", "Field", "Javelin", 11),
        ("F", "Field", "Javelin", 12);


select 'create table results:' as 
    '=============================';
create table results (
    athleteID int not null,
    eventID int not null,
    mark float not null,
    points int not null default 0,
    place int not null default 0,
    disqualified enum('Yes', 'No') default 'No',
    primary key (athleteID, eventID),
    foreign key (athleteID) references athlete(athleteID),
    foreign key (eventID) references event(eventID)
    );

insert into results (athleteID, eventID, mark)
values (1, 1, 5.03),
        (1, 3, 15.23),
        (1, 5, 3.43),
        (1, 7, 34.54),
        (2, 9, 4.10),
        (3, 11, 53.33),
        (4, 1, 3.23),
        (5, 3, 65.63),
        (6, 5, 2.32),
        (7, 7, 65.87),
        (8, 9, 1.03),
        (9, 11, 100.93),
        (10, 1, 6.12),
        (11, 3, 43.73),
        (12, 5, 6.34),
        (13, 7, 66.23),
        (14, 9, 6.04),
        (15, 11, 22.22),
        (16, 2, 6.55),
        (17, 4, 77.03),
        (18, 6, 4.93),
        (19, 8, 49.02),
        (20, 10, 5.01),
        (21, 2, 76.34),
        (22, 4, 2.00),
        (23, 6, 32.03),
        (24, 8, 7.43),
        (25, 10, 76.94),
        (26, 2, 1.00),
        (27, 4, 88.45),
        (28, 6, 3.30),
        (29, 8, 101.05),
        (30, 10, 5.05);


delimiter //
select 'create trigger:' as 
    '============================='//
create trigger moreThanFour
after insert on results
for each row
begin
    if(select count(athleteID) from results 
        where new.athleteID = athleteID) > 4 then
        call disqualifyAthlete(new.athleteID);
    end if;
end //
delimiter ;


delimiter //
select 'create stored procedures:' as
     '============================='//
create procedure disqualifyAthlete(ID int)
begin
    update athlete set disqualified = 'Yes' 
    where athleteID = ID;
end //
delimiter ;


delimiter //
create procedure disqualifyAllEvents(ID int)
begin
    if (select disqualified from athlete where athleteID = ID) = "Yes" then
        update results set disqualified = "Yes" where athleteID = ID;
    end if;
end //
delimiter ;


delimiter //
create procedure assignPlaceTrack(event int)
begin
    declare aId int;
    declare i int;
    declare c int;
    set c = 0;
    set i = 1;

    while i <= (select count(athleteID) from results where eventID = event) DO
        set aId = (select athleteID from results where EventId = event order by mark asc limit c,1);
        update results set place = i where eventID = event and athleteID = aID;

        set i = i + 1;

        if ((select disqualified from results where athleteID = aId and eventID = event order by mark asc limit 1) = 'Yes') then 
            set i = i - 1; end if;

        set c = c + 1;
        
    end while;
end //
delimiter ;


delimiter //
create procedure assignPlaceField(event int)
begin
    declare aId int;
    declare i int;
    declare c int;
    set c = 0;
    set i = 1;

    while i <= (select count(athleteID) from results where eventID = event) DO
        set aId = (select athleteID from results where EventId = event order by mark desc limit c,1);
        update results set place = i where eventID = event and athleteID = aID;
        
        set i = i + 1;

        if ((select disqualified from results where athleteID = aId and eventID = event order by mark desc limit 1) = 'Yes') then 
            set i = i - 1; end if;
        
        set c = c + 1;

    end while;
end //
delimiter ;


delimiter //
create procedure scoreEventTrack(event int)
begin
    declare aId int;
    declare i int;
    declare p int;
    declare c int;
    set c = 0;
    set i = 1;

    while i <= 6 DO
        set aId = (select athleteID from results where EventId = event order by mark asc limit c,1);
        if i = 1 then set p = 10; end if;
        if i = 2 then set p = 8; end if;
        if i = 3 then set p = 6; end if;
        if i = 4 then set p = 4; end if;
        if i = 5 then set p = 2; end if;
        if i = 6 then set p = 1; end if;
        if i > 6 then set p = 0; end if;

        update results set points = p where eventID = event and athleteID = aId;
        set c = c + 1;
        set i = i + 1;

        if ((select disqualified from results where athleteID = aId and eventID = event order by mark asc limit 1) = 'Yes') then 
            set i = i - 1; end if;

    end while;
end //
delimiter ;


delimiter //
create procedure scoreEventField(event int)
begin
    declare aId int;
    declare i int;
    declare p int;
    declare c int;
    set c = 0;
    set i = 1;

    while i <= 6 DO
        set aId = (select athleteID from results where EventId = event order by mark desc limit c,1);
        if i = 1 then set p = 10; end if;
        if i = 2 then set p = 8; end if;
        if i = 3 then set p = 6; end if;
        if i = 4 then set p = 4; end if;
        if i = 5 then set p = 2; end if;
        if i = 6 then set p = 1; end if;
        if i > 6 then set p = 0; end if;

        update results set points = p where eventID = event and athleteID = aId;
        set c = c + 1;
        set i = i + 1;

        if ((select disqualified from results where athleteID = aId and eventID = event order by mark desc limit 1) = 'Yes') then 
            set i = i - 1; end if;

    end while;
end //
delimiter ;


delimiter //
create procedure scoreMeetMen()
begin
    declare sId int;
    declare i int;
    declare c int;
    set c = 0;
    set i = 1;

    while i <= (select count(*) from school) DO
        set sId = (select ID from school order by mensTotal desc limit c,1);
        update school set place = i where ID = sId;
        set c = c + 1;
        set i = i + 1;
    end while;
end //
delimiter ;


delimiter //
create procedure scoreMeetWoman()
begin
    declare sId int;
    declare i int;
    declare c int;
    set c = 0;
    set i = 1;

    while i <= (select count(*) from school) DO
        set sId = (select ID from school order by womensTotal desc limit c,1);
        update school set place = i where ID = sId;
        set c = c + 1;
        set i = i + 1;
    end while;
end //
delimiter ;
