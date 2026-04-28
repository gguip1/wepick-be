FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY gradle ./gradle
COPY gradlew build.gradle settings.gradle ./
COPY src ./src

RUN chmod +x ./gradlew && ./gradlew build --no-daemon -x test

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "build/libs/community-0.0.1-SNAPSHOT.jar"]
