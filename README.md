# Booking System

This is a backend system for managing bookings, schedules, and services. It provides RESTful APIs for user authentication, booking management, schedule management, and service management. The system is built using Spring Boot and integrates with a MySQL database.

## Features

- **User Authentication**: Register, login, and refresh tokens using JWT.
- **Booking Management**: Create, cancel, and confirm bookings.
- **Schedule Management**: Manage schedules and available time slots for services.
- **Service Management**: Add, update, and delete services.
- **Role-Based Access Control**: Admins can manage services and schedules, while users can book services.

## Technologies Used

- **Spring Boot**: Backend framework.
- **Spring Security**: For authentication and authorization.
- **JWT**: JSON Web Tokens for secure authentication.
- **MySQL**: Database for storing application data.
- **Lombok**: For reducing boilerplate code.
- **ModelMapper**: For mapping entities to DTOs.
- **Swagger**: API documentation.

## Setup Instructions

### Prerequisites

- Java 17 or higher
- MySQL database
- Maven

### Steps to Run the Project

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/booking-system.git
   cd booking-system
