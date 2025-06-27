# 빌드 단계 (builder)
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app
COPY . /app


# 2. --- com-service 빌드 ---
WORKDIR /app/com-service
RUN chmod +x ./mvnw || { echo "ERROR: com-service mvnw 스크립트를 찾을 수 없거나 권한이 없습니다. /app/com-service 경로를 확인하세요."; exit 1; } # <-- 오류 메시지도 'com-service'로 수정했습니다.
RUN ./mvnw dependency:go-offline -B
RUN ./mvnw package -DskipTests
ARG COM_JAR_FILE_NAME=target/*.jar
RUN cp ${COM_JAR_FILE_NAME} /app/com-service-bundle.jar || { echo "Com Service JAR 파일이 target 디렉토리에서 발견되지 않았습니다. 빌드 로그를 확인하세요."; exit 1;}


# 3. --- home-service 빌드 ---
WORKDIR /app/home-service
RUN chmod +x ./mvnw || { echo "ERROR: home-service mvnw 스크립트를 찾을 수 없거나 권한이 없습니다. /app/home-service 경로를 확인하세요."; exit 1; }
RUN ./mvnw dependency:go-offline -B
RUN ./mvnw package -DskipTests
ARG HOME_JAR_FILE_NAME=target/*.jar
RUN cp ${HOME_JAR_FILE_NAME} /app/home-service-bundle.jar || { echo "Home Service JAR 파일이 target 디렉토리에서 발견되지 않았습니다. 빌드 로그를 확인하세요."; exit 1;}


# 4. --- quest-service 빌드 ---
WORKDIR /app/quest-service
RUN chmod +x ./mvnw || { echo "ERROR: quest-service mvnw 스크립트를 찾을 수 없거나 권한이 없습니다. /app/quest-service 경로를 확인하세요."; exit 1; }
RUN ./mvnw dependency:go-offline -B
RUN ./mvnw package -DskipTests
ARG QUEST_JAR_FILE_NAME=target/*.jar
RUN cp ${QUEST_JAR_FILE_NAME} /app/quest-service-bundle.jar || { echo "Quest Service JAR 파일이 target 디렉토리에서 발견되지 않았습니다. 빌드 로그를 확인하세요."; exit 1;}


# 실행 단계 (runtime)
FROM openjdk:17-slim
RUN useradd --system --uid 1000 spring
COPY entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh
USER spring
VOLUME /tmp
EXPOSE 8087
EXPOSE 8088
EXPOSE 8089
COPY --from=builder /app/com-service-bundle.jar /app/com-service.jar
COPY --from=builder /app/home-service-bundle.jar /app/home-service.jar
COPY --from=builder /app/quest-service-bundle.jar /app/quest-service.jar
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
ENV SPRING_PROFILES_ACTIVE=production
