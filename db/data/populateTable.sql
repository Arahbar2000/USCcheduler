use cs201;
LOAD DATA INFILE '/docker-entrypoint-initdb.d/courses.csv' IGNORE
INTO TABLE Course
FIELDS TERMINATED BY '|'
LINES TERMINATED BY '\n';
