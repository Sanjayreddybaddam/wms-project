# 🏭 Enterprise Warehouse Management System (WMS)

---

## 📌 Project Overview

The **Enterprise Warehouse Management System (WMS)** is a cloud-ready logistics platform designed to automate warehouse operations such as:

* 📦 Inventory tracking
* 🚚 Receiving & Putaway
* 🛒 Order fulfillment
* 🔐 Secure role-based access

It replaces manual systems with a **real-time, ACID-compliant inventory engine**.

---

## 🎯 Business Objectives

* Ensure **real-time inventory accuracy**
* Prevent **race conditions in stock updates**
* Maintain **ACID-compliant transactions**
* Enable **barcode/QR-based tracking**
* Achieve **fast API response (<200ms)**

---

## 👥 User Roles

### 👨‍💼 Admin

* Manage products
* Monitor inventory
* Control order lifecycle

### 👷 Operator

* View products
* Create orders
* Assist in picking workflow

---

## 🏗️ Architecture Diagram

### 🔧 Architecture Flow

```
Frontend (React)
        ↓
REST API Calls (Axios)
        ↓
Spring Boot Backend
        ↓
Service Layer (Business Logic)
        ↓
Repository Layer (JPA/Hibernate)
        ↓
PostgreSQL Database
```

---

## 🧩 Core Modules

### 📦 Inventory Management

* Multi-warehouse tracking
* Bin-level storage
* Real-time stock updates

### 🚚 Receiving & Putaway

* Intelligent bin allocation
* Capacity-based placement
* Transaction-safe operations

### 🛒 Order Management

* Order lifecycle:

  ```
  PENDING → PICKING → PACKED → SHIPPED
  ```
* Stock validation before picking
* Auto deduction during packing

### 🔐 Security

* JWT-based authentication
* Role-based access (ADMIN / OPERATOR)

### 🔳 Barcode/QR Integration

* Auto-generate QR codes
* Scan-based warehouse operations

---

## 📸 Screenshots

### 🔐 Login & Authentication

### 📦 Product Dashboard

### 🛒 Order Workflow

---

## 🧪 Testing

* ✅ Unit Testing with **JUnit 5**
* ✅ Mocking using **Mockito**
* ✅ Focus on:

  * Inventory Service
  * Order Service

---

## 📅 Development Roadmap

### ✅ Week 1

* Spring Boot setup
* Entity design
* CRUD APIs

### ✅ Week 2

* Receiving service
* Putaway logic
* Transaction management

### ✅ Week 3

* Order workflow
* Inventory deduction
* QR code generation

### ✅ Week 4

* Spring Security + JWT
* Role-based authorization
* React frontend integration
* UI for order processing

---

## 🚀 Getting Started

### 🔧 Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

---

### 💻 Frontend Setup

```bash
cd frontend
npm install
npm start
```

---

## 📌 API Endpoints

### 🛍️ Products

* `GET /api/products`
* `POST /api/products`
* `PUT /api/products/{id}`
* `DELETE /api/products/{id}`

### 📦 Orders

* `POST /api/orders`
* `GET /api/orders`
* `PUT /api/orders/{id}/status?status=PACKED`

---

## 🔒 Security

* JWT Authentication
* Role-based access control
* Secured endpoints using Spring Security

---

## 💡 Key Highlights

* ⚡ Real-time inventory updates
* 🔐 Secure REST APIs
* 🧠 Smart bin allocation logic
* 📦 Multi-bin stock handling
* 🚀 Scalable microservices-ready design

---

## 📎 GitHub Contribution

This repository includes:

* ✅ Continuous commits across 4 weeks
* ✅ Feature-based development
* ✅ Bug fixes and enhancements

---

## 👨‍💻 Author

**Sanjay Reddy Baddam**
Java Full Stack Developer
Spring Boot | React | Microservices | Cloud

---


