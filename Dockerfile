FROM maven:3.8.5 as build

WORKDIR /app
COPY . .

# we skip tests here to avoid connecting to a non started database service
RUN mvn clean install -DskipTests

FROM openjdk:17

COPY --from=build /app/target/megami-0.0.1-SNAPSHOT.jar /app/megami.jar

CMD ["java","-jar","app/megami.jar"]