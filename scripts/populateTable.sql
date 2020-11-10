use cs201;
LOAD DATA LOCAL INFILE '/home/chris/data/untitled/java/myCourse/CSCI201/Backend/scripts/courses.csv'
INTO TABLE Course
FIELDS TERMINATED BY '|'
LINES TERMINATED BY '\n';
# (department, courseNumber,title,startTime,endTime,section,instructor,units,daysOfWeek);
