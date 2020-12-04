docker exec -it sql bash -c "mysql -uroot -proot < createTable.sql && mysql -uroot -proot < populateTable.sql"

