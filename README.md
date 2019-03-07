
## What is this repository for? ##

This is a suite of projects to demonstrate various patterns required to deploy and run application spring boot and spring cloud services on the BIP Platform.  

## Project Breakdown ##

1. ocp-reference-partner: Partner services for reference, showing BGS with sample mock data

1. ocp-reference-person: Service implementation project.  It has REST endpoints and shows various patterns for producing endpoints, swagger for the application, registering the application with Consul, Secrets from Vault calling REST endpoints through Zuul, Hystrix Circuit Breaker, logging pattern etc.

1. ocp-reference-inttest: Contains the integration tests using RestAssured, Cucumber libraries. Includes Test cases against the end points for ascent demo and claims demo. 

1. ocp-reference-perftest: Contains the performance JMX tests scripts for Apache JMeter

## How to include these dependency libraries in your project ##

In this repository, projects are dependent on libraries from OCP framework.

1. ocp-framework-autoconfigure: Shared auto-configuration for the services to enable the patterns for audit, cache, feign, rest, security, swagger, service, vault etc

1. ocp-framework-libraries: Shared libraries for the services to provide common framework and security interfaces. 

1. ocp-framework-parentpom: Parent POM for spring boot and cloud enabled services. It provides common Maven configuration and dependencies for the suite of projects.

1. ocp-framework-test-lib: Test library framework to support functional testing for the services

There are 2 options to download these libraries on your workstation for the projects to compile and build.

**OPTION 1**
1. Clone the `ocp-framework` repository
   
    git clone https://github.com/department-of-veterans-affairs/ocp-framework.git
    
1. Navigate to the folder `ocp-framework` and run `mvn clean install` command

**OPTION 2**
A section has already been added in the reactor (root) pom.xml of this repository. See example: https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/blob/master/pom.xml

pom.xml

	<distributionManagement>
	    <repository>
	        <id>github</id>
	        <name>GitHub Repository</name>
	        <url>https://raw.github.com/department-of-veterans-affairs/ocp-framework/mvn-repo</url>
	    </repository>
	</distributionManagement>

You must then update your local ~/.m2/settings.xml as shown below

	<settings>
	  <servers>
	    <server>
	      <id>github</id>
	      <username>{{GitHub User Name}}</username>
	      <password>{{Personal Access Token}}</password>
	      <configuration>
        	<httpHeaders>
	          	<property>
	            	<name>Authorization</name>
	            	<!-- Base64-encoded username:access_token -->
	            	<!-- Example site to generate https://codebeautify.org/base64-encode -->
	            	<value>Basic {{base64 encoded content}}</value>
	          	</property>
        	</httpHeaders>
      </configuration>
	    </server>
	  </servers>
	</settings>

## Core Concepts
* Service Discovery
* [Secrets Management](docs/secrets.md)
* [Configuration Management](docs/config-management.md)
* [Deployment Packaging](docs/deployment-package.md)

## Contribution guidelines ## 
* If you or your team wants to contribute to this repository, then fork the repository and follow the steps to create a PR for our upstream repo to review and commit the changes
* [Creating a pull request from a fork](https://help.github.com/articles/creating-a-pull-request-from-a-fork/)

## Local Development
Instructions on running the application locall can be found [here].(local-dev)
	