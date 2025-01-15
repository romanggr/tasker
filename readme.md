# Task Management Backend

 This is a backend application for task management, built 
 using **Spring Boot** and **MongoDB**. The application provides APIs for managing 
 tasks, teams, and users, with custom authentication implemented using **JWT**. 
 The project includes comprehensive unit and integration tests and is containerized using
 Docker for easy running.

## Features
### Task Management:
- Create, update, delete, and retrieve tasks.
- Assign tasks to team members.
- Track task status and priority.

### Team Management:
- Manage teams and their members.
- Retrieve tasks associated with a team.

### Database:
- MongoDB for flexible and scalable data storage.

### Authentication:
- User authentication and authorization using JWT.
- Secure endpoints accessible only to authenticated users.

### Testing:
- Unit tests for individual components.
- Integration tests for end-to-end API functionality.

### Docker Support:
- Fully containerized application with a Dockerfile and Docker Compose for easy setup.

## Technologies Used
- Java: Core programming language.
- Spring Boot: Framework for building RESTful APIs.
- MongoDB: NoSQL database for storing application data.
- JWT: For secure and stateless authentication.
- JUnit: For unit testing.
- TestContainer: For integration testing.
- Docker: For containerization.

## Getting Started
### Prerequisites
- Java 17 or higher
- Maven
- Docker
- MongoDB

### Local Setup
1. Clone the repository:
```bash
git clone https://github.com/romanggr/tasker.git
cd tasker
```
2. Build and Start the Application
```bash
docker-compose up --build
```
3. Stop the Application
```bash
docker-compose down
```

### Tests running
1. All tests:
```bash
mvn test
```
2. Only integration tests:
```bash
 ./mvnw test -Dtest=**/integration/**
```
3. Only unit tests:
```bash
./mvnw test -Dtest=**/unit/** 
```