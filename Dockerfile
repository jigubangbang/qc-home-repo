# 빌드 단계 (builder)
# 빌드 환경을 위한 OpenJDK 17 Slim 이미지를 사용합니다.
FROM openjdk:17-jdk-slim AS builder

# 1. 초기 작업 디렉토리 설정 및 전체 소스 복사
# /app 디렉토리를 만들고, Git 리포지토리의 모든 내용 (각 서비스 폴더 포함)을 /app으로 복사합니다.
# 이렇게 하면 community-service, home-service, quest-service 폴더가 /app 아래에 있게 됩니다.
WORKDIR /app
COPY . /app

# 2. --- community-service 빌드 ---
# community-service 디렉토리로 이동합니다.
WORKDIR /app/community-service
# 해당 서비스 디렉토리 내에 있는 mvnw에 실행 권한을 부여합니다.
# mvnw 파일이 해당 폴더에 없거나 권한 문제가 발생하면 빌드 실패 메시지를 출력합니다.
RUN chmod +x ./mvnw || { echo "ERROR: community-service mvnw 스크립트를 찾을 수 없거나 권한이 없습니다. /app/community-service 경로를 확인하세요."; exit 1; }
# 해당 서비스 디렉토리 내에 있는 mvnw를 사용하여 의존성을 미리 다운로드합니다.
RUN ./mvnw dependency:go-offline -B
# 해당 서비스 디렉토리 내에 있는 mvnw를 사용하여 애플리케이션을 빌드합니다 (테스트는 건너뜁니다).
RUN ./mvnw package -DskipTests

# 빌드된 JAR 파일을 /app 디렉토리(빌더 스테이지의 루트)로 복사하고 이름을 명확히 합니다.
# 실제 JAR 파일 이름 패턴 (예: target/community-service-0.0.1-SNAPSHOT.jar)에 맞게 조정하세요.
ARG COMMUNITY_JAR_FILE_NAME=target/*.jar
RUN cp ${COMMUNITY_JAR_FILE_NAME} /app/community-service-bundle.jar || { echo "Community Service JAR 파일이 target 디렉토리에서 발견되지 않았습니다. 빌드 로그를 확인하세요."; exit 1;}


# 3. --- home-service 빌드 ---
# home-service 디렉토리로 이동합니다.
WORKDIR /app/home-service
# 해당 서비스 디렉토리 내에 있는 mvnw에 실행 권한을 부여합니다.
RUN chmod +x ./mvnw || { echo "ERROR: home-service mvnw 스크립트를 찾을 수 없거나 권한이 없습니다. /app/home-service 경로를 확인하세요."; exit 1; }
# 해당 서비스 디렉토리 내에 있는 mvnw를 사용하여 의존성을 미리 다운로드합니다.
RUN ./mvnw dependency:go-offline -B
# 해당 서비스 디렉토리 내에 있는 mvnw를 사용하여 애플리케이션을 빌드합니다 (테스트는 건너뜁니다).
RUN ./mvnw package -DskipTests

# 빌드된 JAR 파일을 /app 디렉토리(빌더 스테이지의 루트)로 복사하고 이름을 명확히 합니다.
ARG HOME_JAR_FILE_NAME=target/*.jar
RUN cp ${HOME_JAR_FILE_NAME} /app/home-service-bundle.jar || { echo "Home Service JAR 파일이 target 디렉토리에서 발견되지 않았습니다. 빌드 로그를 확인하세요."; exit 1;}


# 4. --- quest-service 빌드 ---
# quest-service 디렉토리로 이동합니다.
WORKDIR /app/quest-service
# 해당 서비스 디렉토리 내에 있는 mvnw에 실행 권한을 부여합니다.
RUN chmod +x ./mvnw || { echo "ERROR: quest-service mvnw 스크립트를 찾을 수 없거나 권한이 없습니다. /app/quest-service 경로를 확인하세요."; exit 1; }
# 해당 서비스 디렉토리 내에 있는 mvnw를 사용하여 의존성을 미리 다운로드합니다.
RUN ./mvnw dependency:go-offline -B
# 해당 서비스 디렉토리 내에 있는 mvnw를 사용하여 애플리케이션을 빌드합니다 (테스트는 건너뜁니다).
RUN ./mvnw package -DskipTests

# 빌드된 JAR 파일을 /app 디렉토리(빌더 스테이지의 루트)로 복사하고 이름을 명확히 합니다.
ARG QUEST_JAR_FILE_NAME=target/*.jar
RUN cp ${QUEST_JAR_FILE_NAME} /app/quest-service-bundle.jar || { echo "Quest Service JAR 파일이 target 디렉토리에서 발견되지 않았습니다. 빌드 로그를 확인하세요."; exit 1;}


# 실행 단계 (runtime)
# 애플리케이션 실행을 위한 더 작고 가벼운 OpenJDK 17 Slim 이미지를 사용합니다.
FROM openjdk:17-slim

# 'spring' 사용자를 시스템 사용자로 생성합니다. 이 단계는 ROOT 권한으로 실행되어야 합니다.
RUN useradd --system --uid 1000 spring

# entrypoint.sh 스크립트를 컨테이너 내의 /usr/local/bin 경로로 복사합니다.
# 이 단계는 ROOT 권한으로 실행되어야 합니다.
COPY entrypoint.sh /usr/local/bin/entrypoint.sh
# 복사된 entrypoint.sh 스크립트에 실행 권한을 부여합니다.
# 이 단계는 ROOT 권한으로 실행되어야 합니다.
RUN chmod +x /usr/local/bin/entrypoint.sh

# 이제부터 'spring' 사용자로 전환합니다.
# 이후의 모든 명령은 'spring' 사용자 권한으로 실행됩니다.
USER spring

# 임시 디렉토리 볼륨을 설정합니다. (Spring Boot 애플리케이션에 유용)
VOLUME /tmp

# 각 서비스가 사용하는 포트를 노출합니다. (실제 애플리케이션 포트에 맞게 변경 필요)
EXPOSE 8087
EXPOSE 8088
EXPOSE 8089

# 빌드 단계에서 생성된 각 서비스의 JAR 파일을 런타임 이미지의 /app 디렉토리로 복사합니다.
COPY --from=builder /app/community-service-bundle.jar /app/community-service.jar
COPY --from=builder /app/home-service-bundle.jar /app/home-service.jar
COPY --from=builder /app/quest-service-bundle.jar /app/quest-service.jar

# 애플리케이션 실행 명령어 (컨테이너 진입점)를 정의합니다.
# Docker 컨테이너가 시작될 때 이 스크립트가 실행됩니다.
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]

# Spring 프로파일을 'production'으로 설정하는 환경 변수입니다. (선택 사항, 필요에 따라 변경)
ENV SPRING_PROFILES_ACTIVE=production
