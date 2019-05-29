# OpenAPI V3 code generation journey


Traditionally, writing SDKs (Models, Interfaces) can be a slow and painstaking process, with the need to manually develop a lot of code in languages. There has been an increase in the number of companies using code generation to create their SDKs for a variety of languages.

## OpenAPI V3
In July 2017, the first version of the OpenAPI V3 specification was released. OpenAPI is a standard for describing RESTful HTTP APIs which grew out of the older Swagger Specification, after it was donated to the OpenAPI Initiative following SmartBear's acquisition of Reverb Technologies.

OpenAPI is a way of describing your APIs in a YAML (or JSON) file. You model the data structures exposed and accepted by your API, and tie them to the HTTP calls that a client can make to your services. We found that a great way to get started with OpenAPI is to understand and experiment with the simple [Petstore example](https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v3.0/petstore.yaml) which is part of the OpenAPI specification. If you prefer a tutorial rather than experimenting, [then you may want to start here.](https://idratherbewriting.com/learnapidoc/pubapis_openapi_tutorial_overview)

## OpenAPI V3 Tooling

#### swagger-cli

#### swagger code generator

#### openapi code generator

#### swagger ui

## Challenges encountered

#### Tooling support for OpenAPI V3 is very inconsistent
#### Polymorphism in OpenAPI Generator isn’t consistent
#### OpenAPI Generator falls back to Inline types
#### $refs are great, but inherently limited
#### Generated SDKs are going to closely match you API Structure
#### There’s no consistent way to break down and structure your OpenAPI definitions
