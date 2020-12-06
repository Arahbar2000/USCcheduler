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
first cd into db directory

to get courses.csv, first run python file `python writeData.py --year 20211 output courses.csv` or use `courses.csv`


# RUNNING THE APPLICATION
cd into root directory of repo
```shell script
docker-compose build
docker-compose up
```

This might take a while because we are building a docker image, compiling the backend and then compiling the front end into war files, which are then run on a tomcat server

The application can now be found at http://localhost:8080/home
