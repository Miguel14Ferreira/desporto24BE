FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /workspace
COPY . .
RUN chmod +x mvnw && ./mvnw -q -DskipTests package

FROM eclipse-temurin:21-jre-jammy

ENV CONTAINER_PORT=8080

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system desporto24 \
    && useradd --system --gid desporto24 --home /app --shell /usr/sbin/nologin desporto24 \
    && mkdir -p /app/data/media \
    && chown -R desporto24:desporto24 /app

WORKDIR /app
COPY --from=build --chown=desporto24:desporto24 /workspace/target/*.jar app.jar

USER desporto24

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=5 \
  CMD curl --fail --silent "http://127.0.0.1:${PORT:-${CONTAINER_PORT:-8080}}/actuator/health" >/dev/null || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
