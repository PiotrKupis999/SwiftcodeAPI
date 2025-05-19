# SWIFT Code API – Backend Application (Spring Boot)

## Table of Contents
* [General info](#general-info)
* [Features](#features)
* [Technologies](#technologies)
* [Fast installation](#fast-installation)
* [How to run instructions](#how-to-run-instructions)
* [Running Tests](#running-tests)
* [API Endpoints](#api-endpoints)
* [**Testing the API with Postman**](#testing-the-api-with-postman)


<br>


## General Info
**Swift API** is a lightweight and efficient RESTful backend built using **Spring Boot**.

The application parses SWIFT codes from a provided file, stores them in a database, and exposes four endpoints for retrieving and manipulating the data.

<br>


## Features  


-  **SWIFT Code Parsing**: The application parses SWIFT codes from a provided file and organizes them by headquarters and branches.

-  **Database Storage**: Data is stored in a fast, low-latency database for efficient retrieval.

-  **REST API**: Four key endpoints expose the SWIFT code data, allowing for querying, adding, and deleting entries.

-  **Error Handling**: The API gracefully handles edge cases with clear, informative error messages.

-  **Containerized Application**: The application and database are containerized for easy setup and deployment.



<br>

##  Technologies  

- **Programming Language**: Java 21
- **Framework**: Spring Boot  
- **ORM**: Hibernate with JPA  
- **Database**: PostgreSQL
- **Testing**: JUnit, Mockito



<br>

##  Fast installation  

### 1️ Run Docker Engine

### 2️ Run in cmd
```sh
git clone https://github.com/Piotrkupis999/SwiftcodeAPI
cd SwiftcodeAPI
docker-compose up -d
 
```

If you want to see the logs in real-time while the container is running, you can omit the -d flag:

```sh
docker-compose up
 
```


<br>

##  How to run instructions

### 1️ Clone the Repository
```sh
git clone https://github.com/PiotrKupis999/SwiftcodeAPI
cd SwiftcodeAPI
```
### 2️ Install Required Tools
Before proceeding, make sure you have the following installed:

- Git (https://git-scm.com/)
- Maven (https://maven.apache.org/)
- Docker (https://www.docker.com/get-started)
- Java (https://adoptopenjdk.net/)

### 3️ Build the Project
```sh
mvn clean install
```

### 4️ Run the Application with Docker
Now you can start the application using Docker (run Docker Engine before). Run:

```sh
docker-compose up -d
```
This will build the Docker containers and start the PostgreSQL database and the Spring Boot application in the background.

### 5️ Access the API
Now the API is available at:
http://localhost:8080

### 6️ Access the PostgreSQL Database
You can connect to the PostgreSQL database using the following details:

| PostgreSQL           |                        |
|-----------------|------------------------|
| Host: | localhost  |         
| Port:	  | 5432  |         
| Database Name:	  | swift_db          |      
| Username:   	  | postgres    |      
| Password:	    | password |

You can use any PostgreSQL client (e.g., pgAdmin or psql) to connect to the database.

<br>

##  Running Tests
Navigate to the swift-api directory, then:
```sh
mvn test
```

<br>

##  API Endpoints  
###  Task Endpoints  
| Method | Endpoint                                    | Description                                              |
|--------|---------------------------------------------|----------------------------------------------------------|
| GET    | `/v1/swift-codes/{swift-code}`              | Get details of a single SWIFT code                       |
| GET    | `/v1/swift-codes/country/{countryISO2code}` | Get all SWIFT codes with details for a specific country  |
| POST   | `/v1/swift-codes`                           | Add new SWIFT code entries                               |
| DELETE | `/v1/swift-codes/{swift-code}`              | Delete swift-code data                                   |


<br>

##  Testing the API with Postman

This repository includes a Postman collection:  
📁 `postman/SWIFTcodeAPI_voowu.postman_collection.json`

It contains **a total of 25 requests**, organized into folders:

- **Get All Banks By ISO2 Code** – 3 requests (`GET`)
- **Get Bank By SWIFT code** – 5 requests (`GET`)
- **POST - Add new SWIFT** – 6 requests (`POST`)
- **Delete SWIFT code** – 4 requests (`DELETE`)
- **Sequence** – 7 requests in a logical order (add → read → delete → check absence)

### How to Use?

1. Open Postman.
2. Click **Import** and select the file from the `postman/` folder.
3. Ensure the endpoints are pointing to the correct address (default is `localhost:8080`).
4. Run.

  
<br>

<br>
