# Base image được sử dụng để build image
FROM --platform=amd64 openjdk:17.0.2-oraclelinux8

LABEL authors="minhphan"

# Set working directory trong container
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
 
COPY src ./src

# Expose port của ứng dụng
EXPOSE 8080

# Chỉ định command để chạy ứng dụng khi container khởi chạy
CMD ["./mvnw", "spring-boot:run"]