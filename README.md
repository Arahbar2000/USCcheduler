The only dependency you need for this is npm to create the build folder for the frontend. The rest are handled in docker.

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

The application can now be found at http://localhost:8080/home

# Setting the timer

You can set up a `systemd` timer so that the OS would schedule retrieving USC courses and re-populate tables every 30 minutes. The timer is in 
`scripts/timer`, to setup timer you need sudo permission

first change the `WorkingDirectory` of `fetchAPI.service` to the directory of `scripts`, then

```shell
cd scripts/timer
sudo cp * /etc/systemd/system
systemctl daemon-reload
# auto-start after boot
systemctl enable fetchAPI.timer
systemctl start fetchAPI.timer
```