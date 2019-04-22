## What is this project for?
This project is a Partner client module that demonstrates how to set up a client for making SOAP service calls from your application.

#### IMPORTANT

For demonstration purposes, this partner client module is included as a sub-project in the Reference Person reactor.  This is **not** the intent for real service applications.

**Each partner project should have its own code repository, and produce a stand-alone JAR that can be independently added to the dependencies of a service application.**

## Why a "Partner" client?
VA architec ture separates data needs and availability across many providers (or "partners") and each provider typically exposes many APIs. Each API has a specific endpoints and unique data models. This architecture is sensible, but does present a couple challenges for API consumers:

- **Don't Repeat Yourself**: In one organization, the same API may be accessed by multiple service applications. These applications should not have to recreate the same boilerplate XJC and XJB configurations and implementations.
- **Separation of Concerns**: Service code should not couple itself to the partner technology. A client JAR isolates the partner code and model to a separate and clear interface. Any changes in partner technology is more easily contained, and impacts to the service are limited to the partner data model.

## How to add the dependency
In the POM file for your service application project, add this to the `<dependencies>` section:
	<dependency>
		<groupId>gov.va.bip.reference.person</groupId>
		<artifactId>bip-reference-partner-person</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>

If your application is a reactor project, you will also need to declare it in the `<modules>` section of your root reactor POM:
	<module>bip-reference-partner-person</module>

## Diagrams

