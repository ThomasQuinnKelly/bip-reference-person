#AWS Services with BIP Framework

The Blue Framework AWS Library is intended to provide BIP Platform tenants leveraging the Blue Framework a suite of AutoConfiguration resources and Blue Framework integrations to maximize tenant's ability to utilize AWS services, while minimizing overhead and allowing tenants to define their AWS Service Clients in a configuration-as-code pattern.

## Functionality Currently Supported

### [SNS](https://aws.amazon.com/sns/)
**Amazon Simple Notification Service (Amazon SNS)** is a web service that coordinates and manages the delivery or sending of messages to subscribing endpoints or clients. In Amazon SNS, there are two types of clients—publishers and subscribers—also referred to as producers and consumers. Publishers communicate asynchronously with subscribers by producing and sending a message to a topic, which is a logical access point and communication channel. Subscribers (that is, web servers, email addresses, Amazon SQS queues, AWS Lambda functions) consume or receive the message or notification over one of the supported protocols (that is, Amazon SQS, HTTP/S, email, SMS, Lambda) when they are subscribed to the topic.

### [SQS](https://aws.amazon.com/sqs/)

**Amazon Simple Queue Service (SQS)** is a fully managed message queuing service that enables you to decouple and scale microservices, distributed systems, and serverless applications. SQS eliminates the complexity and overhead associated with managing and operating message oriented middleware, and empowers developers to focus on differentiating work. Using SQS, you can send, store, and receive messages between software components at any volume, without losing messages or requiring other services to be available. Get started with SQS in minutes using the AWS console, Command Line Interface or SDK of your choice, and three simple commands.

SQS offers two types of message queues. Standard queues offer maximum throughput, best-effort ordering, and at-least-once delivery. SQS FIFO queues are designed to guarantee that messages are processed exactly once, in the exact order that they are sent.

### [S3](https://aws.amazon.com/s3/)

**Amazon Simple Storage Service (Amazon S3)** is an object storage service that offers industry-leading scalability, data availability, security, and performance. This means customers of all sizes and industries can use it to store and protect any amount of data for a range of use cases, such as websites, mobile applications, backup and restore, archive, enterprise applications, IoT devices, and big data analytics. Amazon S3 provides easy-to-use management features so you can organize your data and configure finely-tuned access controls to meet your specific business, organizational, and compliance requirements. Amazon S3 is designed for 99.999999999% (11 9's) of durability, and stores data for millions of applications for companies all around the world.

### [LocalStack](https://github.com/localstack/localstack)

LocalStack provides an easy-to-use test/mocking framework for developing Cloud applications.

LocalStack builds on existing best-of-breed mocking/testing tools, most notably kinesalite/dynalite and moto. While these tools are awesome (!), they lack functionality for certain use cases. LocalStack combines the tools, makes them interoperable, and adds important missing functionality on top of them.

## Prerequisites

### Install Docker

A local Docker installation is necessary for utilizing LocalStack on your machine. Our AutoConfiguration will manage creating/destroying a LocalStack Docker container for you.

https://docs.docker.com/get-docker/

_Note: If you don't intend on leveraging the LocalStack AutoConfiguration, this step is optional._

### Install AWS CLI (Optional)

AWS CLI will allow you to manually interact with your AWS Services. None of the AutoConfiguration functionality in the Framework AWS Library needs this installed to function.

https://docs.aws.amazon.com/cli/latest/userguide/install-macos.html

https://docs.aws.amazon.com/cli/latest/userguide/install-windows.html

## Add Maven Dependency

To get started with the BIP Framework AWS library, you will need to add the following Maven dependency to your project:

```
    <dependency>
        <groupId>gov.va.bip.framework</groupId>
        <artifactId>bip-framework-autoconfigure-aws</artifactId>
        <version>${bip-framework.version}</version>
    </dependency>
```

## Further Information

Continue onto our [configuration specifications](aws-services-configuration.md) for more details on how to implement these services. 

