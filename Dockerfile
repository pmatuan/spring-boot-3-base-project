FROM curlimages/curl:8.2.1 AS download
ARG OTEL_AGENT_VERSION="1.32.0"
RUN curl --silent --fail -L "https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar" \
    -o "$HOME/opentelemetry-javaagent.jar"

FROM --platform=$BUILDPLATFORM gradle:7.6.3-jdk17 AS build
WORKDIR /workspace

COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon -x test

FROM --platform=$TARGETPLATFORM eclipse-temurin:17-jre
WORKDIR /workspace
COPY --from=build /workspace/api/build/libs/*.jar ./api.jar
COPY --from=build /workspace/consumer/build/libs/*.jar ./consumer.jar
COPY --from=build /workspace/migration/build/libs/*.jar ./migration.jar
COPY --from=download /home/curl_user/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar