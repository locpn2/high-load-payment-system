# Active Context

**Current Work Focus:**

*   Setting up the development environment using Docker Compose.
*   Configuring PostgreSQL replication.

**Recent Changes:**

*   Created `docker-compose.yml` to define the services.
*   Created `init.sql` to initialize the database schema.
*   Successfully configured PostgreSQL replication.

**Next Steps:**

3.  Implement the Payment Service API.

**Active Decisions and Considerations:**

*   Choosing the appropriate PostgreSQL replication method (streaming replication).
*   Configuring the primary PostgreSQL server to allow replication connections.
*   Ensuring that the replica PostgreSQL servers are correctly configured to connect to the primary.

**Important Patterns and Preferences:**

*   Using Docker Compose for environment setup and management.
*   Following a microservices architecture.
*   Prioritizing reliability and low latency.

**Learnings and Project Insights:**

*   PostgreSQL replication requires careful configuration of both the primary and replica servers.
*   Docker Compose can simplify the deployment and management of complex systems.
