# EBANX Take-Home Assignment - Java Spring Boot API

## Overview

EBANX Software Engineer Take-home assignment. This project showcases my skills in building a REST API using Java and Spring Boot. The API provides two endpoints, `/balance` and `/event`, to handle simple account operations such as deposits, withdrawals, and transfers. The project focuses on clean code, proper encapsulation, and separation of concerns.

## Table of Contents

- [Requirements](#requirements)
- [Project Structure](#project-structure)
- [Endpoints](#endpoints)
- [Getting Started](#getting-started)

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
│   │   │               ├── model
│   │   │               │   └── Account.java
│   │   │               │   └── Event.java
│   │   │               └── SpringApiApplication.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── ebanx
│                       └── SpringApiApplication.java
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
   git clone https://github.com/matheusherman/EBANX-api.git
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

