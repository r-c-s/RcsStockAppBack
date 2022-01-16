FROM openjdk:17-oracle
WORKDIR /talkdesk
COPY target/StockApp-1.0-SNAPSHOT.jar StockApp-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "StockApp-1.0-SNAPSHOT.jar"]