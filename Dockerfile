# ==============================================
# Stage 1: Build nativo con GraalVM
# ==============================================
FROM ghcr.io/graalvm/native-image:java21-23.3.2 AS build

WORKDIR /workspace

# Copiamos gradle y settings
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .

# Copiamos código fuente
COPY src ./src

# Permisos para gradlew
RUN chmod +x ./gradlew

# Build nativo (omite tests y checkstyle para RPi)
RUN ./gradlew clean nativeCompile -x test -x checkstyleMain -x checkstyleTest --no-daemon

# ==============================================
# Stage 2: Imagen ligera de producción
# ==============================================
FROM alpine:latest

# Directorio de trabajo
WORKDIR /app

# Copiamos el binario nativo desde el stage de build
COPY --from=build /workspace/build/native/nativeCompile/expenses-api /app/expenses-api

# Exponemos puerto de la app
EXPOSE 8081

# Variables de entorno para la app (MySQL, etc.)
ARG DB_USERNAME
ARG DB_PASSWORD
ENV DB_USERNAME=${DB_USERNAME} \
    DB_PASSWORD=${DB_PASSWORD}

# Usuario no root
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Comando de ejecución
ENTRYPOINT ["/app/expenses-api"]
