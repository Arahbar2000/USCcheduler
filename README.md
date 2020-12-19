You will need npm to build the frontend and docker to create the wars

Please execute these sections in order

# React

Make sure you run this command first because you run the entire application, as the dockerfile assumes the 'build' folder already exists
```shell script
cd FE
npm i
npm run build
```

# Building the wars
Build the frontend war file
```shell script
bash build.sh f
```
Build the backend war file
```shell script
bash build.sh b
```
Your war files are now find in the "wars" directory

# LINUX MACHINE DEPLOYMENT

It is assumed that you already have installed tomcat and mysql in your linux machine

Make sure to change the url, username, and password in your context.xml to connect to your mysql database

ssh into the linux machine

To set automatic database refreshes using systemd, set present directory to db/scripts/timer, and then run these commands:
```
sudo cp * /etc/systemd/system
systemctl daemon-reload
# auto-start after boot
systemctl enable fetchAPI.timer
systemctl start fetchAPI.timer
```

Place the mysql connector jar file in tomcat/lib directory
```
cp *.jar <tomcat>/lib/
```

Copy the context.xml to /tomcat/conf/Catalina/localhost/context.xml.default
```
cp *.xml <tomcat>/conf/Catalina/localhost/context.xml.default
```

restart tomcat

go to http://{public ipv4 address of machine}:8080/manager/html to deploy the two wars in tomcat
