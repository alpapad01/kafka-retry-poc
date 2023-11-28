ARG PROJECT_VERSION 

FROM poc/base-image:${PROJECT_VERSION}

ARG jarFileName
COPY ${jarFileName} /app/app.jar

ENV JVM_MEM=-Xmx512M
ENV KAFKA_HOST=kafka-0
ENV KAFKA_PORT=9092

RUN mkdir /app/mta

VOLUME ["/app/mta/"]