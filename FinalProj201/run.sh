mvn clean package;
cp ./target/*.war $TOMCAT_HOME/webapps/;
$TOMCAT_HOME/bin/catalina.sh start;
