# OpenAPI V3 code generation journey

Traditionally, writing SDKs (Models, Interfaces) can be a slow and painstaking process, with the need to manually develop a lot of code in languages. There has been an increase in the number of companies using code generation to create their SDKs for a variety of languages. For BIP framework team, intent of this journey is to investigate possible tools to generate SDKs that meets "Design First" Approach for API development

## Design First vs Build First API Development
When it comes to using API description formats, two important schools of thoughts have emerged: The “Design First” and the “Build First” approach to API development. The Build First approach is a more traditional approach to building APIs, with development of code happening after the business requirements are laid out, eventually generating the documentation from the code. The Design First approach advocates for designing the API’s contract first before writing any code. This is a relatively new approach, but is fast catching on, especially with the use of API description formats. To understand the two approaches better, let’s look at the general process followed during the API lifecycle. Like any product, the concept of the API starts with the business team identifying an opportunity. The opportunity is analyzed and a plan to capitalize on it is created in a text document by strategists, analysts and other business folks. This document is then passed along to the development team, which is where the plan takes some tangible form. There are two possibilities from here on to develop the API:

**Design First:** The plan is converted to a human and machine readable contract, such as a Swagger document, from which the code is built

**Build First:** Based on the business plan, API is directly coded, from which a human or machine readable document, such as a Swagger document can be generated

There are advantages and disadvantages associated with both approaches, and at the end of the day, choosing the right approach boils down to your immediate technological and strategic needs that you wish to solve with your APIs.

## OpenAPI V3
In July 2017, the first version of the OpenAPI V3 specification was released. OpenAPI is a standard for describing RESTful HTTP APIs which grew out of the older Swagger Specification, after it was donated to the OpenAPI Initiative following SmartBear's acquisition of Reverb Technologies.

OpenAPI is a way of describing your APIs in a YAML (or JSON) file. You model the data structures exposed and accepted by your API, and tie them to the HTTP calls that a client can make to your services. We found that a great way to get started with OpenAPI is to understand and experiment with the simple [Petstore example](https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v3.0/petstore.yaml) which is part of the OpenAPI specification. If you prefer a tutorial rather than experimenting, [then you may want to start here.](https://idratherbewriting.com/learnapidoc/pubapis_openapi_tutorial_overview)

## OpenAPI V3 Tooling

#### swagger-cli

swagger-cli is one of the important tools in the tooling chain. swagger-cli is a tool which pre-dates the OpenAPI specification, but has been updated to be compatible with OpenAPI V3.

You may end up using the two operations swagger-cli provides frequently while working with OpenAPI:

`swagger-cli validate <file>`

`swagger-cli bundle <file>`
  
validate is used to validate the contents of an OpenAPI YAML file performing some checks against the OpenAPI specification. 

bundle* is used to combine together a set of OpenAPI YAML files which are linked by $ref parameters into a single JSON file. $ref parameters are great because they allow you to use composition in your data models, as well as letting you break down your API descriptions into multiple files for a more maintainable codebase. It’s useful to combine your definitions together when you want to distribute a single file defining your APIs, or for use in other tools which don’t fully support the OpenAPI specification.

#### swagger code generator

Swagger Codegen is driven by SmartBear Software. Swagger Codegen can simplify your build process by generating server stubs and client SDKs for any API, defined with the OpenAPI (formerly known as Swagger) specification, so the team can focus better on API’s implementation and adoption.

In 2018, William Cheng, top contributor to Swagger Codegen, informed about a big change for the swagger community. William and other top contributors (40+) of Swagger Codegen have decided to fork the project to maintain a community-driven version called "OpenAPI Generator", which supports both OpenAPI spec v2 and v3. 

The founding members felt that Swagger Codegen 3.0.0 was diverging too much from the philosophy of Swagger Codegen 2.x. The founding members wanted a more rapid release cycle (weekly patch release, monthly minor release) so users do not need to wait for several months to get a stable release. Having a community-driven version allows for innovation, reliability, and a roadmap owned by the community. OpenAPI Generator is a fork of swagger-codegen between version 2.3.1 and 2.4.0.

#### openapi code generator

"OpenAPI Generator" is a comprehensive Java application which can generate client and server side code from your OpenAPI models. It’s a large code base with support for generating client-side SDKs in over 20 languages as well as nearly the same number of server-side implementations. It's a community-driven version which provides similar functionalities as that of Swagger Codegen and can be used as drop-in replacement. If a team already use swagger-codegen then it's recommended want to migrate from Swagger Codegen to OpenAPI Generator sooner rather than later.

While OpenAPI Generator is very comprehensive, there are rough edges when trying to use it ourselves; documentation can be inconsistent and the error messaging difficult to follow. This coupled with being a large Java code base with liberal usage of abstract classes and inheritance means that debugging why something isn’t working the way you’d expect can be incredibly challenging

There are two main components to how OpenAPI Generator generates source code:

**Mustache Templates**
The core of OpenAPI Generator is based on Mustache templates. Every line of code in your generated SDK is created by reading a template file in and substituting values into it based on your OpenAPI descriptions.

This is a really simple approach, but also adds great flexibility for customization. OpenAPI Generator provides tooling which allows you to extract the templates embedded in its JAR file into a directory, and then use the template files from that directory when generating code, providing simple customization without the need for recompiling the Java application. [For example, here’s the templates used when generating a JavaSpring SDK.](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator/src/main/resources/JavaSpring)

**Generator Classes**
Each language (or framework) you can generate has a java class which defines the behavior for generating code. This class is responsible for deciding what template files to load and generate based on your OpenAPI definitions.

It is very helpful to familiarize yourself with the inner workings of each of the generator classes that you want to work with. [Here’s the Java class which generates a Java Spring client SDK](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/main/java/org/openapitools/codegen/languages/SpringCodegen.java). Much of the business logic which defines the behaviour of the code generation is embedded in this Java. The drawback of this approach is that if want modify the business logic for code generation, you can’t just change the Mustache templates, you’ll have maintain your own fork of the OpenAPI Generator code base.

**Reference Links**

[Why was it decided to fork Swagger Codegen?](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/qna.md)

[Migration from Swagger Codegen](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/migration-from-swagger-codegen.md)

[OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator)

#### swagger ui

## Challenges encountered

#### Tooling support for OpenAPI V3 is very inconsistent
#### Polymorphism in OpenAPI Generator isn’t consistent
#### OpenAPI Generator falls back to Inline types
#### $refs are great, but inherently limited
#### Generated SDKs are going to closely match you API Structure
#### There’s no consistent way to break down and structure your OpenAPI definitions

## Summary and Recommendations
