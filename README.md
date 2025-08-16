# Blood-Link🩸

![Java](https://img.shields.io/badge/Java-21-blue?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring)
![Maven](https://img.shields.io/badge/Maven-4.0.0-C71A36?style=for-the-badge&logo=apache-maven)
![Docker](https://img.shields.io/badge/Docker-Build-2496ED?style=for-the-badge&logo=docker)

Blood-Link is a modern, full-featured web application designed to streamline the operations of a blood bank. It connects donors, recipients, and administrators on a single platform, making the process of blood donation and management efficient, transparent, and user-friendly.



***

## ✨ Key Features

The application is built with a clear separation of roles and functionalities, catering to all stakeholders in the blood donation ecosystem.

### 👤 For Donors
* **Secure Registration & Login**: Easy and secure account creation with email verification.
* **Personalized Dashboard**: A dedicated space to manage personal information and view donation history.
* **Profile Management**: Donors can easily update their personal and medical details.
* **Donation History**: Track all past donations in one place.
* **Blood Camp Locator**: Find and view details of nearby blood donation camps.
* **Blood Requests**: View active blood requests.

### 👑 For Admins
* **Comprehensive Admin Dashboard**: An overview of all system activities.
* **Donor Management**: View, update, and manage all registered donors.
* **Inventory Control**: Real-time management of blood stock, with details on blood type, quantity, and expiry dates.
* **Blood Request Management**: Approve and manage incoming blood requests from patients or hospitals.
* **Camp Management**: Create, update, and manage blood donation camp events.
* **Record Donations**: Manually record new donations into the system.

### ⚙️ General Features
* **Role-Based Access Control**: Secure distinction between `DONOR` and `ADMIN` roles using Spring Security.
* **Password Reset**: Secure "Forgot Password" functionality via email.
* **Donor Search**: A public feature to search for available donors by blood group and location.
* **Responsive UI**: A clean and modern user interface built with Thymeleaf and CSS.



***

## 🛠️ Tech Stack & Architecture

This project is built using the Model-View-Controller (MVC) architectural pattern to ensure a clean separation of concerns.

* **Backend**: **Java 21**, **Spring Boot 3**
* **Data Persistence**: **Spring Data JPA** (Hibernate)
* **Security**: **Spring Security 6** for authentication and authorization.
* **Frontend**: **Thymeleaf**, HTML, CSS, JavaScript
* **Build & Dependency Management**: **Apache Maven**
* **Containerization**: **Docker**
* **Email Service**: **Spring Boot Mail** for sending transactional emails.

***

## 🚀 Getting Started

Follow these instructions to get a local copy of the project up and running for development and testing purposes.

### Prerequisites

* **JDK 21** or later
* **Apache Maven**
* **Git**
* An IDE like IntelliJ IDEA or VS Code
* **Docker** (Optional, for containerized deployment)

## 1. Clone the Repository

```bash
git clone [https://github.com/grvmehto-18/blood-link.git](https://github.com/grvmehto-18/blood-link.git)
cd grvmehto-18-blood-link
```
## 2. Configure application.properties

### Database Configuration
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/blood_bank_db
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Email Configuration
```bash
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password # Use an App Password for Gmail
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## 3. Build and Run

### Build the project and run tests 
```bash
mvn clean install
```

### Run the application ###
```bash
mvn spring-boot:run
```

### Docker

### Build the Docker image
```bash
docker build -t blood-link .
```

* Run the application in a Docker container 
* Make sure to pass your application properties as environment variables
```bash
docker run -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://<your-db-host>:3306/blood_bank_db \
  -e SPRING_DATASOURCE_USERNAME=<your-db-user> \
  -e SPRING_DATASource_PASSWORD=<your-db-password> \
  -e SPRING_MAIL_USERNAME=<your-email> \
  -e SPRING_MAIL_PASSWORD=<your-password> \
  blood-link
```
