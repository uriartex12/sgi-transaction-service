FROM openjdk:17
COPY target/transaction-service-0.0.1-SNAPSHOT.jar transaction-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/transaction-service.jar"]
