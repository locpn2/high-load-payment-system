# System Patterns

**Architecture:**

*   Microservices architecture.
*   API Gateway (Nginx) for routing and authentication.
*   Load Balancer for distributing traffic.
*   Asynchronous processing using Kafka.
*   Caching layer (Redis) for read-heavy operations.
*   Primary-Replica database setup (PostgreSQL).

**Key Technical Decisions:**

*   Using Kafka for asynchronous message processing to improve performance and reliability.
*   Using Redis for caching to reduce database load and improve response times.
*   Using PostgreSQL with a Primary-Replica setup to handle high read/write loads.

**Design Patterns:**

*   Cache-Aside pattern for managing data in Redis.
*   Asynchronous processing pattern for handling payment transactions.
*   Connection Pool pattern for managing database connections.

**Component Relationships:**

1.  **API Gateway -> Payment Service:** Routes payment requests to the Payment Service.
2.  **API Gateway -> Inquiry Service:** Routes inquiry requests to the Inquiry Service.
3.  **Payment Service -> Kafka:** Publishes payment transaction messages to Kafka.
4.  **Kafka -> Payment Worker:** Payment Worker consumes payment transaction messages from Kafka.
5.  **Payment Worker -> PostgreSQL (Primary):** Payment Worker interacts with the Primary PostgreSQL database to process transactions.
6.  **Inquiry Service -> Redis:** Inquiry Service retrieves data from Redis cache.
7.  **Inquiry Service -> PostgreSQL (Replicas):** Inquiry Service retrieves data from PostgreSQL replicas if not found in cache.

**Critical Implementation Paths:**

1.  **Payment Request:** API Gateway -> Payment Service -> Kafka -> Payment Worker -> PostgreSQL (Primary)
2.  **Balance Inquiry:** API Gateway -> Inquiry Service -> Redis (Cache Hit) or PostgreSQL (Replica)
