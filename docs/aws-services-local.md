# AWS Services with Bip Framework on Local Environment
---
## AWS services currently supported on BIP framework

-- SNS

-- SQS

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

`aws --endpoint-url=http://localhost:4576 --queue-url=http://localhost:4576/queue/my_queue sqs receive-message`