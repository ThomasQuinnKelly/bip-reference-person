# AWS Services with Bip Framework on Local Environment
---
## AWS services currently supported on BIP framework

-- SNS

-- SQS

## AWS LocalStack

### What is LocalStack?

LocalStack builds on existing best-of-breed mocking/testing tools, most notably kinesalite/dynalite and moto. While these tools are awesome (!), they lack functionality for certain use cases. LocalStack combines the tools, makes them interoperable, and adds important missing functionality on top of them:

**Error injection:** LocalStack allows to inject errors frequently occurring in real Cloud environments, for instance ProvisionedThroughputExceededException which is thrown by Kinesis or DynamoDB if the amount of read/write throughput is exceeded.
Isolated processes: All services in LocalStack run in separate processes. The overhead of additional processes is negligible, and the entire stack can easily be executed on any developer machine and CI server. In moto, components are often hard-wired in RAM (e.g., when forwarding a message on an SNS topic to an SQS queue, the queue endpoint is looked up in a local hash map). In contrast, LocalStack services live in isolation (separate processes available via HTTP), which fosters true decoupling and more closely resembles the real cloud environment.
Pluggable services: All services in LocalStack are easily pluggable (and replaceable), due to the fact that we are using isolated processes for each service. This allows us to keep the framework up-to-date and select best-of-breed mocks for each individual service.

**Requirements:**

--python (both Python 2.x and 3.x supported)

--pip (python package manager)

--Docker

## Configure LocalStack for local development

With BIP-Framework handling most of the auto-configuration for LocalStack. Each project has the option of configuring different items to customize it for a users specific project:

1. bip-reference-person/bip-reference-person/src/main/resources/bip-reference-person.yml

```
localstack:
  enabled: true
  services:
    sns:
      enabled: true
      port: 4575
    sqs:
      enabled: true
      port: 4576

### AWS Service settings
aws:
  sns:
    topic: sub_new_topic
  sqs:
    queue: http://localhost:4576/queue/sub_new_queue
```
If a user wants to turn on LocalStack for the project then enable LocalStack to true. A user also has the ability to toggle different services off through a boolean featured on this yml file. Under these properties gives a default topic and que used to create items for demo purposes. However a user on a project can change these items to test out queues or topics as needed. 

Currently the default port for SNS and SQS mimic what is in LocalStack. However the configuration allows for users to change the port numbers as needed to accomodate the project.

### SNS Dependency
In the current setup, SQS dependencies are currently handled by LocalStack. However for SNS, LocalStack does not support all the necessary dependencies needed to create successful builds. In order to correct this, the user must ensure that the following dependency is added to the pom.xml file:
````		
        <!-- Spring Cloud AWS -->
   		<dependency>
   			<groupId>org.springframework.cloud</groupId>
   			<artifactId>spring-cloud-aws-messaging</artifactId>
   		</dependency>
````
