# Load Balancer Demo

# Setup Guide

## Introduction

This guide will help you set up a demo environment using Docker Compose with MySQL, Spring Boot, and Nginx to simulate a load balancer. We will create a Spring Boot application with 3 replicas and use Nginx to distribute traffic to these replicas.

## Prerequisites

- Docker
- Docker Compose

## Setup Instructions

### 1. Download the Source Code

Download the source code or create a new directory and set up the required files.

### 2. Build and Start Services

Open a terminal, navigate to the directory containing the `docker-compose.yml` file, and run the following command to build and start the services:

```bash
docker-compose up --build
```

### 3. Create Table in MySQL

Download and install MySQL Workbench or any other MySQL client to connect to the MySQL server running in the Docker container. Connect to the MySQL server using the following credentials:
https://downloads.mysql.com/archives/workbench/

```bash
-- Choose the slaverDb database
USE slaveDb;

-- Create Table image_data
CREATE TABLE image_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    imagedata LONGBLOB NOT NULL
);

-- Create Table product
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL
);

-- Create Table product_images
CREATE TABLE product_images (
    product_id BIGINT,
    image_id BIGINT,
    PRIMARY KEY (product_id, image_id),
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) REFERENCES image_data(id) ON DELETE CASCADE
);
```

### 4. Check Application Status

Once the services are up and running, you can check the application by visiting [http://localhost](http://localhost/) in your browser. Nginx will distribute the traffic to the Spring Boot application replicas. The HTTP requests will be returned `Hello from instance: X` where X is the container ID of the Spring Boot replica.

## Stopping and Removing Services

To stop and remove the services, run the following command:

```bash
docker-compose down
```
