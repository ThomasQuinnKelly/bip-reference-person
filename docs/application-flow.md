# BIP Service Application Flow
This docoument is primarily concerned with describing the typical flow of a service application built on the [BIP Framework](https://github.com/department-of-veterans-affairs/ocp-framework). The spring boot [Reference Person](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot) application provides a valuable example of many of the mechanics involved in preparing for development of a project.

## Overview
BIP applications can be thought of as being comprised of three layers (or "tiers"), each of which encapsulates functionality for the layer:
* Provider layer (or "web", or "exposed api")
* Domain layer (or "service", or "business")
* Partner layer (or "client", or "services outside this app")

This topic is discussed in more detail in [Layer and Model Separation Design](design-layer-separation.md).

## Provider Layer
This is where the "public" API for the service application is exposed. Functions undertaken in this layer include:
* Receiving requests in one or more Resource class(es) that are annotated with `@RestController`. This class will have methods annotated with the relveant `@RequestMapping` attributes.
* Converting Provider model objects into Damain model objects, calling the service, then converting the returned domain response into a Provider model response.

SEQUENCE DIAGRAM HERE

## Domain Layer


## Partner Layer


