# Configuration Specifications

## Table of Contents

#### [SNS Configuration](#sns-configuration)
#### [SQS Configuration](#sqs-configuration)
#### [S3 Configuration](#s3-configuration)
#### [LocalStack Configuration](#localstack-configuration)
#### [Other Configurations](#other-configurations)

## SNS Configuration

The following .yaml specification defines all available configuration options for an SNS Service:

```
bip:
  framework:
    aws:
      sns:
        enabled: true
        topics:
          - id: first-topic-id
            name: first-topic-name
            endpoint: https://localhost:4575/topic/first-topic-name
            region: us-east-1
          - id: second-topic-id
            name: second-topic-name
            endpoint: https://localhost:4575/topic/second-topic-name
            region: us-east-1
            .
            .
            .
```

##### bip.framework.aws.sns.enabled:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will enable or disable the AutoConfiguration and functionality of this service.
##### bip.framework.aws.sns.topics:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; A list of SNS topics to be defined. You can configure zero to many of these topics, as necessary. When AutoConfiguration is enabled, each topic will be pulled from this configuration and created as a Spring Bean at runtime. Users will then be able to autowire in these AmazonSNS clients using the @Qualifier tag, and specifying the topic ID to be autowired.
##### bip.framework.aws.sns.topics[x].id:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The unique identifier for this topic. This unique identifier will be the value given to the Spring Bean generated for this topic. This will be the value used in @Qualifier tags to autowire in specific AmazonSNS Spring Beans.
##### bip.framework.aws.sns.topics[x].name:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The AWS name for the topic. See AWS documentation for further details.
##### bip.framework.aws.sns.topics[x].endpoint:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The AWS endpoint of the topic. See AWS documentation for further details.
##### bip.framework.aws.sns.topics[x].region:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The AWS region of the topic. See AWS documentation for further details.

## SQS Configuration

The following .yaml specification defines all available configuration options for an SQS Service:

```
bip:
  framework:
    aws:
      sqs:
        enabled: true
        queues:
          - id: first-queue-id
            name: first-queue-name
            endpoint: http://localhost:4576/queue/first-queue-name
            region: us-east-1
          - id: second-queue-id
            name: second-queue-name
            endpoint: http://localhost:4576/queue/second-queue-name
            region: us-east-1
            .
            .
            .
```

##### bip.framework.aws.sqs.enabled:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will enable or disable the AutoConfiguration and functionality of this service.
##### bip.framework.aws.sqs.queues:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; A list of SQS queues to be defined. You can configure zero to many of these queues, as necessary. When AutoConfiguration is enabled, each queue will be pulled from this configuration and created as a Spring Bean at runtime. Users will then be able to autowire in these AmazonSQS clients using the @Qualifier tag, and specifying the queue ID to be autowired.
##### bip.framework.aws.sqs.queues[x].id:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The unique identifier for this queue. This unique identifier will be the value given to the Spring Bean generated for this queue. This will be the value used in @Qualifier tags to autowire in specific AmazonSQS Spring Beans.
##### bip.framework.aws.sqs.queues[x].name:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The AWS name for the queue. See AWS documentation for further details.
##### bip.framework.aws.sqs.queues[x].endpoint:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The AWS endpoint of the queue. See AWS documentation for further details.
##### bip.framework.aws.sqs.queues[x].region:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The AWS region of the queue. See AWS documentation for further details.

## S3 Configuration

The following .yaml specification defines all available configuration options for an S3 Service:

```
bip:
  framework:
    aws:
      s3:
        enabled: true
        buckets:
          - id: first-bucket-id
            name: first-bucket-name
            endpoint: http://localhost:4576/bucket/first-bucket-name
            region: us-east-1
          - id: second-bucket-id
            name: second-bucket-name
            endpoint: http://localhost:4576/bucket/second-bucket-name
            region: us-east-1
            .
            .
            .
```

##### bip.framework.aws.s3.enabled:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will enable or disable the AutoConfiguration and functionality of this service.
##### bip.framework.aws.s3.buckets:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; A list of S3 buckets to be defined. You can configure zero to many of these buckets, as necessary. When AutoConfiguration is enabled, each bucket will be pulled from this configuration and created as a Spring Bean at runtime. Users will then be able to autowire in these AmazonS3 clients using the @Qualifier tag, and specifying the bucket ID to be autowired.
##### bip.framework.aws.s3.buckets[x].id:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The unique identifier for this bucket. This unique identifier will be the value given to the Spring Bean generated for this bucket. This will be the value used in @Qualifier tags to autowire in specific AmazonS3 Spring Beans.
##### bip.framework.aws.s3.buckets[x].name:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The AWS name for the bucket. See AWS documentation for further details.
##### bip.framework.aws.s3.buckets[x].endpoint:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The AWS endpoint of the bucket. See AWS documentation for further details.
##### bip.framework.aws.s3.buckets[x].region:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The AWS region of the bucket. See AWS documentation for further details.

