FROM maven:3-jdk-14 AS builder

WORKDIR /usr/src/app

COPY . .

RUN mvn package -B -DskipTests -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn

FROM openjdk:11-jre-slim

LABEL maintainer="Yanzhe Lee"

WORKDIR /usr/src/app/

COPY --from=builder /usr/src/app/target/*.jar ./api-gate.jar

EXPOSE 8981

CMD [ "java", "-server", "-XX:+HeapDumpOnOutOfMemoryError", "-Xmx500m", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseZGC", "-Dspring.profiles.active=test", "-jar", "api-gate.jar" ]
