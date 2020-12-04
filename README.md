# Tomcat

After you clone this project, please remember to change the following files:

1. `FinalProj201/web/META-INF/context.xml`: username and password

2. `FinalProj201/src/main/JDBCCredential`: username and password

These files contain the information of the database.

To start the project

```shell script
cd FinalProje201
mvn clean package
cp ./target/*.war <tomcat-home>/webapps/
<tomcat-home>/bin/catalina.sh start
```

Now the tomcat starts on port 8080 and visit `http://localhost:8080/cs201` to visit our project.

Note that if you deploy the project through IDE and you experience the CORS issue, then it is probably because your IDE is deployed in IDE's cache folder but not `<tomcat-home>/webapps/` which can cause trouble when using CORS. Please make sure that you are deploying to `<tomcat-home>/webapps/`, you can  deploy the war file manually in `http://localhost:8080/manager/html`.

Please execute these sections in order

# React

Make sure you run this command first because you run the entire application, as the dockerfile assumes the 'build' folder already exists
```shell script
cd FE
npm i
npm run build
```

# SQL
first cd into scripts directory

to get courses.csv, first run python file `python writeData.py --year 20211 output courses.csv` or use `courses.csv`

To build image, run container, and popualate database:

```shell script

bash build.sh

```

To populate table based on already running container

first run python file `python writeData.py --year 20211 output
courses.csv` or use `courses.csv` then

```shell script

bash init.sh

```

Note that you only have to run build.sh once, after that the container will always be running and you can simply call init.sh to repopulateDatabase based on courses.csv

# RUNNING THE APPLICATION
cd into root directory of repo
```shell script
bash run.sh
```

This might take a while because we are building a docker image, compiling the backend and then compiling the front end into war files, which are then run on a tomcat server
