FROM tomee:latest

RUN curl -L -o ./lib/mysql-connector-java-8.0.29.jar https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.29/mysql-connector-java-8.0.29.jar

COPY ./tomee.xml ./conf

COPY ./target/schoolbell-1.0-SNAPSHOT.war ./webapps/schoolbell.war

CMD ["catalina.sh", "run"]

EXPOSE 8080