## LocalStack Configuration

The following .yaml specification defines all available configuration options for the LocalStack service:

```
bip:
  framework:
    localstack:
      enabled: true
      services:
        s3:
          enabled: true
          port: 4572
        sns:
          enabled: true
          port: 4575
        sqs:
          enabled: true
          port: 4576
      externalHostName: localstack
      imageTag: 0.10.7
      pullNewImage: false
      randomizePorts: false
      defaultRegion: us-east-1
      network: bip-reference-person_bip
```

##### bip.framework.localstack.enabled:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will enable or disable the AutoConfiguration and functionality of this service.

##### bip.framework.localstack.services:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The service definitions for LocalStack. This list will inform LocalStack of which AWS Services it should enable and expose, and on what ports those services should be exposed on.

##### bip.framework.localstack.services[x].s3:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The S3 service definitions for LocalStack. When enabled, LocalStack will create this service and expose it on the given port (See default ports in [LocalStack documentation](https://github.com/localstack/localstack)). From there, AutoConfiguration will pull any S3 buckets defined under ```bip.framework.aws.s3.buckets:``` and create them in the LocalStack S3 service for use. Currently, no further customization is available through these configuration options, however this is a planned future enhancement.

##### bip.framework.localstack.services[x].s3.enabled:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will enable or disable the LocalStack service for S3

##### bip.framework.localstack.services[x].s3.port:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will configure the port LocalStack should expose the S3 service on.

##### bip.framework.localstack.services[x].sns:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The SNS service definitions for LocalStack. When enabled, LocalStack will create this service and expose it on the given port (See default ports in [LocalStack documentation](https://github.com/localstack/localstack)). From there, AutoConfiguration will pull any SNS topics defined under ```bip.framework.aws.sns.topics:``` and create them in the LocalStack SNS service for use. Currently, no further customization is available through these configuration options, however this is a planned future enhancement.

##### bip.framework.localstack.services[x].sns.enabled:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will enable or disable the LocalStack service for SNS

##### bip.framework.localstack.services[x].sns.port:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will configure the port LocalStack should expose the SNS service on.

##### bip.framework.localstack.services[x].sqs:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The SQS service definitions for LocalStack. When enabled, LocalStack will create this service and expose it on the given port (See default ports in [LocalStack documentation](https://github.com/localstack/localstack)). From there, AutoConfiguration will pull any SQS queues defined under ```bip.framework.aws.sqs.queues:``` and create them in the LocalStack SQS service for use. Currently, no further customization is available through these configuration options, however this is a planned future enhancement.

##### bip.framework.localstack.services[x].sqs.enabled:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will enable or disable the LocalStack service for SQS

##### bip.framework.localstack.services[x].sqs.port:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Will configure the port LocalStack should expose the SQS service on.

##### bip.framework.localstack.externalHostName:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The external host name LocalStack should be configured with. See [LocalStack documentation](https://github.com/localstack/localstack) for further details.

##### bip.framework.localstack.imageTag:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The image tag LocalStack should use when pulling an image for creation of the LocalStack container. See [LocalStack documentation](https://github.com/localstack/localstack) for further details.

##### bip.framework.localstack.pullNewImage:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Defines if LocalStack should reach out to pull a fresh image for creation of the LocalStack container. See [LocalStack documentation](https://github.com/localstack/localstack) for further details.

##### bip.framework.localstack.randomizePorts:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Defines if LocalStack should randomize the ports of services. See [LocalStack documentation](https://github.com/localstack/localstack) for further details.

##### bip.framework.localstack.defaultRegion:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Defines the default region LocalStack should be configured with. See [LocalStack documentation](https://github.com/localstack/localstack) for further details.

##### bip.framework.localstack.network:
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Defines the Docker network that the LocalStack container should attempt to join once instantiated. This setting will allow projects to create their own Docker network ecosystem to use, then have LocalStack dynamically join this network allowing other containers in the network to communicate with the service. Currently, the LocalStack container will always join a network with the alias "localstack", however the configuration of this alias is a planned future enhancement.

## Other Configurations

The following .yaml specification defines all miscellaneous configuration options:

```
None at this time
```