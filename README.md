# Untitled

# Load Balancer Demo Setup Guide

## Introduction

This guide will help you set up a demo environment using Docker Compose with MySQL, Spring Boot, and Nginx to simulate a load balancer. We will create a Spring Boot application with 3 replicas and use Nginx to distribute traffic to these replicas.

## Prerequisites

- Docker
- Docker Compose

## Setup Instructions

### 1. Download the Source Code

Download the source code or create a new directory and set up the required files.

### 2. Create `docker-compose.yml`

Create a file named `docker-compose.yml` and paste the following code into it:

```yaml
version: '3'

services:
  mysql:
    image: mysql:latest
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: 123
    ports:
      - "3306:3306"
    networks:
      - app-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 20s
      retries: 10

  spring-app:
    build:
      context: .
      dockerfile: Dockerfile
    depends_on:
      - mysql
    restart: always
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/db-example?createDatabaseIfNotExist=true
      - SPRING_DATASOURCE_USERNAME=root
      - SPRING_DATASOURCE_PASSWORD=123
    deploy:
      mode: replicated
      replicas: 3
    networks:
      - app-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080"]
      interval: 30s
      timeout: 10s
      retries: 5

  nginx:
    image: nginx:latest
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf
    ports:
      - "80:80"  # Main port that Nginx will listen on
    depends_on:
      - spring-app
    networks:
      - app-network

networks:
  app-network:

```

### 3. Create `nginx.conf`

Create a folder named `nginx` in the same directory as `docker-compose.yml`, then create a file named `nginx.conf` in that folder and paste the following code into it:

```
nginx
Sao chép mã
events {}

http {
  upstream spring_app_cluster {
    server spring-app:8080;  # Dynamic connection to the Spring Boot replicas
  }

  server {
    listen 80;

    location / {
      proxy_pass http://spring_app_cluster;
      proxy_set_header Host $host;
      proxy_set_header X-Real-IP $remote_addr;
      proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      proxy_set_header X-Forwarded-Proto $scheme;
    }
  }
}

```

### 4. Build and Start Services

Open a terminal, navigate to the directory containing the `docker-compose.yml` file, and run the following command to build and start the services:

Sao chép mã
```bash
docker-compose up --build
```

### 5. Create Table in MySQL

Download and install MySQL Workbench or any other MySQL client to connect to the MySQL server running in the Docker container. Connect to the MySQL server using the following credentials:
https://downloads.mysql.com/archives/workbench/

```bash
-- Chọn cơ sở dữ liệu slaveDb
USE slaveDb;

-- Tạo bảng image_data
CREATE TABLE image_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    imagedata LONGBLOB NOT NULL
);

-- Tạo bảng product
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL
);

-- Tạo bảng product_images
CREATE TABLE product_images (
    product_id BIGINT,
    image_id BIGINT,
    PRIMARY KEY (product_id, image_id),
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES image_data(id) ON DELETE CASCADE
);
```
### 6. Check Application Status

Once the services are up and running, you can check the application by visiting [http://localhost](http://localhost/) in your browser. Nginx will distribute the traffic to the Spring Boot application replicas. The HTTP requests will be returned `Hello from instance: X` where X is the container ID of the Spring Boot replica.

## Stopping and Removing Services

To stop and remove the services, run the following command:

```bash
docker-compose down
```