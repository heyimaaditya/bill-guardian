# 🛡️ BillGuardian: The Autonomous Household CFO

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-19-61DAFB?style=for-the-badge&logo=react)](https://reactjs.org/)
[![Kafka](https://img.shields.io/badge/Apache_Kafka-3.7-black?style=for-the-badge&logo=apachekafka)](https://kafka.apache.org/)
[![Redis](https://img.shields.io/badge/Redis-7.0-DC382D?style=for-the-badge&logo=redis)](https://redis.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)

**BillGuardian** is a high-performance, AI-driven financial intelligence platform designed to eliminate the **"Lazy Tax."** It automatically audits bank statements, identifies "zombie" subscriptions, and generates high-leverage negotiation scripts to lower utility and service bills.

---

## 🚀 The Core Problem: "Subscription Creep"
The average middle-class household now pays for 10–15+ recurring services. Most people overpay for internet, phone, and utilities because they lack the time to negotiate or track price hikes. BillGuardian acts as an **Autonomous CFO**, identifying overcharges and providing "Negotiation Evidence" to save money.

---

## 🧠 System Architecture

BillGuardian is designed with a **Modular Monolith** approach using an **Event-Driven Data Pipeline**.

- **Frontend:** React 19 + TypeScript + Vite (Material UI Dashboard)
- **Backend:** Spring Boot 3.4 + Java 21 (Virtual Threads/Project Loom)
- **Messaging:** Apache Kafka (Asynchronous statement processing)
- **Persistence:** PostgreSQL (Transaction storage) + Redis (Analytics caching)
- **AI Intelligence:** Context-aware negotiation script generator

---

## 🌟 Senior-Level Engineering Features

### 🧩 1. Pluggable Strategy Design Pattern
The system uses the **Strategy Pattern** to handle bank-specific CSV formats. The `StatementProcessingService` auto-detects if an upload is from **Chase**, **Amex**, or other banks by "sniffing" the file header, allowing the system to scale to new banks without changing core code.

### ⛓️ 2. Idempotent Data Pipeline (SHA-256 Hashing)
To prevent data duplication, every transaction is assigned a unique **Fingerprint** using SHA-256 hashing. This ensures that even if a user uploads the same statement multiple times, the database remains clean and accurate.

### ⚡ 3. Performance Tuning (Redis Caching)
The Dashboard Summary is optimized using the **Cache-Aside Pattern**. Analytics (Total Burn, Savings) are cached in **Redis** and automatically evicted only when new data is uploaded. This reduces PostgreSQL load and ensures sub-10ms UI response times.

### 🛡️ 4. Privacy Shield (PII Masking)
Financial privacy is paramount. BillGuardian implements a **Privacy Utility** that uses Regex to mask sensitive data (Account numbers, Credit Cards, Emails) at the point of ingestion before it is stored or processed by AI.

### 📡 5. Real-Time UI Synchronization (WebSockets)
Built with **STOMP/WebSockets**, the dashboard provides instant feedback. When the background Kafka consumer finishes an AI audit, it pushes a notification to the React UI to refresh the data without the user needing to click "Reload."

---

## 🛠️ Project Structure

```text
bill-guardian/
├── backend/                # Spring Boot App
│   ├── src/main/java/com/billguardian/cfo/
│   │   ├── api/            # REST Controllers
│   │   ├── domain/         # Business Logic (Services & Strategies)
│   │   ├── config/         # Kafka, Redis, WebSocket, Security Configs
│   │   └── shared/         # Common Utilities (Hashing, Privacy)
├── frontend/               # React Vite App
│   ├── src/
│   │   ├── api/            # Axios Client & WebSocket Hooks
│   │   ├── components/     # Dashboard, Uploader, Calendar
│   │   └── theme/          # MUI Dark Mode Config
└── docker-compose.prod.yml # Production Orchestration

## 🚦 Getting Started

### 🛠️ Prerequisites
Ensure you have the following installed on your local machine:
*   **Docker Desktop** (Recommended for easy setup)
*   **Java 21 JDK** (For backend development)
*   **Node.js 20+** (For frontend development)

### 🚀 Launch the Full Stack (Docker)
To spin up the entire production-ready environment (Postgres, Redis, Kafka, Backend, and Frontend) with a single command:

```bash
docker-compose -f docker-compose.prod.yml up --build -d

🔗 Accessing the App
Once the containers are healthy, you can access the system at the following endpoints:
Main Dashboard: http://localhost (Served via Nginx)
API Health Check: http://localhost:8080/api/statements/health
Database: localhost:5432 (PostgreSQL)
🏗️ Technical Highlights (Senior Engineering)
Event-Driven: Uses Kafka for non-blocking background audit processing.
Idempotency: SHA-256 transaction hashing to prevent duplicate data entry.
Real-time: WebSockets (STOMP) for instant dashboard updates after AI audits.
Performance: Redis caching for the Analytics summary to ensure <10ms response times.
Security: PII Masking at the ingestion level to protect sensitive bank information.


📝 Author
Aaditya
Software Engineer / Architect
Built with ❤️ for Financial Freedom.