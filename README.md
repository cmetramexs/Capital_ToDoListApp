# Capital_ToDoListApp
Capital Group Take-Home Assignment - Solution Engineering: To Do List App

# Task
You are tasked with developing a user-friendly web application that helps users manage their daily tasks. The purpose of this application is to allow users to create, view, and organize their to-do lists efficiently. The goal is to provide users with a simple and
intuitive interface to keep track of their tasks.

### Requirements
Provide a central area where users can manage their tasks.

### To-Do list page details
- Display the user’s tasks.
- Allows the user to add, edit, or delete tasks and sub-tasks as needed.
- Implement a search or filter functionality to easily find tasks.
- Allow users to mark tasks as completed or pending.
- Enable users to set due dates and priorities for tasks.
- Provide a way to categorize tasks (e.g., work, personal, urgent).
- Provide an option to restore deleted tasks.

### Additional Considerations
- User Experience: Ensure the application is user-friendly and responsive.
- Documentation: Provide clear documentation on how to set up and run the
application.
- Testing: Include unit tests and integration tests to ensure the application functions
correctly.

## Submission
### Technology Stack
### Features
**API Endpoints**
| HTTP Method | Endpoint                   | Description                          |
|-------------|----------------------------|--------------------------------------|
| GET         | `/api/tasks`               | Get all tasks                        |
| GET         | `/api/tasks/{id}`          | Get task by ID                       |
| POST        | `/api/tasks`               | Create new task                      |
| PUT         | `/api/tasks/{id}`          | Update task                          |
| DELETE      | `/api/tasks/{id}`          | Delete task (soft delete)            |
| GET         | `/api/tasks/deleted`       | Get deleted tasks                    |
| PUT         | `/api/tasks/{id}/restore`  | Restore deleted task                 |
### Local Development Setup
### Deployment Steps