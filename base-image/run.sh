#!/bin/bash
unset SPRING_PROFILES
export
exec ${JAVA_HOME}/bin/java -server ${JVM_ARGS} ${JVM_MEM} ${JVM_DEBUG} \
     -Djava.security.egd=file:/dev/./urandom \
     -XX:+ExitOnOutOfMemoryError \
     -XX:+CrashOnOutOfMemoryError \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=20 \
     -XX:InitiatingHeapOccupancyPercent=35 \
     -XX:+ExplicitGCInvokesConcurrent \
     -XX:MaxInlineLevel=15 \
     -Djava.awt.headless=true \
     -Djava.io.tmpdir=/tmp \
     -Dlogging.config=${LOGBACK_CONFIG} \
     -jar /app/app.jar --spring.profiles.active=${APP_PROFILES}