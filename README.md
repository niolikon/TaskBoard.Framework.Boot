# TaskBoard.Framework.Boot

[![Build Workflow](https://github.com/niolikon/TaskBoard.Framework.Boot/actions/workflows/maven.yml/badge.svg)](https://github.com/niolikon/TaskBoard.Framework.Boot/actions)
[![Release Workflow](https://github.com/niolikon/TaskBoard.Framework.Boot/actions/workflows/release-maven.yml/badge.svg)](https://github.com/niolikon/TaskBoard.Framework.Boot/actions)
[![JitPack](https://jitpack.io/v/niolikon/TaskBoard.Framework.Boot.svg)](https://jitpack.io/#niolikon/TaskBoard.Framework.Boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

Task Board Framework and Common components (Spring Case Study)

# Overview

📚 **TaskBoard Framework** is a modular library for building scalable and testable applications using **Spring Boot**, **Keycloak**, and **Testcontainers**.

---

## 🚀 Features

- **Standardized Exception Handling**: Implements REST exception management following [RFC 7807 (Problem Details for HTTP APIs)](https://datatracker.ietf.org/doc/html/rfc7807).
- **Keycloak Authentication Integration**: Provides an authentication system that integrates with Spring Security OAuth and Keycloak.
- **Scenario-Based Testing**: Supports scenario-driven testing with JUnit extensions, annotations, and Spring Boot configurations.
- **Spring Boot Auto-Configuration**: Includes configuration classes that enable Spring Boot auto-discovery.

---

## 🏗️ Package Structure

```
com
 └───niolikon
     └───taskboard
         └───framework
             ├───exceptions
             │   ├───config
             │   ├───dto
             │   └───rest
             │       ├───client
             │       └───server
             ├───security
             │   ├───dto
             │   └───keycloak
             │       ├───client
             │       └───config
             └───test
                 ├───annotations
                 ├───containers
                 └───extensions
```

---

## 🛠️ Getting Started

### Prerequisites

- **Java 17+**
- **Maven 3+**
- **Docker** (optional, for integration testing with TestContainers)

### Quickstart Guide

1. Clone the repository:
   ```bash
   git clone https://github.com/niolikon/TaskBoard.Framework.Boot.git
   cd TaskBoard.Framework.Boot
   ```

2. Compile the project:
   ```bash
   mvn clean install
   ```

3. Run tests:
   ```bash
   mvn test
   ```

---

### Enabling Features

#### Global Exception Handling
To enable global exception handling, set the property:
```properties
taskboard.exceptions.handler.enabled=true
```

#### JWT Authentication Converter
To enable the definition of the `JwtAuthenticationConverter` bean, configure Spring Security OAuth Resource Server with:
```properties
spring.security.oauth2.resourceserver.jwt.issuer-uri=<issuer-uri>
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=<jwk-set-uri>
```

#### Keycloak JWT Authentication Service
To enable the framework-provided `JwtAuthenticationService` bean, configure the following properties:
```properties
taskboard.security.keycloak.auth-server-url=<auth-server-url>
taskboard.security.keycloak.realm=<realm>
taskboard.security.keycloak.client-id=<client-id>
taskboard.security.keycloak.client-secret=<client-secret>
```

---

## 🧪 Running Integration Tests

The project provides a **TestContainers-based testing environment**. To execute integration tests:

```bash
mvn verify
```

By default, tests will spin up an **ephemeral Keycloak instance** and any required database containers.

To define **custom test scenarios**, annotate your test methods with:

```java
@WithIsolatedKeycloakTestScenario(dataClass = SampleKeycloakScenario.class)
```

This will load the corresponding **Keycloak realm configuration** before test execution.

---

## 📦 Deployment

Since this is a **library**, it is meant to be included in your Maven projects as a dependency.

### Add JitPack repository to your Maven project:

```xml
<repositories>
   <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
   </repository>
</repositories>
```

### Add the dependency to your Maven project:

```xml
<dependency>
   <groupId>com.github.niolikon</groupId>
   <artifactId>TaskBoard.Framework.Boot</artifactId>
   <version>0.0.2</version>
</dependency>
```

---

## 📬 Feedback & Contributions

If you have suggestions or improvements, feel free to open an issue or create a pull request. Contributions are always welcome!

---

## 📝 License

This project is licensed under the MIT License.

---

🚀 **Developed by Simone Andrea Muscas | Niolikon**

