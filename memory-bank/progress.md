# Progress

**What Works:**

*   [x] Basic project structure and initial Git repository setup.
*   [x] Docker Compose file created with definitions for Zookeeper, Kafka, PostgreSQL (primary and replicas), and Redis.
*   [x] PostgreSQL replication configured and working correctly.
*   [x] Payment Service API fully implemented:
    *   [x] Payment entity with complete data model
    *   [x] PaymentRepository for database operations
    *   [x] PaymentService for business logic
    *   [x] PaymentController with full CRUD API endpoints
    *   [x] Application configuration for Kafka and Redis
    *   [x] Dependencies added (Gson for JSON processing)
*   [x] Payment Worker implementation:
    *   [x] PaymentWorker with Kafka consumer functionality
    *   [x] Asynchronous message processing
    *   [x] Error handling and logging
*   [x] Inquiry Service project structure:
    *   [x] Spring Boot project initialized
    *   [x] Basic Maven configuration
    *   [x] Standard directory structure
*   [x] Development environment fully operational with all services running.

**What's Left to Build:**

*   [x] **Decouple Payment Worker from Payment Service** - COMPLETE
*   [x] **Khởi tạo project `inquiry-service`** - COMPLETE
*   [x] **Implement Inquiry Service API** - COMPLETE
*   [x] **Giai đoạn 1: Xây dựng Inquiry Service & Tích hợp Caching** - COMPLETE
*   [x] **Giai đoạn 2: Triển khai Cơ chế Vô hiệu hóa Cache (Cache Invalidation)** - COMPLETE
*   [x] **Refactored CacheInvalidationListener to use InquiryService.invalidateCache()** - COMPLETE
*   [ ] **Giai đoạn 3: Cấu hình API Gateway và Load Balancer (Nginx)**
*   [ ] **Giai đoạn 4: Kiểm thử và Tối ưu hóa**

**Current Status:**

*   [x] Development environment setup completed successfully.
*   [x] Payment Service API is fully operational and tested.
*   [x] Payment Worker successfully decoupled and operational.
*   [x] All infrastructure services (PostgreSQL, Kafka, Redis) are operational.
*   [x] Database connection issues resolved.
*   [x] API endpoints responding correctly (HTTP 200).
*   [x] Inquiry Service fully implemented with Cache-Aside pattern and Redis integration.
*   [x] Cache invalidation mechanism implemented for data consistency.
*   [x] All three microservices (payment-service, payment-worker, inquiry-service) operational.

**Known Issues:**

*   None
*   **Fixed**: Kafka connection error in inquiry-service - Added Docker profile configuration to use `kafka:9093` instead of `localhost:9092` for Kafka bootstrap servers.

**Evolution of Project Decisions:**

*   Switched from RabbitMQ to Kafka Confluent Platform for message queuing.
*   Switched from MySQL 8 to PostgreSQL for the database.
*   Using Nginx as the API Gateway.
*   Using JMeter for load testing.
