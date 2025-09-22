# Active Context

**Current Work Focus:**

*   [x] **Decoupled Payment Worker from Payment Service** - COMPLETE
*   [x] **Khởi tạo project `inquiry-service`** - COMPLETE
*   [x] **Implementing Cache invalidation mechanism and Inquiry Service** - COMPLETE
*   [x] **Integrating Redis caching and PostgreSQL replica reading** - COMPLETE
*   [x] **Giai đoạn 1: Xây dựng Inquiry Service & Tích hợp Caching** - COMPLETE
*   [x] **Giai đoạn 2: Triển khai Cơ chế Vô hiệu hóa Cache (Cache Invalidation)** - COMPLETE

**Recent Changes:**

*   [x] Successfully started all Docker services (PostgreSQL, Kafka, Redis, Zookeeper).
*   [x] Built and deployed Payment Service Spring Boot application.
*   [x] Fixed database connection configuration mismatch.
*   [x] Implemented Payment Worker with Kafka consumer functionality.
*   [x] Enhanced Payment Service to send messages to Kafka asynchronously.
*   [x] Added proper error handling and logging throughout the system.
*   [x] Verified Payment Service API endpoints are operational (HTTP 200 responses).
*   [x] Updated project progress documentation.
*   [x] **Bước 1 hoàn thành**: Khởi tạo dự án `payment-worker` với cấu trúc Spring Boot cơ bản.
*   [x] **Bước 2 hoàn thành**: Di chuyển và tách biệt mã nguồn (PaymentWorker, PaymentRequest, Payment, PaymentRepository).
*   [x] **Bước 3 hoàn thành**: Cấu hình cho `payment-worker` (application.yml với port 8081, Kafka consumer, database).
*   [x] **Bước 4 hoàn thành**: Tái cấu trúc `payment-service` (xóa thư mục worker, cập nhật PaymentService để chỉ gửi Kafka message).
*   [x] **Bước 5 hoàn thành**: Cập nhật Cấu hình Triển khai (Docker Compose) - thêm service payment-worker.
*   [x] **Bước 6 hoàn thành**: Kiểm thử và Xác minh - cả payment-service và payment-worker đều compile thành công.
*   [x] **Fixed Docker build error**: Created Dockerfiles for both services and updated docker-compose.yml.
*   [x] **Fixed "Broker may not be available" error**: Updated application.yml files to use Spring Profiles for Docker environment.
*   [x] **Khởi tạo project `inquiry-service`**: Created new Spring Boot project for inquiry service with basic structure.
*   [x] **Bước 1 hoàn thành**: Sao chép Payment entity và PaymentRequest DTO từ payment-worker sang inquiry-service.
*   [x] **Bước 2 hoàn thành**: Cấu hình application.yml cho inquiry-service (port 8082, kết nối PostgreSQL replica, Redis).
*   [x] **Bước 3 hoàn thành**: Triển khai PaymentRepository interface với các truy vấn cần thiết.
*   [x] **Bước 4 hoàn thành**: Triển khai InquiryService với logic Cache-Aside pattern và InquiryController với REST endpoints.
*   [x] **Bước 5 hoàn thành**: Tạo Dockerfile cho inquiry-service và cập nhật docker-compose.yml.
*   [x] **Fixed Kafka connection error in inquiry-service**: Added Docker profile configuration to use `kafka:9093` instead of `localhost:9092` for Kafka bootstrap servers.

**Next Steps:**

1.  [ ] **Giai đoạn 3: Cấu hình API Gateway và Load Balancer (Nginx)**
    *   [ ] Tạo file cấu hình cho Nginx (`nginx.conf`).
    *   [ ] Định nghĩa các `upstream` cho từng service (`payment-service`, `inquiry-service`).
    *   [ ] Thiết lập `location blocks` để điều hướng request dựa trên URL.
    *   [ ] Cập nhật `docker-compose.yml` để thêm service `nginx`.

2.  [ ] **Giai đoạn 4: Kiểm thử và Tối ưu hóa**
    *   [ ] Kiểm thử End-to-End toàn bộ luồng hệ thống.
    *   [ ] Viết kịch bản kiểm thử tải bằng JMeter.
    *   [ ] Thực thi load test và phân tích kết quả.
    *   [ ] Tối ưu hóa hiệu suất dựa trên kết quả kiểm thử.

**Active Decisions and Considerations:**

*   Implementing Cache-Aside pattern for Redis integration in inquiry-service.
*   Configuring inquiry-service to read from PostgreSQL replicas for load distribution.
*   Designing Payment Worker to handle transaction processing asynchronously.
*   Ensuring data consistency between cache and database through invalidation mechanism.
*   Using port 8082 for inquiry-service to avoid conflicts with existing services.

**Important Patterns and Preferences:**

*   Using Docker Compose for environment setup and management.
*   Following a microservices architecture.
*   Prioritizing reliability and low latency.
*   Implementing asynchronous processing with Kafka.
*   Refactoring CacheInvalidationListener to use InquiryService.invalidateCache() method for better code reusability and maintainability.

**Learnings and Project Insights:**

*   PostgreSQL replication requires careful configuration of both the primary and replica servers.
*   Docker Compose can simplify the deployment and management of complex systems.
*   Spring Boot with Kafka integration provides robust asynchronous processing capabilities.
*   Proper caching strategies are crucial for high-read scenarios.
