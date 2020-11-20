# Tomcat

Remember to change:

1. `FinalProj201/web/META-INF/context.xml`: username and password

2. `FinalProj201/src/main/JDBCCredential`: username and password

To start the project

```shell script

cd FinalProje201
mvn clean package
cp ./target/*.war <tomcat-home>/webapps/
<tomcat-home>/bin/catalina.sh start

```

Now the tomcat starts on port 8080 and visit `http://localhost:8080/cs201` to visit our project.

# React

```shell script
cd FE
npm i
npm run start
```

then react would be start on port 3000.

# SQL

To create table 

```sql

mysql -u <user> -p < createTable.sql

```

To populate table

first run python file `python writeData.py --year 20211 output
courses.csv` or use `courses.csv` then

```sql

mysql -u <user> -p < populateTable.sql

```
