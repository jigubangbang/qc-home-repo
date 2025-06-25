# 빌드 단계 (builder)
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app
COPY . /app
RUN chmod +x mvnw

# --- community-service 빌드 ---
WORKDIR /app/community-service
RUN ../mvnw dependency:go-offline -B
RUN ../mvnw package -DskipTests
ARG COMMUNITY_JAR_FILE_NAME=target/*.jar
RUN cp ${COMMUNITY_JAR_FILE_NAME} /app/community-service.jar || { echo "Community Service JAR not found"; exit 1;}

# --- home-service 빌드 ---
WORKDIR /app/home-service
RUN ../mvnw dependency:go-offline -B
RUN ../mvnw package -DskipTests
ARG HOME_JAR_FILE_NAME=target/*.jar
RUN cp ${HOME_JAR_FILE_NAME} /app/home-service.jar || { echo "Home Service JAR not found"; exit 1;}

# --- quest-service 빌드 ---
WORKDIR /app/quest-service
RUN ../mvnw dependency:go-offline -B
RUN ../mvnw package -DskipTests
ARG QUEST_JAR_FILE_NAME=target/*.jar
RUN cp ${QUEST_JAR_FILE_NAME} /app/quest-service.jar || { echo "Quest Service JAR not found"; exit 1;}


# 실행 단계 (runtime)
FROM openjdk:17-slim
RUN useradd --system --uid 1000 spring
USER spring
VOLUME /tmp

EXPOSE 8087
EXPOSE 8088
EXPOSE 8089

COPY --from=builder /app/community-service.jar /app/community-service.jar
COPY --from=builder /app/home-service.jar /app/home-service.jar
COPY --from=builder /app/quest-service.jar /app/quest-service.jar

COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
ENV SPRING_PROFILES_ACTIVE=production