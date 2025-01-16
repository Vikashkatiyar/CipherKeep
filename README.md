# CipherKeep : Secure Secret Management System

This project focuses on building a **Secure Secret Management System** using **Java** and **Spring Boot**. The system provides robust features for managing sensitive data (secrets) with an emphasis on security, version control, access control, and detailed audit trails.

## Features

1. **Encryption and Decryption**
    - Secure storage of secrets with encryption.
    - Authorized decryption for permitted users.

2. **Role-Based Access Control (RBAC)**
    - Granular access control with roles:
        - **Admin**: Full access to manage secrets, users, and audit controls.
        - **Owner**: Owns specific secrets and assigns permissions to other users.
        - **User**: Accesses secrets based on permissions granted.

3. **CRUD Operations**
    - Create, Read, Update, and Delete secrets via REST APIs.

4. **Version Control**
    - Maintain up to 10 versions of a secret.
    - Archive older versions for audit and recovery.

5. **Pagination, Search, and Sorting**
    - Efficient listing of secrets with pagination.
    - Search secrets based on metadata.
    - Sort secrets by attributes like creation date or modification date.

6. **Audit Trail**
    - Detailed logs of all operations on secrets.
    - Ensure logs encrypt sensitive data.

7. **Re-encryption Cron Job**
    - Automatically re-encrypt secrets older than 10 days.

## Development Stages

### Development Stage 1: Project Setup, Swagger, and CRUD with Encryption
1. **Project Setup**
    - Spring Boot configuration.
    - Database schema design.
    - Swagger for API documentation.

2. **CRUD Operations**
    - Implement APIs for creating, reading, updating, and deleting secrets.
    - Encrypt secret data before storage and decrypt it for authorized users.

3. **Encryption Handling**
    - Secure encryption mechanisms for all secret data.

---

### Development Stage 2: RBAC for CRUD Operations and Version Control
1. **RBAC Implementation**
    - Use **JWT** and **Spring Security** for authentication and role-based access.

2. **Version Control**
    - Maintain and archive older versions of secrets.
    - Ensure audit trails capture version updates.

3. **API Enhancements**
    - Enable owners to view and revert to previous versions.

---

### Development Stage 3: Pagination, Sorting, Re-encryption Cron Job, and Audit Trail
1. **Pagination, Search, and Sorting**
    - Implement APIs with pagination, search, and sorting functionality.

2. **Re-encryption Cron Job**
    - Scheduled task to re-encrypt secrets older than 10 days.

3. **Enhanced Audit Trail**
    - Log all operations, with sensitive data encrypted even in logs.

---

### Development Stage 4: Logging, Reporting & Fine-Grained Access
1. **Fine-Grained Access Control**
    - Allow Owners to specify detailed permissions for secrets.

2. **Reports and Monitoring**
    - Generate usage reports:
        - Total number of secrets.
        - Encryption status summary (e.g., last encryption date).

---

## Prerequisites

1. **Java 17**
2. **Spring Boot 3.x**
3. **MySQL/PostgreSQL** for the database
4. **Swagger** for API documentation

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Vikashkatiyar/CipherKeep.git
   ```

2. Set up the database:
    - Create the necessary database schema as per the project design.

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

4. Access Swagger API documentation:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```

## Project Structure

- `controller`: Handles REST API endpoints.
- `service`: Contains business logic.
- `repository`: Handles database interactions.
- `entity`: Contains entity classes for database tables.
- `config`: Configurations for security, encryption, and application settings.

## Future Enhancements

1. Add support for external secret stores (e.g., AWS Secrets Manager, Azure Key Vault).
2. Enhance reporting with analytics dashboards.
3. Implement secret expiration and notifications.

## License
This project is licensed under the MIT License.
