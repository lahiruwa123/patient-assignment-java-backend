# **README.md**

# 🚀 Full-Stack Application

### **Spring Boot + PostgreSQL (Docker) + React + Swagger**

This project is a full-stack application consisting of:

* **Backend:** Java Spring Boot
* **Database:** PostgreSQL running in **Docker**
* **Frontend:** React
* **API Documentation:** Swagger (SpringDoc OpenAPI)

# 1️⃣ **Requirements**

Make sure you have installed:

* Java **17+**
* Maven or Gradle
* Node.js **18+**
* Docker & Docker Compose
* Git (optional)

---

# 2️⃣ **Backend Setup (Spring Boot)**

### application.yml

```yaml
spring:
  datasource:
  url: jdbc:postgresql://127.0.0.1:5432/patient
  username: myuser
  password: mypassword
  driver-class-name: org.postgresql.Driver
  hikari:
    maximum-pool-size: 10
    minimum-idle: 2
    connection-timeout: 30000

server:
  port: 8000
```

---

## 📘 **Swagger / OpenAPI Setup**

Add dependency in **pom.xml**:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

Swagger will be available at:

👉 **[http://localhost:8000/patient-api/patient-api-documentation.html](http://localhost:8000/patient-api/patient-api-documentation.html)**

---

## ▶️ **Run Backend Locally**

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

---

# 3️⃣ **Database Setup — PostgreSQL in Docker**

## docker-compose.yml

```yaml
services:
  postgres:
    image: postgres:17  # Changed from 'latest' to specific version 17
    environment:
      POSTGRES_DB: patient
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: mypassword
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data  # Keep the original path for PG17
    networks:
      - postgres-network

  pgadmin:
    image: dpage/pgadmin4:latest
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@admin.com
      PGADMIN_DEFAULT_PASSWORD: admin
      # Add these CSRF settings:
      PGADMIN_CONFIG_ENHANCED_COOKIE_PROTECTION: "False"
      PGADMIN_CONFIG_WTF_CSRF_ENABLED: "False"
    ports:
      - "8080:80"
    depends_on:
      - postgres
    networks:
      - postgres-network

volumes:
  postgres_data:

networks:
  postgres-network:
    driver: bridge
```

---

## ▶️ **Start All Containers**

```bash
docker-compose up --build
```

---

# 4️⃣ **Frontend Setup (React)**

Install dependencies:

```bash
cd frontend
npm install
```

Run locally:

```bash
npm run dev
```

Default ports:

* Frontend → **[http://localhost:3000](http://localhost:3000)**
* Backend → **[http://localhost:8000](http://localhost:8000)**

---

## 🔗 Connecting React to Backend

Create **frontend/.env**

```
VITE_API_URL=http://localhost:8000/patient-api
```

Use in React:

```javascript
const API_URL = import.meta.env.VITE_API_URL;
```
# 5️⃣ **Swagger API Documentation**

Once backend is running:

📄 **Swagger UI:**
`http://localhost:8000/patient-api/patient-api-documentation.html`

📄 **OpenAPI JSON:**
`http://localhost:8000/patient-api/patient-registration-api`

# ✅ Done!

You now have a full-stack environment using:

✔ Spring Boot
✔ PostgreSQL (Docker)
✔ React
✔ Swagger API Docs
✔ Full docker-compose orchestration

---
