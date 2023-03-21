FROM maven:3.8.7-eclipse-temurin-19
COPY target/svc-challenge.jar svc-challenge.jar
ENTRYPOINT ["java","-jar","/svc-challenge.jar"]