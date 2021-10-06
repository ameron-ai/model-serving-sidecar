FROM openjdk:16-slim-buster

RUN mkdir /app
COPY target/sidecar-1.0-SNAPSHOT.jar /app/app.jar
WORKDIR /app

ENTRYPOINT ["java","-jar","app.jar"]