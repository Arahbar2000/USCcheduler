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

# React

```shell script
cd FE
mvn clean package
cp ./target/*.war <tomcat_home>/webapps/;
<tomcat_home>/bin/catalina.sh start;
```

then react would be served on `http://localhost:8080/home`

# SQL

To create table 

```shell script

mysql -u <user> -p < createTable.sql

```

To populate table

first run python file `python writeData.py --year 20211 output
courses.csv` or use `courses.csv` then

```shell script

mysql -u <user> -p < populateTable.sql

```
