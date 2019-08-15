# Quick Start Guide

## Prerequisites

* [Apache Maven 3.6.0](https://archive.apache.org/dist/maven/maven-3/3.6.0/binaries/)
* [JDK 8](installation-help-guide.md#install-jdk-8)
* [GIT 2.18.0 or higher](installation-help-guide.md#install-git)
* IDE ([Spring Tool Suite 4:STS4](https://spring.io/tools) is recommended as it plays nicely with Spring Boot. However you are free to choose.)
* Docker [for MAC](https://docs.docker.com/docker-for-mac/install/) or [Windows](https://docs.docker.com/docker-for-windows/install/) (to run BIP reference person service in the container)
* Create a GitHub [Personal Access Token](#creating-personal-access-token-to-connect-to-github) to connect to GitHub
* [Fortify 19.1.0](installation-help-guide.md#install-and-run-fortify) SCA and maven plugins provided in the download
* [SonarQube is installed as a docker image](#running-sonar) when you build a BIP service project
* See [How to connect Maven with Nexus using HTTPS](installation-help-guide.md#how-to-connect-maven-with-nexus-using-https)

## Docker images

## To Use

* From your command line:
```bash
   git clone https://github.com/department-of-veterans-affairs/bip-reference-person
```
* Alternately, you can also clone the repository from IDE using URL `https://github.com/department-of-veterans-affairs/bip-reference-person`
* Change directory to `bip-reference-person`
* If Docker is RUNNING, run `mvn clean install` from the reactor POM to build the project which will create the docker image for bip-reference-person.
* If Docker is UNAVAILABLE/OFFLINE, run `mvn clean install -Ddockerfile.skip=true` from the reactor POM to build the project and *skip* creating the docker images. Don't use this option if you are planning to run `local-int` (see below) as it would require docker image(s) to run in docker container.

## Build and Test

#### Application Team Developers: ###

There are 2 application profiles that you could run locally

* default (Stand alone mode)
* local-int (Docker Engine Integrated Environment)


1. To run with default profile mode, clone only the `bip-reference-spring-boot` repository, then run `bip-reference-person` service from your IDE using [Deploy Only Mode](#ide-deploy-only-bip-reference-person)
1. To run local-int profile mode, go through the steps mentioned under [Using Docker Compose](#using-docker-compose)

### IDE Deploy only bip-reference-person
* Assuming you are using Spring Tool Suite as suggested
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

### Using Docker Compose

##### Running the services in local-int profile

* Ensure that the Docker is running and that you have run `mvn clean install` from the reactor POM
* Verify that the `docker-compose.yml` exists in the root directory of bip-reference-person
* To start all the containers, execute the `[path]/bip-reference-person/start-all.sh` command under the root directory, which will bring up all the docker containers.
* Localhost URLs for testing/using this deployment approach (you must start the spring boot application!)

  [Swagger UI](http://localhost:8080/swagger-ui.html) (http://localhost:8080/swagger-ui.html)

  [Consul](http://localhost:8500) (http://localhost:8500) - Master ACL token is `7652ba4c-0f6e-8e75-5724-5e083d72cfe4`

  [Vault](http://localhost:8200) (http://localhost:8200) - Root token is `vaultroot`

  [Prometheus](http://localhos:9090) (http://localhos:9090)

  [Grafana](http://localhost:3000) (http://localhost:3000) - Username/Password is `admin/admin` by default

	[Sonar](http://localhost:9000) (http://localhost:9000)

  *Note there are other URLs, such as all the actuator URLs.  Listed here are the basic minimum URLs.*

## Running Sonar

A SonarQube docker image is added to your docker environment when you run a maven build without specifying to skip the docker build.

* Start the docker container for sonar with `[path]/bip-reference-person/start-all.sh`, or individually with this command:
	```bash
	$ docker-compose -f [path]/bip-reference-person/local-dev/sonar/docker-compose.yml up --build -d
	```

* Wait for the container to spin up. It will respond on http://localhost:9000 once it is is running.

* Run a sonar scan on your project(s)
	```bash
	$ mvn clean install # must build the project and run unit tests first
	$ mvn sonar:sonar
	```

* View the results at http://localhost:9000

* Stop SonarQube with `[path]/bip-reference-person/stop-all.sh`, or individually with this command:
	```bash
	$ docker-compose -f [path]/bip-reference-person/local-dev/sonar/docker-compose.yml down --rmi all -v
	```

## Creating Personal Access Token to connect to GitHub

Creating Personal Access Token is required to perform GIT operations using HTTPS for any private repositories

Execute the steps below only if you haven't already performed these steps.

* Developers using Windows, Mac and Linux:

  * Creating a Personal Token
    1. [Verify your email address](https://help.github.com/articles/verifying-your-email-address/), if it hasn't been verified yet on GitHub.
    2. In the upper-right corner of any page, click your profile photo, then click Settings.
    3. In the left sidebar, click Personal access tokens.
    4. Click Generate new token.
    5. Give your token a descriptive name.
    6. Select the scopes, or permissions, you'd like to grant this token. To use your token to access repositories from the command line, select repository.
    7. Click Generate token.
    8. Copy the token to your clipboard. For security reasons, after you navigate off the page, you will not be able to see the token again.

    **Warning**: Treat your tokens like passwords and keep them secret. When working with the API, use tokens as environment variables instead of hard-coding them into your programs.

  * Caching your GitHub password in Git
    1. If you're cloning GitHub repositories using HTTPS, you can use a credential helper to tell Git to remember your GitHub username and password every time it talks to GitHub.
    2. Follow the steps mentioned [here](https://help.github.com/en/articles/caching-your-github-password-in-git)

  * You could also refer to Instructions on GitHub to [Create Personal Token](https://help.github.com/articles/creating-a-personal-access-token-for-the-command-line/)
