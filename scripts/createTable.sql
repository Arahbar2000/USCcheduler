drop database if exists cs201;
create database cs201;
use cs201;

drop table if exists Users;
create table Users
(
	userId int auto_increment,
	lastName varchar(50) not null,
	firstName varchar(50) not null,
	email varchar(50) not null,
	password varchar(50) not null,
	constraint Users_pk
		primary key (userId)
);


drop table if exists Course;
create table Course
(
	courseId int not null auto_increment,
	department VARCHAR(50) not null,
	courseNumber int not null,
    title varchar(50) not null,
	startTime varchar(50) not null,
	endTime varchar(50) not null,
	section varchar(50) not null,
	instructor varchar(50) not null,
	units int not null,
	daysOfWeek varchar(50) not null comment 'use string like "MW"',
    spots varchar(50) not null comment 'use string like "3/25"',
	constraint Course_pk
		primary key (courseId)
);


drop table if exists Friends;
create table Friends
(
	userId int not null,
	friendId int not null,
	constraint Friends_pk
		primary key (userId, friendId),
	constraint Friends_Users_userId_fk
		foreign key (userId) references Users (userId),
    constraint Friends_Users_friendId_fk
        foreign key (friendId) references Users (userId)
);


drop table if exists Schedule;
create table Schedule
(
    userId int not null,
    department varchar(50) not null,
	courseNumber int not null,
	constraint Schedule_pk
		primary key (userId, department, courseNumber),
	constraint Schedule_Users_userId_fk
		foreign key (userId) references Users (userId)
);


drop table if exists Preferences;
create table Preferences
(
	userId int not null,
	courseName varchar(50) not null comment 'CS201,CS270',
	startTime varchar(50) comment 'if not specified TBA',
	endTime varchar(50) comment 'if not specified TBA',
    extraCurriculum varchar(50) comment '[13:00 14:00],[15:00 17:00]',
    desiredUnits int,

	constraint Takes_pk
		primary key (userId, courseName,extraCurriculum),
	constraint Takes_Users_userId_fk
		foreign key (userId) references Users (userId)
);


