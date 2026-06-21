# Ski Station Management — Spring Boot REST API

![Java](https://img.shields.io/badge/Java-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?logo=swagger&logoColor=black)

A **Spring Boot** REST API for managing a ski station — subscribers, instructors, courses, and pistes — built with a clean layered architecture and documented with OpenAPI/Swagger. Developed as the application base for a DevOps / CI/CD workflow.

> **Stack:** Java · Spring Boot · Spring Data JPA · MySQL · Lombok · springdoc-openapi

## Features

- **RESTful CRUD API** for the ski-station domain (`gestion-station-ski`)
- **Persistence** — Spring Data JPA over MySQL
- **API documentation** — interactive Swagger UI via springdoc-openapi
- **Clean code** — Lombok to reduce boilerplate; layered controller/service/repository design
- **DevOps-ready** — structured as the deployable artifact for an automated build/test/deploy pipeline

## Tech Stack

| Layer | Technologies |
|---|---|
| Language | Java |
| Framework | Spring Boot (Web, Data JPA, DevTools) |
| Database | MySQL |
| Docs | springdoc-openapi (Swagger UI) |
| Tooling | Maven, Lombok |

## Getting Started

```bash
git clone https://github.com/salahbnh/Project-DevOps.git
cd Project-DevOps
# configure src/main/resources/application.properties with your MySQL credentials
mvn spring-boot:run
# Swagger UI: http://localhost:8080/swagger-ui/
```

---

Built by [Salah Bounouh](https://github.com/salahbnh) · [Portfolio](https://salahbounouh.com) · [LinkedIn](https://www.linkedin.com/in/salah-bounouh-1426ba27b/)
