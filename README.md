# High-Load Payment System

## 1. Overview

This project aims to design and implement a robust and scalable payment system capable of handling 3000 concurrent requests with a 5:1 read/write ratio. The system is optimized for reliability, low latency, and stable performance during high-traffic events like Black Friday.

## 2. System Architecture

The system is built on a microservices architecture, comprising the following key components:

*   **API Gateway (Nginx):** Acts as the single entry point for all requests, handling routing, load balancing, and authentication.
*   **Payment Service:** Receives and validates payment requests, then pushes them to a Kafka queue for asynchronous processing.
*   **Inquiry Service:** Handles query requests (e.g., balance, transaction history), optimized for data retrieval.
*   **Payment Worker:** Consumes messages from Kafka, executes the core transaction processing logic, and updates the primary database.
*   **Kafka (Confluent Platform):** A message queuing system that decouples components and enables reliable asynchronous processing.
*   **Redis:** A caching layer that stores frequently accessed data to reduce database load and improve response times for read operations.
*   **PostgreSQL (Primary-Replica):**
    *   **Primary:** The main database that handles all write operations.
    *   **Replicas:** Read-only copies that serve query requests from the `Inquiry Service`.

### Core Workflows

1.  **Payment Processing (Write Flow):**
    `API Gateway` → `Payment Service` → `Kafka` → `Payment Worker` → `PostgreSQL (Primary)`
2.  **Information Inquiry (Read Flow):**
    `API Gateway` → `Inquiry Service` → `Redis (Cache)` → `PostgreSQL (Replica)` (on cache miss)

## 3. Technology Stack

| Component          | Technology                              |
| ------------------ | --------------------------------------- |
| Language           | Java                                    |
| Framework          | Spring Boot                             |
| Database           | PostgreSQL 14 (Primary-Replica)         |
| Message Broker     | Kafka Confluent Platform 7.3.0          |
| Caching            | Redis                                   |
| API Gateway        | Nginx                                   |
| Containerization   | Docker, Docker Compose                  |
| Load Testing       | JMeter                                  |

## 4. Setup and Launch Guide

Ensure you have Docker and Docker Compose installed.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/locpn2/high-load-payment-system.git
    cd high-load-payment-system
    ```

2.  **Build the services:**
    ```bash
    # Build Payment Service
    cd payment-service
    ./mvnw clean package -DskipTests

    # Build Payment Worker
    cd ../payment-worker
    ./mvnw clean package -DskipTests
    ```

3.  **Launch the environment:**
    From the project's root directory, run the following command to start all containers (PostgreSQL, Kafka, Redis, Zookeeper, and application services):
    ```bash
    docker-compose up --build
    ```

4.  **Verify:**
    *   `Payment Service` will be running at `http://localhost:8080`
    *   `Payment Worker` will be running at `http://localhost:8081`
