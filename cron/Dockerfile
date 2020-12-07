FROM ubuntu:latest

WORKDIR /app

# Copy update_db_cron file to the cron.d directory
COPY update_db_cron /etc/cron.d/update_db_cron

# Copy files to app working directory
COPY populateTable.sql writeData.py repopulate.sh wait-for-it.sh /app/

# sets up chron and python
RUN apt-get update && \ 
    apt-get -y install mysql-client cron python3 python3-pip && \
    pip3 install requests && \
    chmod 0644 /etc/cron.d/update_db_cron && \
    chmod 0744 /app/repopulate.sh /app/wait-for-it.sh && \
    crontab /etc/cron.d/update_db_cron && \
    touch /var/log/cron.log

# Run the command on container startup
CMD cron && /app/repopulate.sh && tail -f /var/log/cron.log