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
