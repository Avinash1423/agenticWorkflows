FROM maven:3.9.6-openjdk-23  AS BUILD
COPY  . .
RUN mvn clean package -DskipTests

FROM openjdk:23-jdk
COPY --from=build target/Agents-0.0.1-SNAPSHOT.jar Agent.jar
EXPOSE 8080
ENTRYPOINT["java","-jar","Agent.jar"]



