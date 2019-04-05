## What is this project for? ##

Claims service API provides end points that gives the claims information for a given veteran.

## End Points ##

1) http://localhost:8080/api/v1/claims - This endpoint gives all the claims
2) http://localhost:8080/api/v1/claims/12345 - This endpoint gives the claim details for the claim id provided as path parameter.

## Database Access: ##

1) The app uses in memory H2 database when run in default profile in STS
2) The app uses Oracle database when run in local-int profile in docker

Below jars need to be included to enable jpa and data access

    	<dependency>
		<groupId>com.oracle</groupId>
		<artifactId>ojdbc7</artifactId>
		<version>12.1.0.2</version>
    	</dependency>
	<dependency>	
		<groupId>com.h2database</groupId>	
		<artifactId>h2</artifactId>	
		<scope>runtime</scope>	
	</dependency>        
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-jpa</artifactId>
	</dependency>	

The app uses jpa which creates claim and attributes tables when the app comes up. There is a script file - import.sql in src/main/resources which is run when the app comes up and creates sample data in both claim and attributes tables.

The app uses oracle docker image. If you are setting up app first time, the start-all.sh will throw errors to start oracle container.  Follow the below steps.
	login into dockerhub and go to https://hub.docker.com/_/oracle-database-enterprise-edition.
	Agree to the terms.
	Login with your userid/pwd into docker in terminal (Enter "docker login" in terminal)
	Run start-all.sh

Download sql developer and use the below connection setting to view/update the data.

	userid - sys as sysdba
	pwd - Oradoc_db1
	hostname - localhost
	port - 1521
	sid - ORCLCDB

Running start-all.sh will bring up consul, grafana, prometheus, redis, vault, oracle and ocp-vetservices-claims containers.

wait-for-it.sh - this script is used in docker-compose.yml file. This is used to make ocp-vetservices-claims container wait until oracle comes up. Once the ping for oracle is successful, it waits for another 30 seconds and brings up ocp-vetservices-claims container. 

If the container doesn't come up because of database connection issue, just restart the ocp-vetservices-claims container.
