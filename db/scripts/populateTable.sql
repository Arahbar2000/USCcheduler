use scheduler;
LOAD DATA INFILE '/var/lib/mysql-files/courses.csv' IGNORE
INTO TABLE Course
FIELDS TERMINATED BY '|'
LINES TERMINATED BY '\n';