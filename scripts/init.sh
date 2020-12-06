docker exec sql bash -c "mysql -uroot -proot < createTable.sql && mysql -uroot -proot < populateTable.sql"

