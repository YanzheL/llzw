FROM maven:3-jdk-14 AS builder

WORKDIR /usr/src/app

COPY pom.xml .

RUN mvn dependency:go-offline -B -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn

COPY . .

RUN mvn package -B -DskipTests -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn

FROM openjdk:15-jdk-slim

LABEL maintainer="Yanzhe Lee"

WORKDIR /usr/src/app/

COPY --from=builder /usr/src/app/target/*.jar ./app.jar

EXPOSE 8981

CMD [ "java", "-server", "-XX:+HeapDumpOnOutOfMemoryError", "-Xmx500m", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseZGC", "-Dspring.profiles.active=test", "-jar", "app.jar" ]
