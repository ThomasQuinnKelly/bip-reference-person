
## What is this repository for? ##

This is a suite of projects to demonstrate various patterns required to deploy and run application spring boot and spring cloud services on the BIP Platform.  

## Project Breakdown ##

1. os-reference-spring-boot-parentpom: This project is the parent POM for spring boot reference service. This supplies common maven configuration specific to the submodules and dependencies for the suite of projects.

1. os-reference-spring-boot-partner: Partner services for reference, showing BGS with sample mock data

1. os-reference-spring-boot-service: Service implementation project.  It has REST endpoints and shows various patterns for producing endpoints, swagger for the application, registering the application with Consul, Secrets from Vault calling REST endpoints through Zuul, Hystrix Circuit Breaker, logging pattern etc.

1. os-reference-spring-boot-shared-autoconfigure: Shared autoconfiguration for the services to enable the patterns for audit, feign, rest, security, swagger and service 

1. os-reference-spring-boot-shared-library: Shared library for the services to provide common framework and security interfaces. 

1. os-reference-spring-boot-service-inttest: Contains the integration tests using RestAssured, Cucumber libraries. Includes Test cases against the end points for ascent demo and claims demo. 

1. os-reference-spring-boot-service-perftest: Contains the performance JMX tests scripts for Apache JMeter

1. os-reference-spring-boot-service-2: Second reference service to show FEIGN REST service to service call implementation

## Contribution guidelines ## 
* If you or your team wants to contribute to this repository, then fork the repository and follow the steps to create a PR for our upstream repo to review and commit the changes
* [Creating a pull request from a fork](https://help.github.com/articles/creating-a-pull-request-from-a-fork/)
	
