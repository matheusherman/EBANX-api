# EBANX Take-Home Assignment - Java Spring Boot API

## Overview

EBANX Software Engineer Take-home assignment. This project showcases my skills in building a REST API using Java and Spring Boot. The API provides two endpoints, `/balance` and `/event`, to handle simple account operations such as deposits, withdrawals, and transfers. The project focuses on clean code, proper encapsulation, and separation of concerns.

## Table of Contents

- [Requirements](#requirements)
- [Project Structure](#project-structure)
- [Endpoints](#endpoints)
- [Getting Started](#getting-started)
- [Running Tests](#running-tests)
- [Version Control](#version-control)

## Requirements

- Java 17
- Maven
- Spring Boot

## Project Structure

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── ebanx
│   │   │               ├── controller
│   │   │               │   └── AccountController.java
│   │   │               ├── service
│   │   │               │   └── AccountService.java
│   │   │               ├── model
│   │   │               │   └── Account.java
│   │   │               ├── dto
│   │   │               │   └── EventDTO.java
│   │   │               ├── exception
│   │   │               │   └── AccountNotFoundException.java
│   │   │               └── EbanxApplication.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── ebanx
│                       └── EbanxApplicationTests.java
└── README.md
```

## Endpoints

### GET /balance

Fetches the balance of a specified account.

**Request:**
```
GET /balance?account_id={id}
```

**Responses:**
- `200 OK` - Returns the account balance
- `404 Not Found` - Account does not exist

### POST /event

Handles events such as deposits, withdrawals, and transfers.

**Request:**
```
POST /event
Content-Type: application/json
{
  "type": "deposit|withdraw|transfer",
  "destination": "account_id",
  "origin": "account_id",
  "amount": value
}
```

**Responses:**
- `201 Created` - Event processed successfully
- `404 Not Found` - Account does not exist (for withdrawals and transfers)

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/ebanx-assignment.git
   cd ebanx-assignment
   ```

2. **Build the project:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **API will be available at:**
   ```
   http://localhost:8080
   ```

## Running Tests

To run the provided test suite, use the following command:

```bash
mvn test
```

The tests include scenarios such as creating accounts, making deposits, withdrawing funds, and transferring balances between accounts.

## Version Control

The project follows best practices in version control, with meaningful commit messages documenting the step-by-step development process. You can explore the commit history to understand the evolution of the project.

## Conclusion

This project demonstrates my ability to create a simple yet effective REST API using Java and Spring Boot. I focused on keeping the codebase clean, well-structured, and easy to modify, ensuring it meets the requirements outlined in the challenge.

For any questions or further clarifications, please feel free to reach out.

Happy coding!
