# Project Brief: High-Load Payment System

**Objective:** Design and implement a robust and scalable payment system capable of handling 3000 concurrent requests, with a read/write ratio of 5:1, while ensuring reliability and low latency. The system must be able to withstand high load during peak events like Black Friday.

**Resource Constraints:**

*   6 servers available
*   Maximum 3 servers for relational databases
*   250 connections per database server

**Key Requirements:**

*   **Scalability:** Handle 3000 concurrent requests.
*   **Reliability:** Ensure data integrity and prevent inappropriate payments.
*   **Low Latency:** Provide a fast and responsive user experience.
*   **High Read/Write Ratio:** Optimize for a 5:1 read/write ratio (2500 reads/s, 500 writes/s).

**Technologies:**

*   Java + Spring Boot
*   PostgreSQL (Primary-Replica)
*   Kafka Confluent Platform
*   Redis
*   Nginx
*   JMeter

**Git Repository:**

*   Initialized in the `payment-system` directory.
