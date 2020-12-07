echo "getting courses" && \ 
python3 /app/writeData.py --year 20211 --output /app/courses.csv && \
/app/wait-for-it.sh db:3306 -s -t 60 -- echo "repopulating database" && \
mysql -uroot -proot -hdb --local-infile=1 < /app/populateTable.sql && \
echo "finished repopulating database";
