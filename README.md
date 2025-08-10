# Capital_ToDoListApp

Capital Group Take-Home Assignment - Solution Engineering: To Do List App

## 📝 Overview

This full-stack web application helps users manage their daily tasks effectively. Users can create, update, delete, search, and organize tasks and subtasks with categories, priorities, and statuses.

---

## ✅ Features

- Add, edit, delete, and restore tasks and subtasks
- Set due dates, priorities, and categories
- Mark tasks as completed or pending
- Search tasks by keyword
- Filter tasks by status or category
- View deleted tasks and restore them
- RESTful API design
- Backend unit and integration testing

---

## 🛠️ Technology Stack

### Backend
- Java 11
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- H2 (In-memory) or Oracle DB
- JUnit 5, Mockito

### Frontend
- React (running at `http://localhost:3000`)

### Tools
- Maven
- Git

---

## 🔗 API Endpoints

| HTTP Method | Endpoint                           | Description                          |
|-------------|------------------------------------|--------------------------------------|
| GET         | `/tasks`                           | Get all tasks                        |
| GET         | `/tasks/{id}`                      | Get task by ID                       |
| POST        | `/tasks`                           | Create a new task                    |
| PUT         | `/tasks/{id}`                      | Update task by ID                    |
| DELETE      | `/tasks/{id}`                      | Soft-delete task by ID               |
| PUT         | `/tasks/{id}/restore`              | Restore a deleted task               |
| GET         | `/tasks/deleted`                   | Get all deleted tasks                |
| GET         | `/tasks/status/{status}`           | Get tasks by status (e.g. COMPLETED) |
| GET         | `/tasks/category/{category}`       | Get tasks by category (e.g. WORK)    |
| GET         | `/tasks/search?keyword=...`        | Search tasks by keyword              |
| GET         | `/tasks/{id}/subtasks`             | Get subtasks for a specific task     |

> ℹ️ Status values: `PENDING`, `IN_PROGRESS`, `COMPLETED`  
> ℹ️ Category values: `WORK`, `PERSONAL`, `URGENT`, etc.

---

## 💻 Local Development Setup

### Prerequisites

- Java 11+
- Maven
- Node.js & npm (for frontend)
- Git
- IDE (e.g., IntelliJ, VSCode)

### Steps

1. **Clone the Repository**

   ```
   git clone https://github.com/your-username/Capital_ToDoListApp.git
   cd Capital_ToDoListApp
   ```

2. **Backend Setup**

```
cd backend
mvn clean install
mvn spring-boot:run
Runs at http://localhost:8080
```

3. **Frontend Setup**

```
cd frontend
npm install
npm start
Runs at http://localhost:3000
```

🚀 Deployment Steps
```
cd backend
mvn clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

📁 Project Structure (Backend)
```
com.taskmanager
├── controller
│   └── TaskController.java
├── model
│   └── Task.java
├── repository
│   └── TaskRepository.java
├── service
│   └── TaskService.java
├── CapitalToDoListAppApplication.java
└── test
    └── TaskControllerTest.java
```

📌 Notes
All API responses follow proper HTTP status codes.

Soft-deleted tasks are excluded from /tasks by default.

The controller uses @CrossOrigin for frontend integration at localhost:3000.