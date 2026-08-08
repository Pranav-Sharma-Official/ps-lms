# 📚 Library Management System (LMS) - Backend API

<div align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=java&logoColor=white"/>
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img alt="MySQL" src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white"/>
  <img alt="JWT" src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens"/>
  <img alt="Razorpay" src="https://img.shields.io/badge/Razorpay-02042B?style=for-the-badge&logo=razorpay&logoColor=white"/>
</div>

---

## 📝 Overview

The **Library Management System (LMS)** is a comprehensive, enterprise-level backend REST API built using **Java 21** and **Spring Boot 4.1.0**. It is designed to manage the core operations of a modern library, including book cataloging, user memberships, loan processing, fine calculations, payment integrations, and reservation handling.

The system incorporates robust security using **Spring Security** and **JWT (JSON Web Tokens)**, ensuring safe access for different roles (Admin and User). It also integrates with **Razorpay** for handling online payments for fines and subscriptions, and utilizes **SMTP** for email notifications.

---

## ✨ Key Features & Modules

### 🔐 1. Authentication & Security (Role-Based Access)
- Secure User Registration and Login endpoints.
- **JWT (JSON Web Token)** based stateless authentication.
- Password recovery mechanism via secure email tokens.
- **Role-Based Access Control (RBAC):**
  - `ROLE_ADMIN`: Full access to manage books, genres, users, and override system settings.
  - `ROLE_USER`: Access to browse books, checkout, place reservations, manage their wishlist, and process payments.

### 📚 2. Book & Catalog Management
- CRUD operations for Books and Genres.
- Advanced search and filtering capabilities (by Title, Author, ISBN, Genre, Availability).
- Track book inventory, publication details, and stock count.

### 🔄 3. Book Loans & Returns (Circulation)
- Checkout (borrowing) and Check-in (returning) mechanisms.
- Tracks loan issue date, due date, and return status.
- Renewal system allowing users to extend their borrowing period.

### ⏳ 4. Reservation System
- Users can place a hold (reserve) on books that are currently checked out.
- Automated status updates when a reserved book becomes available.

### 💰 5. Fine Management & Payments
- Automatically calculates fines for overdue book returns.
- System allows administrators to view and waive fines.
- **Razorpay Integration:** Secure checkout gateway for users to pay their pending library fines or purchase subscriptions online.

### 💳 6. Memberships & Subscriptions
- Different tiers of subscription plans for library members.
- Manage active/expired subscriptions and limits on how many books a user can borrow simultaneously based on their tier.

### 🌟 7. User Wishlist & Reviews
- Users can maintain a personal wishlist of books they intend to read.
- Review and Rating system for books.

### 📧 8. Email Notifications
- SMTP Integration for transactional emails.
- Sends notifications for successful registration, password resets, payment confirmations, and overdue reminders.

---

## 🏗️ System Architecture & Tech Stack

### Core Technologies
- **Language:** Java 21
- **Framework:** Spring Boot 4.1.0 (Spring MVC, Spring Data JPA, Spring Security, Spring Mail)
- **Build Tool:** Maven
- **Database:** MySQL 8.x
- **ORM:** Hibernate (via Spring Data JPA)

### Security & Utilities
- **Authentication:** JWT (jjwt api v0.12.6)
- **Payment Gateway:** Razorpay Java SDK v1.4.9
- **Boilerplate Reduction:** Lombok

---

## 🗄️ Database Schema & Entities Overview

The domain model maps to a relational database scheme via JPA annotations. Key entities include:

- **`User`**: Core user details, linked to roles (`UserRole`).
- **`Book`**: Represents physical or digital media available in the library.
- **`Genre`**: Categories for the books.
- **`BookLoan`**: Transactional entity linking a User and a Book over a specific period.
- **`Reservation`**: Tracks hold requests on books.
- **`Fine`**: Ledger of monetary penalties for overdue items.
- **`Payment`**: Records of transactions via Razorpay.
- **`Subscription` & `SubscriptionPlan`**: Membership tiers and user active plans.
- **`Wishlist` & `BookReview`**: User engagement models.
- **`PasswordResetToken`**: Secure, time-sensitive tokens for account recovery.

---

## 📂 Project Directory Structure

