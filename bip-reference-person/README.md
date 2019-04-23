## What is this repository for?
This project is the service application module that demonstrates how to set up a project to expose a REST API, validate and process requests, and call external partners.

## About the service application
The service application consists of:
- The configuration elements to connect to Vault and Consul, boot strap Spring Boot, and configure the platform capabilities for the application.
- Exposure of an API with request validation and exception management.
- Service capabilities to process requests and acquire data from external partners.

The responsibilities and capabilities of the service application are talked about throughout the documentation. See [Developing with BIP Framework](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/blob/master/docs/developing-with-bip-framework.md), [BIP Service Application Flow](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/blob/master/docs/application-flow.md), and the other links at [Application Core Concepts and Patterns](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot#application-core-concepts-and-patterns).

## How to add the dependency
The service application project is one of the sub-project `<modules>` in a reactor project.  Add the service project to the reactor POM.
```xml
	<module>bip-reference-person</module>
```

## Diagrams

#### Class Diagram
Classes that are inherited from bip-framework libraries.
<img src = "/docs/images/framework-reference-person.jpg">

#### Sequence Diagrams

**Provider Layer**
<img src = "/docs/images/sd-reference-person-layer-provider.png">

**Domain Layer**
<img src = "/docs/images/sd-reference-person-layer-domain.png">

**Partner Client**
<img src = "/docs/images/sd-reference-person-layer-partner.png">

