# Quick Start Guide

## Setting up your development environment

### Prerequisites

* [Apache Maven 3.6.0](https://archive.apache.org/dist/maven/maven-3/3.6.0/binaries/)
* [JDK 8](installation-help-guide.md#install-jdk-8)
* [GIT 2.18.0 or higher](installation-help-guide.md#install-git)
* IDE [Spring Tool Suite 4:STS4](https://spring.io/tools) or [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) is recommended.

### Nice to have

* Docker [for MAC](https://docs.docker.com/docker-for-mac/install/) or [Windows](https://docs.docker.com/docker-for-windows/install/) to run BIP reference person service in the container
* [SonarQube is installed as a docker image](installation-help-guide.md#running-sonar) when you build a BIP service project
* [Fortify 19.1.0](installation-help-guide.md#install-and-run-fortify) SCA and maven plugins provided in the download
* Create a GitHub [Personal Access Token](installation-help-guide.md#creating-personal-access-token-to-connect-to-github) to connect to GitHub
* [How to connect Maven with Nexus using HTTPS](installation-help-guide.md#how-to-connect-maven-with-nexus-using-https)

### Docker Images

#### To Use

* From your command line:
```bash
   git clone https://github.ec.va.gov/EPMO/bip-reference-person
```
* Alternately, you can also clone the repository from IDE using URL `https://github.ec.va.gov/EPMO/bip-reference-person`
* Change directory to `bip-reference-person`
* If Docker is RUNNING, run `mvn clean install` from the reactor POM to build the project which will create the docker image for bip-reference-person.
* If Docker is UNAVAILABLE/OFFLINE, run `mvn clean install -Ddockerfile.skip=true` from the reactor POM to build the project and *skip* creating the docker images. Don't use this option if you are planning to run `local-int` (see below) as it would require docker image(s) to run in docker container.

### Build and Test

#### Application Team Developers: ###

There are 2 application profiles that you could run locally

* default (Stand alone mode)
* local-int (Docker Engine Integrated Environment)


1. To run with default profile mode, clone only the `bip-reference-person` repository, then run `bip-reference-person` service from your IDE using `IDE Deploy only bip-reference-person` sections
1. To run local-int profile mode, go through the steps mentioned under [Using Docker Compose](#using-docker-compose)

##### Spring Tool Suite - IDE Deploy only bip-reference-person
* Ensure you've imported the projects in the IDE
* In the "Boot Dashboard" within Spring Tool Suite, highlight `bip-reference-person` project and click the "*(Re)start*" button *
* Localhost URLs for testing/using this deployment approach

  [Swagger UI](http://localhost:8080/swagger-ui.html) (http://localhost:8080/swagger-ui.html)

  - After updating to BIP Framework 2.x, there may be initial build issues including the fact that Swagger UI isn't available. Mitigation for this:

      - Force maven to update libraries:
			- In STS, right-click your reactor project and select _Maven > Update Project... > Force Update of Snapshots/Releases_.
			- At command line, add `-U`, for example: `$ mvn clean install -U`

      - Clean and build the project, select _Project > Clean..._
	- It may be necessary to run the clean/build more than one time for necessary artifacts to get generated correctly.

##### IntelliJ - IDE Deploy only bip-reference-person
* Open IntelliJ and go through the wizard to import the `bip-reference-person` project using maven with the following steps:
    - import project > `bip-reference-person` > import project from external model > maven..next > next (keep default settings) > next > ensure project snapshot is selected…next > next > finish
* In the project tab, highlight `bip-reference-person` project and right click and select `synchronize bip-reference-person`
* Localhost URLs for testing/using this deployment approach

  [Swagger UI](http://localhost:8080/swagger-ui.html) (http://localhost:8080/swagger-ui.html)

  - After updating to BIP Framework 2.x, there may be initial build issues including the fact that Swagger UI isn't available. Mitigation for this:

      - Force maven to update libraries:
			- In IntelliJ, highlight `bip-reference-person` project, right click and select _Maven > Reimport_
			- At command line, add `-U`, for example: `$ mvn clean install -U`

      - Clean and build the project, go to the right side of the IDE, select the maven tab and select _bip-reference-person > Lifecycle > Clean > Install_
	- It may be necessary to run the clean/build more than one time for necessary artifacts to get generated correctly.

#### Using Docker Compose

##### Running the services in local-int profile

* Ensure that the Docker is running and that you have run `mvn clean install` from the reactor POM
* Verify that the `docker-compose.yml` exists in the root directory of bip-reference-person
* To start all the containers, execute the `[path]/bip-reference-person/start-all.sh` command under the root directory, which will bring up all the docker containers.
* Localhost URLs for testing/using this deployment approach (you must start the spring boot application!)

  [Swagger UI](http://localhost:8080/swagger-ui.html) (http://localhost:8080/swagger-ui.html)

  [Consul](http://localhost:8500) (http://localhost:8500) - Master ACL token is `7652ba4c-0f6e-8e75-5724-5e083d72cfe4`

  [Vault](http://localhost:8200) (http://localhost:8200) - Root token is `vaultroot`

  [Prometheus](http://localhost:9090) (http://localhost:9090)

  [Grafana](http://localhost:3000) (http://localhost:3000) - Username/Password is `admin/admin` by default

  [Sonar](http://localhost:9000) (http://localhost:9000) - Only available if explicitly set up ([see running sonar](installation-help-guide.md#running-sonar) section of the installation help guide)

  *Note there are other URLs, such as all the [actuator URLs](actuator-management.md).  Listed here are the basic minimum URLs.*
  
## Using BIP API Framework in your application

To run spring boot application and spring cloud enabled services on the BIP Platform, it must adhere to various service patterns. BIP API Framework provides a suite of java libraries, auto configurations, testing libraries and parent POM that must be included as dependencies to enable the patterns.

For information on BIP Framework project, see the [bip-framework README.md](https://github.ec.va.gov/EPMO/bip-framework/blob/master/README.md)

For information on developing using BIP Framework see the [Developing with BIP Framework](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/docs/developing-with-bip-framework.md)

For information on various configuration and patterns see the [Configuration Usage Patterns](https://github.ec.va.gov/EPMO/bip-reference-person#configuration--usage-patterns)

## Creating a new BIP REST API application

BIP framework team provides a service archetype repository that can be used to create the skeleton for a new BIP service project. The newly created artifact will contain a skeleton project with some rudimentary sample data objects demonstrating a simple use case.

For information on creating a new skeleton project, see the [BIP Archetype Service](https://github.ec.va.gov/EPMO/bip-archetype-service)

BIP framework team also provides a configuration archetype repository that can be used to create a new BIP service project external configuration.

For information on creating a new skeleton project external configuration, see the [BIP Archetype Config](https://github.ec.va.gov/EPMO/bip-archetype-config)

## Using BIP Jenkins Library for your application pipeline

BIP framework team provides a repository that contains a reusable Jenkins Library supporting the CI/CD pipelines. To consume the library, it must first be configured in your Jenkins server. After the library is configured and given a unique name, the library can be called from your Jenkinsfile by importing the library, calling the appropriate pipeline for your project, and providing any necessary configuration settings.

For information on the library, see [BIP Jenkins Library](https://github.ec.va.gov/EPMO/bip-jenkins-lib)

For information on the pipeline steps, see [Pipeline Steps README.md](https://github.ec.va.gov/EPMO/bip-jenkins-lib/blob/master/docs/spring-boot-pipelines/README.md#pipeline-steps)

Example usage for Maven Service pipeline, see the [bip-reference-person Jenkinsfile](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/Jenkinsfile)

Example usage for Maven Library pipeline, see the [bip-framework Jenkinsfile](https://github.ec.va.gov/EPMO/bip-framework/blob/master/Jenkinsfile)

## Creating Integration tests in your application

BIP framework team provides `bip-framework-test-lib` library that contains the dependencies and classes to support API Integration Testing. Cucumber tool is integrated to assist with the needs of Acceptance Test Driven Development (ATDD), BDD by capturing the Acceptance criteria and implementing the scenarios with automated tests. It can be easily integrated with Jenkins Continuous Integration (CI) Pipeline, and it displays the Cucumber Reports as well.

For an example, see the [Reference Person IntTest](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/docs/referenceperson-intest.md)

## Creating Performance tests in your application

BIP framework team provides an example module in BIP Reference Person to demonstrate how to set up performance tests configurations using Apache JMeter. It can be easily integrated with Continuous Integration (CI) Pipeline. JMeter also generates performance reports and dashboard is displayed in the Jenkins pipeline.

For an example, see the [Reference Person PerfTest](https://github.ec.va.gov/EPMO/bip-reference-person/tree/master/bip-reference-perftest)

For Capability, Features, Steps to Install Etc, see [Reference Person Performance Test Plan](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/docs/referenceperson-perftest.md)

Archetype Service project also creates a module in Skeleton Project to support Apache JMeter performance tests.

## BIP Platform Intake for Tenants Reference Links

To access the links in this section, you will need a user account on MAX.gov.

- [Tenant Welcome Sheet](https://community.max.gov/display/VAExternal/Tenant+Welcome+Sheet)
- [Accessing BIP Kubernetes Clusters](https://community.max.gov/display/VAExternal/Accessing+BIP+Kubernetes+Clusters)
- [BIP Platform - Tenant Support Q & A](https://community.max.gov/pages/viewpage.action?pageId=1944756335)
- [BIP Platform - Project Intake Worksheet](https://community.max.gov/display/VAExternal/BIP+Platform+-+Project+Intake+Worksheet)
