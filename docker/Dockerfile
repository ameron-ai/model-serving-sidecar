FROM openjdk:11-slim-buster

RUN mkdir /app
COPY target/sidecar-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app

ENTRYPOINT ["java","-jar","app.jar"]