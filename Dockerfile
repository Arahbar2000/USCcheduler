# FROM node:12 as reactBuild
# WORKDIR /app
# COPY FE/src /app/src
# COPY FE/public /app/public
# COPY FE/package.json /app/package.json
# RUN npm install && npm run build


FROM maven:3.6.3-jdk-11 AS warBuilder
WORKDIR /app
COPY FinalProj201/src /app/backend/src
COPY FinalProj201/pom.xml /app/backend/pom.xml
# COPY --from=reactBuild /app/build /app/frontend/build
COPY FE/build /app/frontend/build
COPY FE/pom.xml /app/frontend/pom.xml
COPY FE/web.xml /app/frontend/web.xml

RUN mvn clean package -f /app/backend/pom.xml \
    && mvn clean package -f /app/frontend/pom.xml

FROM tomcat:9.0

COPY --from=warBuilder /app/backend/target/*.war \
    /usr/local/tomcat/webapps/
COPY --from=warBuilder /app/frontend/target/*.war \
    /usr/local/tomcat/webapps/
COPY *.jar /usr/local/tomcat/lib/

EXPOSE 8080

CMD ["catalina.sh", "run"]