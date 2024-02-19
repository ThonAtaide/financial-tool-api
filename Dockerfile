FROM gradle:8-jdk17-alpine as build
WORKDIR /app
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts /app/
COPY --chown=gradle:gradle src /app/src
RUN gradle --no-daemon build

FROM amazoncorretto:17.0.8-alpine3.18 as jdk
ENV SERVER_PORT 8080

EXPOSE $SERVER_PORT

COPY --from=build /app/build/libs/*jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]