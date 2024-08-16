FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR app
COPY pom.xml pom.xml
COPY src src

RUN --mount=type=bind,source=./settings.xml,target=/app/settings.xml \
    mvn -s ./settings.xml clean package

# Create a Java runtime
FROM eclipse-temurin:21 AS jre-build

RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.logging,java.net.http \
         --strip-debug \
         --strip-java-debug-attributes \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

#FROM debian:stable-slim
FROM bitnami/minideb:bullseye

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME
WORKDIR app
COPY --from=builder /app/target/chronicles-embeddings-*-SNAPSHOT.jar chronicles-embeddings.jar

ENTRYPOINT [ "java", "-jar", "chronicles-embeddings.jar" ]
#CMD ["sleep", "60m"]



