# Technical Context

**Technologies Used:**

*   **Programming Language:** Java
*   **Framework:** Spring Boot
*   **Database:** PostgreSQL 14
*   **Message Broker:** Kafka Confluent Platform 7.3.0
*   **Caching:** Redis (latest)
*   **API Gateway:** Nginx
*   **Load Testing:** JMeter
*   **Containerization:** Docker
*   **Orchestration:** Docker Compose

**Development Setup:**

*   Docker Compose is used to define and manage the development environment.
*   The environment includes:
    *   PostgreSQL (Primary and 2 Replicas)
    *   Kafka
    *   Zookeeper
    *   Redis

**Technical Constraints:**

*   Limited number of servers (6 total, max 3 for DB).
*   Limited number of database connections (250 per server).

**Dependencies:**

*   Spring Boot dependencies for web development, data access, and messaging.
*   PostgreSQL JDBC driver for database connectivity.
*   Kafka client libraries for producing and consuming messages.
*   Redis client libraries for caching.

**Tool Usage Patterns:**

*   Docker Compose: Used to start and stop the development environment.
*   Git: Used for version control.
*   JMeter: Used for load testing.
