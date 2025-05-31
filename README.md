# Hotel Management System

A web application for hotel employees to manage reservations. Built with Java Servlets, JSP, PostgreSQL, and Tailwind CSS.

## Features

- Employee login and authentication
- Dashboard to view all reservations
- Add new reservations
- Color-coded reservation status
- Responsive design using Tailwind CSS

## Prerequisites

- Java Development Kit (JDK) 21 or higher
- Apache Tomcat 10.1 or higher
- PostgreSQL 12 or higher
- Maven 3.6 or higher

## Setup Instructions

### 1. Database Setup

1. Create a PostgreSQL database named `hotel_db`:
   ```sql
   CREATE DATABASE hotel_db;
   ```

2. Connect to the database and run the SQL script located at `src/main/resources/database_setup.sql` to create the necessary tables and sample data:
   ```
   psql -U postgres -d hotel_db -f src/main/resources/database_setup.sql
   ```

3. Update the database connection parameters in `src/main/java/com/code/hetelview/util/DatabaseUtil.java` if needed.

### 2. Build and Deploy

1. Build the project using Maven:
   ```
   mvn clean package
   ```

2. Deploy the generated WAR file to Tomcat:
   - Copy the `target/hetel-view-1.0-SNAPSHOT.war` file to Tomcat's `webapps` directory
   - Rename it to `hotel.war` for a cleaner URL (optional)

3. Start Tomcat if it's not already running.

4. Access the application at `http://localhost:8080/hotel` (or `http://localhost:8080/hetel-view-1.0-SNAPSHOT` if you didn't rename the WAR file).

## Default Login Credentials

The database setup script creates the following default users:

1. Administrator:
   - Username: `admin`
   - Password: `admin123`

2. Receptionist:
   - Username: `john`
   - Password: `john123`

3. Manager:
   - Username: `mary`
   - Password: `mary123`

## Project Structure

- `src/main/java/com/code/hetelview/model/`: Model classes
- `src/main/java/com/code/hetelview/dao/`: Data Access Objects
- `src/main/java/com/code/hetelview/servlet/`: Servlet controllers
- `src/main/java/com/code/hetelview/util/`: Utility classes
- `src/main/webapp/`: JSP views and web resources
- `src/main/resources/`: Configuration files and SQL scripts

## Security Note

This is a demonstration application. In a production environment, you should:

- Use password hashing instead of storing plain text passwords
- Implement HTTPS
- Add more robust input validation and error handling
- Implement proper logging
- Add more comprehensive access control based on user roles