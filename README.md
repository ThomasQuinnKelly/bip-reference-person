## What is this repository for?

This is a suite of projects to demonstrate various patterns required to deploy and run application spring boot and spring cloud services on the BIP Platform.  

## Project Breakdown

1. [bip-reference-partner-person](https://github.ec.va.gov/EPMO/bip-reference-person/tree/master/bip-reference-partner-person): Partner service for reference person, showing sample mock data for BGS. It should be noted by service designers and tech leads that Partner projects would typically be stand-alone (in their own repo, and not part of a reactor project). The intent is for Partner projects to be freely available for use by any number of service applications that might need them by including them as a maven dependency.
2. [bip-reference-person](https://github.ec.va.gov/EPMO/bip-reference-person/tree/master/bip-reference-person): Service implementation project. This project demonstrates the recommended design patterns, configuration pointers, and coding examples. It shows how to produce a documented endpoint, how to register the app with Consul, how to use secrets from Vault, how to implement a Hystrix circuit breaker, how to get and use loggers, etc. The design consists of three layers:
	- The Provider (or "web") layer contains the REST endpoints and model, JSR 303 annotations in the resource class and the model, and the use of an adapter class to transform models and call the service interface.
	- The Domain (or "service") layer contains examples of business validation, business logic,  to call Partner services and process the returned data, and exception handling.
	- The Partner (or "client") layer shows how to perform model transformation, how to call a partner client interface, and how to handle responses (or exceptions thrown) from the partner client.
3. [bip-reference-inttest](https://github.ec.va.gov/EPMO/bip-reference-person/tree/master/bip-reference-inttest): Contains the integration tests using the framework Test Library ()Spring Rest Template, Cucumber libraries, and other capabilities). It includes functioning test cases that run against the endpoints in `bip-reference-person`. 
4. [bip-reference-perftest](https://github.ec.va.gov/EPMO/bip-reference-person/tree/master/bip-reference-perftest): Contains functioning performance JMX test scripts for Apache JMeter that run against the endpoints in `bip-reference-person`. 

## How to include the framework libraries in your project

The projects in this repository are dependent on the libraries from [BIP framework](https://github.ec.va.gov/EPMO/bip-framework) and [BIP framework test library](https://github.ec.va.gov/EPMO/bip-framework/tree/master/bip-framework-test-lib) for  auto configuration, common shared libraries, parent pom maven configuration and test libary. These libraries can be included as shown below.

```xml
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
```

To make these libraries available for compilation, read [this section](#how-to-make-the-dependency-framework-libraries-available).

## How to build and test?

The fastest way to get set up is to visit the [Quick Start Guide](docs/quick-start-guide.md).

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

To make the framework libraries available locally for the service projects to compile and build, there are 3 options.

#### Option 1 - Clone the libraries from GitHub
This option downloads the bip-framework code to your local workstation to be built.

1. Clone the BIP framework repository `git clone https://github.ec.va.gov/EPMO/bip-framework.git`
2. Navigate to the folder `bip-framework` and run the `mvn clean install` command. This builds all the libraries with versions as configured in `pom.xml` files.

#### OPTION 2 - Retrieve the libraries from the VA Nexus repo
This option requires that your workstation is connected to the VA network. It allows maven to retrieve the libraries from nexus, so you don't need to build them.

Add the repository definition to the `pom.xml` file in your reactor (root) project. Note that the URL provided in this snippet is the correct base URL for the VA nexus repository used for BIP Framework.

```xml
	<repositories>
		<repository>
			<id>nexus3</id>
			<name>BIP Nexus Repository</name>
			<url>https://nexus.dev.bip.va.gov/repository/maven-public</url>
		</repository>
	</repositories>
```

#### OPTION 3 - Retrieve the librarties from a GitHub repository
This is a temporary work-around for those who are not connected to the VA network. In this case, GitHub is co-opted to act as a repository for the framework libraries.

There are two steps to make this work.

1. Add the below section in the reactor (root) `pom.xml` of your service project. A complete and functioning example can be seen in the [bip-reference-reactor pom.xml](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/pom.xml).

```xml
	<repositories>
		<repository>
			<id>github</id>
			<name>GitHub Repository</name>
			<url>https://raw.github.com/department-of-veterans-affairs/bip-framework/mvn-repo</url>
		</repository>
	</repositories>
```

2. Update your local `~/.m2/settings.xml` as shown below. Replace values between `{{Text}}` with your personal GitHub information

```xml
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
	            	<value>Basic {{base64-encoded-token}}</value>
	          	</property>
        	</httpHeaders>
          </configuration>
	    </server>
	  </servers>
	</settings>
```

## Contribution guidelines
If you or your team wants to contribute to this repository, then fork the repository and follow the steps to create a PR for our upstream repo to review and commit the changes:
[Creating a pull request from a fork](https://help.github.com/articles/creating-a-pull-request-from-a-fork/)

## Local Development
Instructions on running the application on a local workstation can be found in the [local-dev README](local-dev)
	
