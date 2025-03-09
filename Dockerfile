FROM gradle:8.13-jdk23 AS build
WORKDIR /app

COPY gradle gradle
COPY gradlew settings.gradle.kts build.gradle.kts ./

RUN chmod +x ./gradlew

RUN ./gradlew dependencies --no-daemon

COPY src ./src
RUN ./gradlew build -x test --no-daemon

##  [ Etapa de Runtime ]

FROM eclipse-temurin:23-jdk AS runtime
WORKDIR /app

COPY --from=build /app/build/libs/shotit-1.0.jar shotit.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "shotit.jar"]