```text
src/main/java/dev/pranavsharma/librarymanagementsystem/
├── configuration/       # Spring Security, JWT Filters & Beans
├── controller/          # REST API Endpoints (Admin, Auth, Books, Users, etc.)
├── domain/              # Enums (Roles, Statuses, Types)
├── event/               # Application Event Listeners & Publishers (e.g., Payments)
├── exception/           # Custom Exceptions & Global Exception Handler (Controller Advice)
├── mapper/              # DTO to Entity Converters (MapStruct/Manual)
├── model/               # JPA Entities (Database Tables)
├── payload/             # DTOs (Data Transfer Objects), Requests, and Responses
├── repository/          # Spring Data JPA Interfaces
└── service/             # Business Logic Interfaces and Implementations
    ├── gateway/         # External service integrations (Razorpay)
    └── impl/            # Implementation of service layer
```

---

## 🔌 API Endpoints (High-Level Overview)

| Module | Base Path | Description | Access |
|---|---|---|---|
| **Authentication** | `/auth` | Login, Signup, Password Reset | Public |
| **Admin Books** | `/api/admin/books` | Add, update, delete books | Admin |
| **Books** | `/api/books` | Browse, search books | Public / User |
| **Genres** | `/api/genres` | Manage book categories | User / Admin |
| **Book Loans** | `/api/book-loans` | Checkout, Checkin, Renew | User / Admin |
| **Reservations** | `/api/reservations` | Hold / Reserve books | User |
| **Fines** | `/api/fines` | View, calculate, waive fines | User / Admin |
| **Payments** | `/api/payments` | Initiate & Verify Razorpay txns | User |
| **Subscriptions** | `/api/subscriptions` | Manage user memberships | User / Admin |
| **Subscription Plans**| `/api/subscription-plans`| Define membership tiers | Admin |
| **Users** | `/api/users` | Manage profiles | User / Admin |
| **Wishlist** | `/api/wishlist` | Manage saved books | User |
| **Reviews** | `/api/reviews` | Post and view book ratings | User |

> Note: All `/api/**` endpoints (except some read-only book queries) require an `Authorization: Bearer <JWT_TOKEN>` header.

---

## ⚙️ Configuration & Environment Variables

The application relies on `application.properties` located in `src/main/resources/`. You can configure the application by setting the following environment variables or modifying the properties file directly.

### Database Settings (MySQL)
```properties
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/lms
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

### Email Settings (SMTP)
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### Razorpay Integration
```properties
razorpay.key.id=rzp_test_XXXXXXXXXXXX
razorpay.key.secret=XXXXXXXXXXXXXXXXXXXXXXXX
razorpay.callback.base-url=http://localhost:5173
```

---

## 🛠️ Installation & Setup Guide

### Prerequisites
1. **Java Development Kit (JDK) 21** installed.
2. **Maven** installed (or use the provided `mvnw` wrapper).
3. **MySQL Server** (Running locally on port 3306 or configured remotely).
4. **Razorpay Account** (For testing payment integrations, obtain test keys).

### Steps to Run Locally

1. **Clone the Repository**
   ```bash
   git clone <repository_url>
   cd library-management-system
   ```

2. **Configure the Database**
   - Create a MySQL database named `lms`:
     ```sql
     CREATE DATABASE lms;
     ```
   - Update `src/main/resources/application.properties` with your MySQL credentials, SMTP settings, and Razorpay API Keys.

3. **Build the Application**
   Using Maven wrapper:
   ```bash
   ./mvnw clean install -DskipTests
   ```
   *(On Windows, use `mvnw.cmd clean install -DskipTests`)*

4. **Run the Application**
   ```bash
   ./mvnw spring-boot:run
   ```
   The backend API will start on `http://localhost:5000`.

---

## 🛡️ Security Implementation Details
- **Password Hashing:** BCrypt Password Encoder.
- **Stateless Sessions:** The server uses `SessionCreationPolicy.STATELESS`.
- **CORS:** Configured in `SecurityConfig.java` to allow requests from the frontend URL (`http://localhost:5173`).
- **Custom JWT Filter:** Intercepts requests to validate the token, extract user details, and populate the `SecurityContext`.

---

## 🧪 Testing
To run the automated test suite (Unit and Integration tests):
```bash
./mvnw test
```
*Note: Ensure your test database configuration is correct before executing tests.*

---

## 📜 License
This project is proprietary and is created for demonstration and learning purposes. 

---
<div align="center">
  <i>Developed with ❤️ for robust backend architecture and library management.</i>
</div>
