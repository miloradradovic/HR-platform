# Coding task for INTENS student internship
  - HR platform for adding and monitoring job candidates and their skills in order to facilitate HR processes within a company.

# Application structure
  - Spring Boot on the server side
      - Port: 8080
      - Needs to be built before starting by using the command mvn clean compile install. After that, run it like a basic Spring Boot app.

  - Angular on the client side
      - Port: 4200
      - Before starting, run npm install when positioned at the location of the ng-app. After that, run ng serve.
  
  - PostgreSQL database
      - Download postgresql from: https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
      - After that, use the preferred tool for database to access it. 
      - Credentials can be found at the application.properties.

# Additional
  - Tests
      - Service and api layer are covered in JUnit and integration tests and they are all listed in SuiteAll. 
      - By starting SuiteAll up all tests will start running.

  - Swagger
      - Integrated into Spring Boot application.
      - Can be found at http://localhost:8080/swagger-ui.html while backend is running.
