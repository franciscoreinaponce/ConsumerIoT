# Technical Test using Spring Boot 2.5 and JUnit 5 compiling with Java 8

## How to install / run the app

### Install the necessary applications

Download JDK 8: https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html  
Configure JDK: https://docs.oracle.com/cd/E19182-01/820-7851/inst_cli_jdk_javahome_t/index.html  
Download Maven: https://maven.apache.org/download.cgi  
Configure Maven: https://maven.apache.org/install.html

### Run the ConsumerIoT app

Clone the *ConsumerIoT* repository which contains de project.  

Once you have configured the JDK and Maven, run the following commands to:

*mvn install* => Downloads all the dependencies found in pom.xml, compiles the project and also ***runs*** the tests.  
*mvn install -DskipTests* => Same as the previous command ***without running*** the tests  
*mvn test* => Run only the tests  
*mvn spring-boot:start* => Run Spring Boot app  
*mvn spring-boot:stop* => Stop Spring Boot app  

## How to test the app

### Unit and Integration tests

The project has unit and integration tests which can be executed by means of the above mentioned command *mvn test*.  

Currently, the project has a coverage of +90% with unit tests which increases to +95% with integration tests.

<code>*</code> 
I would like to mention that some test has been implemented on some private method to demonstrate 
the handling of the Powermock library, knowing in advance that covering private methods would be a not good practice.

### Postman

In order to test the api, I have provided a Postman collection (*ConsumerIoT.postman_collection.json*),
which contains the existing endpoints and examples of calls for the different scenarios.

Download Postman: https://www.postman.com/downloads/

## Assumptions

It is assumed that the only optional fields in the CSV file are "Latitude" and "Longitude".

## Improvements
- Add Swagger

### Current stack

- Java 8
- Spring Boot 2.5
- Lombok 1.18  
- JUnit 5.7
- Mockito 3.11
- Powermock 2.0
- OpenCSV 5.4
- Maven 3
