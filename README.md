# ⚽Football Manager API⚽

The Football Manager API designed for managing football teams and players. It offers a set of controllers to facilitate basic CRUD operations for both players and teams. When retrieving data, the API provides results in paginated form using pageable queries. The API also supports actions such as assigning players to teams and executing player transfers between teams, with each team having its allocated budget.

# Features

* `CRUD Operations:` Perform Create, Read, Update, and Delete operations on players and teams
* `Pagination:` Retrieve data in paginated sets using pageable queries
* `Player Management:` Attach players to teams and execute player transfers
* `Team Budgets:` Manage individual team budgets for transfer activities
* `Initial Data:` The database is preloaded with initial data using Liquibase
* `Docker Compatibility:` The application is containerized and can be launched using Docker
* `Controller and Service Layer Tests:` The project includes comprehensive tests for both the controller and service layers to ensure functionality and reliability.


# Getting Started

* Copy the repository to your local machine.
* Build the project using the command: `mvn clean package`.
* Launch the `Docker` on your device.
* Run the command: `docker-compose up` (It is necessary to allow time for the MySQL container to fully start, because of which the container with the application may restart several times with an error).
* You can explore all endpoints conveniently using the integrated Swagger UI at http://localhost:6868/swagger-ui/index.html.
* If you are not going to run the application in a Docker container, you must enter credentials in `resources\application.properties` to connect your MySQL database and change the port in the URL from `6868` to `8080`


# Structure

* `controller` - Rest controllers
* `dto` - Data transfer objects for request and response, includes mapper-s for dto
* `model` - Classes which describe objects as `Player` and `Team` 
* `repository` - Classes that contain methods that work with the repository
* `service` - Provides business logic
* `validation` - Classes that contain validations
* `resources` - Contains scripts, database changelog, and properties

# Used Technologies

* Java `17`
* Spring Boot `3.1.2` (Data JPA, Web, Validation)
* SpringDoc `2.2.0`
* MySQL Connector `8.0.33`
* Project Lombok `1.18.28`
* MapStruct `1.5.5`
* Liquibase `4.23.0`
* Maven `3.8.7`
* JUnit `4.13.2`
* Mockito `5.5.0`


## Authors

[Déschna (Ditkovskyi Pasha)](https://github.com/Deschna)