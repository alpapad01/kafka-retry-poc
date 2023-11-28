ARG REGISTRY
# ${REGISTRY}/...
FROM registry.access.redhat.com/ubi8/openjdk-17

USER root

ENV APPLICATION=base
ENV JVM_ARGS=  JVM_MEM=  JVM_DEBUG=  LOG_FILE=/app/logs/application.log

RUN mkdir /app && mkdir /app/logs && mkdir /app/configuration
ADD entrypoint.sh /app/entrypoint.sh
ADD run.sh /app/run.sh
ADD logback-*.xml /app/

# 
RUN chown -R 1001:0 /app && chmod -R g=u /app && chmod +x /app/*.sh

WORKDIR /app

USER 1001

ENTRYPOINT ["/app/entrypoint.sh"]