# Progress

**What Works:**

*   Basic project structure and initial Git repository setup.
*   Docker Compose file created with definitions for Zookeeper, Kafka, PostgreSQL (primary and replicas), and Redis.
*   PostgreSQL replication configured and working correctly.
*   Payment Service API fully implemented:
    *   Payment entity with complete data model
    *   PaymentRepository for database operations
    *   PaymentService for business logic
    *   PaymentController with full CRUD API endpoints
    *   Application configuration for Kafka and Redis
    *   Dependencies added (Gson for JSON processing)
*   Development environment fully operational with all services running.

**What's Left to Build:**

*   Implement Cache invalidation mechanism.
*   Implement Inquiry Service API.
*   Integrate Caching.
*   Configure Read from Replica.
*   Set up API Gateway and Load Balancer.
*   Write Load Test scripts with JMeter.
*   Execute and Analyze Results.
*   Tune and Optimize.

**Current Status:**

*   Development environment setup completed successfully.
*   Payment Service API is fully operational and tested.
*   All infrastructure services (PostgreSQL, Kafka, Redis) are operational.
*   Database connection issues resolved.
*   API endpoints responding correctly (HTTP 200).

**Known Issues:**

*   None

**Evolution of Project Decisions:**

*   Switched from RabbitMQ to Kafka Confluent Platform for message queuing.
*   Switched from MySQL 8 to PostgreSQL for the database.
*   Using Nginx as the API Gateway.
*   Using JMeter for load testing.
