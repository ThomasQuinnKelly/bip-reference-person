AWS Services with Bip Framework on Local Environment
---
## AWS services currently supported on BIP framework

-- SNS

-- SQS

_Future Possible Services Include: FIFO Queues, Multiple Queues, Multiple Topics_

### What is Amazon SNS?

**Amazon Simple Notification Service (Amazon SNS)** is a web service that coordinates and manages the delivery or sending of messages to subscribing endpoints or clients. In Amazon SNS, there are two types of clients—publishers and subscribers—also referred to as producers and consumers. Publishers communicate asynchronously with subscribers by producing and sending a message to a topic, which is a logical access point and communication channel. Subscribers (that is, web servers, email addresses, Amazon SQS queues, AWS Lambda functions) consume or receive the message or notification over one of the supported protocols (that is, Amazon SQS, HTTP/S, email, SMS, Lambda) when they are subscribed to the topic.


When using Amazon SNS, you (as the owner) create a topic and control access to it by defining policies that determine which publishers and subscribers can communicate with the topic. A publisher sends messages to topics that they have created or to topics they have permission to publish to. Instead of including a specific destination address in each message, a publisher sends a message to the topic. Amazon SNS matches the topic to a list of subscribers who have subscribed to that topic, and delivers the message to each of those subscribers. Each topic has a unique name that identifies the Amazon SNS endpoint for publishers to post messages and subscribers to register for notifications. Subscribers receive all messages published to the topics to which they subscribe, and all subscribers to a topic receive the same messages.

### What is Amazon SQS?

**Amazon Simple Queue Service (SQS)** is a fully managed message queuing service that enables you to decouple and scale microservices, distributed systems, and serverless applications. SQS eliminates the complexity and overhead associated with managing and operating message oriented middleware, and empowers developers to focus on differentiating work. Using SQS, you can send, store, and receive messages between software components at any volume, without losing messages or requiring other services to be available. Get started with SQS in minutes using the AWS console, Command Line Interface or SDK of your choice, and three simple commands.

SQS offers two types of message queues. Standard queues offer maximum throughput, best-effort ordering, and at-least-once delivery. SQS FIFO queues are designed to guarantee that messages are processed exactly once, in the exact order that they are sent.

**_Benefits_**
* Eliminate administrative overhead -- AWS manages all ongoing operations and underlying infrastructure needed to provide a highly available and scalable message queuing service. With SQS, there is no upfront cost, no need to acquire, install, and configure messaging software, and no time-consuming build-out and maintenance of supporting infrastructure. SQS queues are dynamically created and scale automatically so you can build and grow applications quickly and efficiently.

* Keep sensitive data secure -- You can use Amazon SQS to exchange sensitive data between applications using server-side encryption (SSE) to encrypt each message body. Amazon SQS SSE integration with AWS Key Management Service (KMS) allows you to centrally manage the keys that protect SQS messages along with keys that protect your other AWS resources. AWS KMS logs every use of your encryption keys to AWS CloudTrail to help meet your regulatory and compliance needs.

* Reliably deliver messages -- Use Amazon SQS to transmit any volume of data, at any level of throughput, without losing messages or requiring other services to be available. SQS lets you decouple application components so that they run and fail independently, increasing the overall fault tolerance of the system. Multiple copies of every message are stored redundantly across multiple availability zones so that they are available whenever needed.

* Scale elastically and cost-effectively -- Amazon SQS leverages the AWS cloud to dynamically scale based on demand. SQS scales elastically with your application so you don’t have to worry about capacity planning and pre-provisioning. There is no limit to the number of messages per queue, and standard queues provide nearly unlimited throughput. Costs are based on usage which provides significant cost saving versus the “always-on” model of self-managed messaging middleware.

## AWS LocalStack

### What is LocalStack?

**Requirements:**

--Docker

_Note: Our current Localstack configuration has only been tested for Mac. Windows users will still be able to use Localstack as long as Docker is intalled on their machine._

LocalStack builds on existing best-of-breed mocking/testing tools, most notably kinesalite/dynalite and moto. While these tools are awesome (!), they lack functionality for certain use cases. LocalStack combines the tools, makes them interoperable, and adds important missing functionality on top of them.

## Installing AWS-CLI

**Requirements:**

--python (both Python 2.x and 3.x supported)

--pip (python package manager)

--curl

### Installation steps for Mac users

```pip3 --version```

```curl -O https://bootstrap.pypa.io/get-pip.py```

```python3 get-pip.py --user```

```pip3 install awscli --upgrade --user```

```aws --version```

Site to use when needing to troubleshoot or as a reference: 

https://docs.aws.amazon.com/cli/latest/userguide/install-macos.html
   
### Installation steps for Windows users

Currently we do not have a tested setup for windows users. To configure aws-cli to a windows machine follow the steps shown on this site: 

https://docs.aws.amazon.com/cli/latest/userguide/install-windows.html

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

Currently the default port for SNS and SQS mimic what is in LocalStack. However the configuration allows for users to change the port numbers as needed to accomodate the project.\

## Running AWS Localstack Application

* Create/Ensure you have a `.aws directory` in your home directory. 
* Ensure you have two files in this `.aws directory`: credentials and config

**Config File:**
```
[default]
aws_access_key_id=foo
aws_secret_access_key=bar
```
**Credentials File:**
```
[default]
aws_access_key_id=foo
aws_secret_access_key=bar
```

* Check out `epmo-aws-localstack` from bip-reference-person(https://github.ec.va.gov/EPMO/bip-reference-person.git) and bip-framework(https://github.ec.va.gov/EPMO/bip-framework.git) repos

* Go to the Parent Pom of bip-framework and do the following command: 

`mvn clean install -DskipTests -TC4 -U`

* Go to the Parent Pom of bip-reference-person and do the following command: 
   
`mvn clean install -DskipTests -TC4 -U`

* Run the spring boot application on the bip-reference-person application and it will show you the default topic and queue that was created in the console of your IDE.

## Verifying Through AWS-CLI

Here are some of the commands to use when verifying topics, queues, subscriptions, or messages through the aws-cli.

**List Topic:**

`aws --endpoint-url=http://localhost:4575 sns list-topics`

**List Queue:**

`aws --endpoint-url=http://localhost:4576 sqs list-queues`

**List subscriptions:**

`aws --endpoint-url=http://localhost:4575 sns list-subscriptions`

**List Published Message:**

`aws --endpoint-url=http://localhost:4576 --queue-url=http://localhost:4576/queue/sub_new_queue sqs receive-message`