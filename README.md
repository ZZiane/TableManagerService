# TableManager SQL API

## Overview

The **TableManager SQL API** is a RESTful service built using **Spring Boot** to manage SQL tables and databases. It allows for database connections, data insertion, table retrieval, and other database management operations. The service supports multiple database types, including H2, MySQL, and DB2.

## Features

- **Database Operations**: 
  - Create, delete, and list tables in the database.
- **Data Insertion**: 
  - Insert records into specified tables.
- **Multi-Database Support**: 
  - Connect to databases like MySQL, DB2, and H2 (in-memory).
- **Spring Boot Integration**:
  - Uses Spring Boot Actuator for health checks and monitoring.
  - Implements Spring Data JPA for database interactions.
  - Provides RESTful API endpoints via Spring Web.
- **Security**:
  - OAuth2 client authentication and resource server support with Spring Security.

## Technologies

- **Java**: 17
- **Spring Boot**: 3.2.1
  - Spring Data JPA
  - Spring Web
  - Spring Boot Actuator
  - Spring Security (OAuth2 and Resource Server)
- **Databases Supported**:
  - H2 (for development and testing)
  - MySQL
  - IBM DB2
## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ZZiane/TableManager.git
   cd TableManager
   mvn clean install
   mvn spring-boot:run

### **Security**
The API is secured using **Keycloak**. Authentication and authorization are handled through **OAuth2** and **JWT tokens**. Users must provide a valid JWT token obtained from Keycloak in the `Authorization` header to access most endpoints.

