# Quick Start Guide

## Prerequisites

* Apache Maven 3.6.0
* JDK 8
* GIT 2.18.0 or higher
* IDE (Spring Tool Suite:STS is recommended as it plays nicely with Spring Boot. However you are free to choose.)
* Optionally Docker (To run BIP reference person service in the container)
* Personal Access Token to connect to GitHub: [Creating Personal Access Token](#creating-personal-access-token-to-connect-to-github)

## Installation Reference Links
* [Apache Maven 3.5.4 Download Link](https://archive.apache.org/dist/maven/maven-3/3.5.4/binaries/)
* [Install JDK 8 on Mac OS X](installation-help-guide.md#install-jdk-8-on-a-mac)
* [Installing Git on a Mac](installation-help-guide.md#installing-git-on-a-mac)
* [STS Download Site](https://spring.io/tools3/sts/all)
* [Docker Installation for MAC](https://docs.docker.com/docker-for-mac/install/)

## To Use

To clone and run this repository you'll need Git installed on your computer. 

* From your command line: `git clone https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot`
* Alternately, you can also clone the repository from IDE using URL `https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot`
* Change directory to "ocp-reference-spring-boot"
* If Docker is RUNNING, run `mvn clean install` from the reactor POM to build the project which will create the docker image for bip-reference-person. 
* If Docker is UNAVAILABLE/OFFLINE, run `mvn clean install -Ddockerfile.skip=true` from the reactor POM to build the project. `-Ddockerfile.skip=true` will *SKIP* the docker image(s) creation. Don't use this option if you are planning to run `local-int` mode as it would require docker image(s) to run in docker container.

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
* URLs for testing/using this deployment approach

  [BIP Reference Person Swagger](http://localhost:8080/swagger-ui.html)

### Using Docker Compose

##### Running the services in local-int profile

* Ensure that the Docker is running and that you have run `mvn clean install` from the reactor POM
* Verify that the `docker-compose.yml` exists in the root directory of ocp-reference-spring-boot
* To start all the containers, execute the `./start-all.sh` command under the root directory, which will bring up all the docker containers. 
* URLs for testing/using this deployment approach

  [BIP Reference Person Swagger](http://localhost:8080/swagger-ui.html)

  [Consul](http://localhost:8500) - Master ACL token is `7652ba4c-0f6e-8e75-5724-5e083d72cfe4`

  [Vault](http://localhost:8200) - Root token is `vaultroot`

  [Prometheus](http://localhos:9090)

  [Grafana](http://localhost:3000) - Username/Password is `admin/admin` by default

  *Note there are other URLs, such as all the actuator URLs.  Listed here are the basic minimum URLs.*

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


  
