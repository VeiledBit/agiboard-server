FROM maven:3.8.5-openjdk-17-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -DskipTests -f /home/app/pom.xml clean package

FROM openjdk:17
ARG JAR_FILE=*.jar
COPY --from=build /home/app/target/${JAR_FILE} /usr/local/lib/application.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/application.jar"]
