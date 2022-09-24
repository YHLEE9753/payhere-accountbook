FROM openjdk:17-jdk-slim-buster
COPY build/libs/accountbook-0.0.1-SNAPSHOT.jar accountbook.jar
EXPOSE 8080
ENTRYPOINT exec java -jar accountbook.jar
