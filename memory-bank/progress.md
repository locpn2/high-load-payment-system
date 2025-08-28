# Progress

**What Works:**

*   Basic project structure and initial Git repository setup.
*   Docker Compose file created with definitions for Zookeeper, Kafka, PostgreSQL (primary and replicas), and Redis.

**What's Left to Build:**

- [x] Configure PostgreSQL replication correctly.
*   Implement Payment Service API.
    *   Defined data model (Payment entity)
    *   Implemented PaymentRepository
    *   Implemented PaymentService
*   Defined API endpoints in PaymentController
*   Configured application.yml
*   Implement Payment Worker (Kafka Consumer).
*   Implement Cache invalidation mechanism.
*   Implement Inquiry Service API.
*   Integrate Caching.
*   Configure Read from Replica.
*   Set up API Gateway and Load Balancer.
*   Write Load Test scripts with JMeter.
*   Execute and Analyze Results.
*   Tune and Optimize.

**Current Status:**

*   Development environment setup in progress.
*   PostgreSQL replication is working correctly.

**Known Issues:**

*   None

**Evolution of Project Decisions:**

*   Switched from RabbitMQ to Kafka Confluent Platform for message queuing.
*   Switched from MySQL 8 to PostgreSQL for the database.
*   Using Nginx as the API Gateway.
*   Using JMeter for load testing.
