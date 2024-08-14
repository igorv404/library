# Library

This repository contains a system for managing library.

## Prerequisites

Ensure you have Java 21, Docker and Docker Compose installed on your machine. You can download Docker from
the [Docker's official website](https://www.docker.com/products/docker-desktop/).

## How to run the project

1. **Create the database:**

```bash
docker compose up -d
```

2. **Run the project:**

For Unix systems:

```bash
./mvnw spring-boot:run
```

For Windows systems:

```cmd
mvnw.cmd spring-boot:run
```

## Additional Information

- The API documentation is available at: ```http://localhost:8080/swagger-ui/index.html```
