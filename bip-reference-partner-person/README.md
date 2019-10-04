## What is this project for?
This project is a Partner client module that demonstrates how to set up a client for making SOAP service calls from your application.

#### IMPORTANT

For demonstration purposes, this partner client module is included as a sub-project in the Reference Person reactor.  This is **not** the intent for *real* partner clients.

**Each partner project should have its own code repository, and produce a stand-alone JAR that can be independently added to the dependencies of a service application.**

## About Partner client projects
VA architecture separates data needs and availability across many providers (or "partners") and each provider typically exposes many APIs. Each API has specific operations and unique data models. This architecture is sensible, but does present a couple challenges for API consumers:

- **Don't Repeat Yourself**: In one organization, the same API may be accessed by multiple service applications. These applications should not have to recreate the same boilerplate XJC and XJB configurations and implementations.
- **Separation of Concerns**: Service code should not couple itself to the partner technology. A client JAR isolates the partner code and model to a separate and clear interface. Any changes in partner technology is more easily contained, and impacts to the service are limited to the partner data model.

For communication protocols that require adaptation to fit into the BIP Framework paradigm, it is good design to separate that code into a client project/JAR. Typical functions undertaken by a client are simple and direct:
* Receive the request from the service
* Assemble the protocol-specific request and call the external service partner
* Deliver the partner response (or exception) back to the service layer

Some things that a Partner client should **NOT** do - these are the responsibility of the calling service:
* Any form of translation or modification of data accepted by or returned from the partner.  The client cannot anticipate the needs of the service it is embedded within.
* Validations or filtering. The client cannot anticipate the needs of the service it is embedded within.
* Catch runtime exceptions (non-data problems). Code or infrastructure issues must bubble back to the service layers as-is, for translation by the global exception handler.

#### Simulating Client Connections

By definition, a client JAR will make external calls. However, the endpoints are unlikely to be available to local developer workstations, the build pipeline, and possibly some of the lower environments.

The Partner client design presents a `RemoteServiceCall` interface that has 2 implementations:
- the real implementation using the appropriate protocol to make the call to the partner
- a mock implementation that retrieves static data from `/test/mock/[response-file].xml`.

Configuration to invoke the mock simulator is done through the `remote_client_sims` spring profile. Search [bip-reference-person.yml](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/bip-reference-person/src/main/resources/bip-reference-person.yml) for *remote_client_sims* to see example configuration.

## How to add the dependency
In the POM file for your service application project, add this to the `<dependencies>` section:

```xml
<dependency>
	<groupId>gov.va.bip.reference.person</groupId>
	<artifactId>bip-reference-partner-person</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

If your application is a reactor project, you will also need to declare it in the `<modules>` section of your root reactor POM:
```xml
<module>bip-reference-partner-person</module>
```

## Diagrams

#### Class Diagram
<img src = "/docs/images/bip-reference-partner-person.png">

#### Sequence Diagram
<img src = "/docs/images/sd-reference-person-client-partner.png">

