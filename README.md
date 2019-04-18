## What is this repository for? ##

This is a suite of projects to demonstrate various patterns required to deploy and run application spring boot and spring cloud services on the BIP Platform.  

## Project Breakdown

1. **bip-reference-partner**: Partner services for reference person, showing sample mock data for BGS
1. **bip-reference-person**: Service implementation project.  It has REST endpoints and shows various patterns for producing endpoints, swagger for the application, registering the application with Consul, Secrets from Vault, Hystrix Circuit Breaker, logging pattern etc.
1. **bip-reference-inttest**: Contains the integration tests using Spring Rest Template, Cucumber libraries. Includes Test cases against the end points for BIP reference person. 
1. **bip-reference-perftest**: Contains the performance JMX tests scripts for Apache JMeter

## How to include the dependency framework libraries in your project

The projects in this repository are dependent on the libraries from [BIP framework](https://github.com/department-of-veterans-affairs/ocp-framework) for  auto configuration, common shared libraries, parent pom maven configuration and test libary. These libraries can be included as shown below.

       <dependency>
         <groupId>gov.va.bip.framework</groupId>
         <artifactId>bip-framework-autoconfigure</artifactId>
         <version><!-- add the appropriate version --></version>
       </dependency>
       <dependency>
         <groupId>gov.va.bip.framework</groupId>
         <artifactId>bip-framework-libraries</artifactId>
         <version><!-- add the appropriate version --></version>
       </dependency>
       <dependency>
         <groupId>gov.va.bip.framework</groupId>
         <artifactId>bip-framework-parentpom</artifactId>
         <version><!-- add the appropriate version --></version>
       </dependency>
       <dependency>
         <groupId>gov.va.bip.framework</groupId>
         <artifactId>bip-framework-test-lib</artifactId>
         <version><!-- add the appropriate version --></version>
       </dependency>

To make these libraries available for compilation, read the [section](#how-to-make-the-dependency-framework-libraries-available)

## How to build and test?

Follow the link to get started. [Quick Start Guide](docs/quick-start-guide.md)

## Application Core Concepts and Patterns
#### Design
* [Layer Separation Design](docs/design-layer-separation.md)

#### Develop
* [Developing with BIP Framework](docs/developing-with-bip-framework.md)
* [BIP Service Application Flow](docs/application-flow.md)

#### Configuration & Usage Patterns
* [Service Discovery](docs/service-discovery-guide.md)
* [Configuration Management](docs/config-management.md)
* [Secrets Management](docs/secrets.md)
* [Swagger Management](docs/swagger-management.md)
* [Security Management](docs/application-security-management.md)
* [Secure Communications](docs/secure-communication.md)
* [Log and Audit Management](docs/log-audit-management.md)
* [Cache Management](docs/cache-management.md)
* [Validation Management](docs/validation.md)
* [Exception Handling](docs/exception-handling.md)
* [Hystrix Circuit Breaker Management](docs/hystrix-management.md)
* [Actuator Management](docs/actuator-management.md)
* [Build Info and Git Properties Plugin](docs/build-and-git-properties.md)
* [Prometheus Grafana Local Dev](docs/prometheus-grafana-setup.md)
* [Deployment Packaging](docs/deployment-package.md)

## How to make the dependency framework libraries available

To make these libraries available locally for the service projects to compile and build, there are 3 options.

**OPTION 1**

1. Clone the BIP framework repository `git clone https://github.com/department-of-veterans-affairs/ocp-framework.git`
1. Navigate to the folder `ocp-framework` and run `mvn clean install` command. This would build all the libraries with versions as configured in pom.xml files.

**OPTION 2**

**If you are on VA network, the framework libraries would be made available from nexus repository with base url: https://nexus.dev.bip.va.gov/repository** You MUST have BIP Nexus url configured in the reactor POM xml file as shown below.
    
	<repositories>
		<repository>
			<id>nexus3</id>
			<name>BIP Nexus Repository</name>
			<url>https://nexus.dev.bip.va.gov/repository/maven-public</url>
		</repository>
	</repositories>
      
**OPTION 3**
**If you are NOT on VA network, a temporary solution in provided where GitHub repository acts as your nexus repository.

Add the below section in the reactor (root) pom.xml of your service project. See example: https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/blob/master/pom.xml
 
	<repositories>
		<repository>
			<id>github</id>
			<name>GitHub Repository</name>
			<url>https://raw.github.com/department-of-veterans-affairs/ocp-framework/mvn-repo</url>
		</repository>
	</repositories>
	
You MUST also update your local ~/.m2/settings.xml as shown below. Replace values between {{Text}} with your information

	<settings>
	  <servers>
	    <server>
	      <id>github</id>
	      <username>{{Your GitHub User Name}}</username>
	      <password>{{Your Personal Access Token}}</password>
	      <configuration>
        	<httpHeaders>
	          	<property>
	            	<name>Authorization</name>
	            	<!--
			For value tag below:
				Step 1: Base64-encode your username and Github access token together
				        in the form: {{username}}:{{access_token}}
					Example: encode the string "myGithubUsername:ab123983245sldfkjsw398r7"
				Step 2: Add the encoded string to the value tag in the form of
					"Basic {{encoded-string}}"
					Example: <value>Basic YXJtaXvB4F5ghTE2OGYwNmExMWM2NDdhYjWExZjQ1N2FhNGJiMjE=</value>
	            	Base64 encoders:
				https://codebeautify.org/base64-encode
				https://www.base64encode.org/
			-->
	            	<value>Basic {{base64 encoded content}}</value>
	          	</property>
        	</httpHeaders>
          </configuration>
	    </server>
	  </servers>
	</settings>

## Contribution guidelines
* If you or your team wants to contribute to this repository, then fork the repository and follow the steps to create a PR for our upstream repo to review and commit the changes
* [Creating a pull request from a fork](https://help.github.com/articles/creating-a-pull-request-from-a-fork/)

## Local Development
Instructions on running the application local can be found [here](local-dev)
	
