in=$( echo $1 | tr [:upper:] [:lower:])
if [ $in = "f" ]
then
    docker run -v $PWD/FE:/frontend maven:3.6.3-jdk-11 \
    mvn clean package -f /frontend/pom.xml &&
    cp ./FE/target/*.war wars/
elif [ $in = "b" ]
then
    docker run -v $PWD/FinalProj201:/backend maven:3.6.3-jdk-11 \
    mvn clean package -f /backend/pom.xml && 
    cp ./FinalProj201/target/*.war wars/
    cp ./FinalProj201/target/*.war /usr/local/Cellar/tomcat/9.0.40/libexec/webapps/
    
else
    echo "please enter either \"f\" or \"b\""
fi