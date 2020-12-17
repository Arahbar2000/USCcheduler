FROM mysql:latest
COPY my.cnf /etc/mysql/conf.d/my.cnf
ADD createTable.sql /docker-entrypoint-initdb.d
RUN mkdir /var/lib/mysql-files
EXPOSE 3306