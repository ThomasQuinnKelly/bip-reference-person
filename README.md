
## What is this repository for? ##

This is a suite of projects to demonstrate various patterns required to deploy and run application spring boot and spring cloud services on the BIP Platform.  

## Project Breakdown ##

1. ocp-reference-autoconfigure: Shared autoconfiguration for the services to enable the patterns for audit, feign, rest, security, swagger and service 

1. ocp-reference-libraries: Shared libraries for the services to provide common framework and security interfaces. 

1. ocp-reference-parentpom: This project is the parent POM for spring boot reference service. This supplies common maven configuration specific to the submodules and dependencies for the suite of projects.

1. ocp-reference-partner: Partner services for reference, showing BGS with sample mock data

1. ocp-reference-person: Service implementation project.  It has REST endpoints and shows various patterns for producing endpoints, swagger for the application, registering the application with Consul, Secrets from Vault calling REST endpoints through Zuul, Hystrix Circuit Breaker, logging pattern etc.

1. ocp-reference-inttest: Contains the integration tests using RestAssured, Cucumber libraries. Includes Test cases against the end points for ascent demo and claims demo. 

1. ocp-reference-perftest: Contains the performance JMX tests scripts for Apache JMeter

## Core Concepts
* Service Discovery
* [Secrets Management](docs/secrets.md)
* [Configuration Management](docs/config-management.md)

## Contribution guidelines ## 
* If you or your team wants to contribute to this repository, then fork the repository and follow the steps to create a PR for our upstream repo to review and commit the changes
* [Creating a pull request from a fork](https://help.github.com/articles/creating-a-pull-request-from-a-fork/)

## Local Development
Instructions on running the application locall can be found [here].(local-dev)
	
