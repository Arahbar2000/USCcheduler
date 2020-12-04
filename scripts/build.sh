docker stop sql;
docker build -t sql . &&
docker run -d --rm -v $PWD/:/var/lib/mysql-files --network app --name sql -e MYSQL_ROOT_PASSWORD=root sql --secure-file-priv="" &&
echo "Waiting for sql to install..." &&
sleep 45 &&
bash init.sh