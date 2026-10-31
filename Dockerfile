# ============================================================
#  Etapa 1: compilar con Maven (usa el wrapper mvnw)
# ============================================================
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY src ./src

RUN chmod +x mvnw && ./mvnw -q clean package -DskipTests

# ============================================================
#  Etapa 2: imagen final ligera (solo JRE + el .jar)
# ============================================================
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
