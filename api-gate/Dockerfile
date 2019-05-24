FROM openjdk:13-alpine

LABEL maintainer="Yanzhe Lee"

WORKDIR /usr/src/app/

RUN apk add curl \
    && curl -L -o api-gate.jar $(curl -s https://api.github.com/repos/YanzheL/llzw/releases | grep browser_download_url | head -n 1 | cut -d '"' -f 4)

EXPOSE 8981

CMD [ "java", "-server", "-XX:+HeapDumpOnOutOfMemoryError", "-Xmx500m", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseZGC", "-Dspring.profiles.active=test", "-jar",  "api-gate.jar" ]
