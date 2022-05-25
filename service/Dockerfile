FROM openjdk:8-jdk-slim

ENV TZ=Europe/Berlin
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update

RUN apt-get install -y curl

RUN groupadd ptstgroup
RUN useradd -G ptstgroup ptstuser
USER ptstuser:ptstgroup

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

#ENTRYPOINT ["java","-jar","/app.jar", "--spring.config.location=file:/application.properties"]

# use environment variables
ENTRYPOINT ["java","-jar","/app.jar"]
