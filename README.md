
## What is this repository for? ##

This is a suite of projects to demo various patterns required to deploy and run application services on to the new BIP Platform.  

## Project Breakdown ##

1. os-reference-spring-boot-parentpom This project is the parent pom for spring boot reference service. This supplies common Maven configuration specific to the os-reference-spring-boot suite of projects.

1. os-reference-spring-boot-partner: Partner services for Demo, showing BGS for sample mock data

2. os-reference-spring-boot-service-inttest: Contains the integration tests using RestAssured, Cucumber libraries. Includes Test cases against the end points for ascent demo and claims demo. 

3. os-reference-spring-boot-service-perftest: Contains the performance JMX tests scripts for Apache JMeter

4. os-reference-spring-boot-service: Demo for the core service.  It has REST endpoints and demo's various patterns for producing REST endpoints, swagger for the application, registering the application with Eureka, calling REST endpoints through Zuul, Hystrix Circuit Breaker etc.  

5. os-reference-spring-boot-service-2: Second demo service to show FEIGN client service to service call implementation

## Contribution guidelines ## 
* If you or your team wants to contribute to this repository, then fork the repository and follow the steps to create a PR for our upstream repo to review and commit the changes
* [Creating a pull request from a fork](https://help.github.com/articles/creating-a-pull-request-from-a-fork/)
	
