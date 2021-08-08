А PROJECT FOR THE DEVELOPMENT OF
SOFTWARE SOLUTIONS FOR PUBLISHING
AND MEDIA COMPANIES.

* The user uploads the XML file via REST API,
The file is parsed and validated, its content goes to the table in the
database,
* The content from the database can be presented by the GUI in the
form of a table with the possibility of sorting, paging, filtering (REST
API prepared is enough to allow "front-end users" to prepare such a
GUI).
  
Application in Spring Boot that will allow you to upload
XML files via REST API, and then validate their correctness (based on XSD)
and parse data from the file in order to save them in the database.



In order to run application fill in data source credentials in application.properties file and run.  

Then you can run application with following command
```
mvnw spring-boot:run
```
Swagger documentation can be found at http://localhost:8080/swagger-ui

To build docker image run following command:
```
mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=test/pamc
```


