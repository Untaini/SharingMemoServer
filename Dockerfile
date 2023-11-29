FROM amazoncorretto:17.0.8-alpine as build
ENV APP_HOME=/app
WORKDIR ${APP_HOME}
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle ./gradle
RUN chmod +x gradlew
RUN apk add dos2unix
RUN dos2unix ./gradlew
RUN ./gradlew build || return 0
COPY src ./src
RUN ./gradlew clean build

FROM amazoncorretto:17.0.8-alpine
ENV APP_HOME=/app
WORKDIR ${APP_HOME}
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]