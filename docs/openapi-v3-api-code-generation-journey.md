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

### swagger-cli

swagger-cli is one of the important tools in the tooling chain. swagger-cli is a tool which pre-dates the OpenAPI specification, but has been updated to be compatible with OpenAPI V3.

You may end up using the two operations swagger-cli provides frequently while working with OpenAPI:

`swagger-cli validate <file>`

`swagger-cli bundle <file>`
  
validate is used to validate the contents of an OpenAPI YAML file performing some checks against the OpenAPI specification. 

bundle* is used to combine together a set of OpenAPI YAML files which are linked by $ref parameters into a single JSON file. $ref parameters are great because they allow you to use composition in your data models, as well as letting you break down your API descriptions into multiple files for a more maintainable codebase. It’s useful to combine your definitions together when you want to distribute a single file defining your APIs, or for use in other tools which don’t fully support the OpenAPI specification.

### swagger code generator

Swagger Codegen is driven by SmartBear Software. Swagger Codegen can simplify your build process by generating server stubs and client SDKs for any API, defined with the OpenAPI (formerly known as Swagger) specification, so the team can focus better on API’s implementation and adoption.

In 2018, William Cheng, top contributor to Swagger Codegen, informed about a big change for the swagger community. William and other top contributors (40+) of Swagger Codegen have decided to fork the project to maintain a community-driven version called "OpenAPI Generator", which supports both OpenAPI spec v2 and v3. 

The founding members felt that Swagger Codegen 3.0.0 was diverging too much from the philosophy of Swagger Codegen 2.x. The founding members wanted a more rapid release cycle (weekly patch release, monthly minor release) so users do not need to wait for several months to get a stable release. Having a community-driven version allows for innovation, reliability, and a roadmap owned by the community. OpenAPI Generator is a fork of swagger-codegen between version 2.3.1 and 2.4.0.

### openapi code generator

"OpenAPI Generator" is a comprehensive Java application which can generate client and server side code from your OpenAPI models. It’s a large code base with support for generating client-side SDKs in over 20 languages as well as nearly the same number of server-side implementations. It's a community-driven version which provides similar functionalities as that of Swagger Codegen and can be used as drop-in replacement. If a team is already using swagger-codegen, then it's recommended to migrate to OpenAPI Generator sooner rather than later.

While OpenAPI Generator is very comprehensive, there are rough edges when trying to use it ourselves; documentation can be inconsistent and the error messaging difficult to follow. This coupled with being a large Java code base with liberal usage of abstract classes and inheritance means that debugging why something isn’t working the way you’d expect can be incredibly challenging.

There are two main components to how OpenAPI Generator generates source code:

**Mustache Templates**
The core of OpenAPI Generator is based on Mustache templates. Every line of code in your generated SDK is created by reading a template file in and substituting values into it based on your OpenAPI descriptions.

This is a really simple approach, but also adds great flexibility for customization. OpenAPI Generator provides tooling which allows you to extract the templates embedded in its JAR file into a directory, and then use the template files from that directory when generating code, providing simple customization without the need for recompiling the Java application. [For example, here’s the templates used when generating a JavaSpring SDK.](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator/src/main/resources/JavaSpring)

**Generator Classes**
Each language (or framework) you can generate has a java class which defines the behavior for generating code. This class is responsible for deciding what template files to load and generate based on your OpenAPI definitions.

It is very helpful to familiarize yourself with the inner workings of each of the generator classes that you want to work with. [Here’s the Java class which generates a Java Spring client SDK](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/main/java/org/openapitools/codegen/languages/SpringCodegen.java). Much of the business logic which defines the behaviour of the code generation is embedded in this Java. The drawback of this approach is that if want modify the business logic for code generation, you can’t just change the Mustache templates, you’ll have maintain your own fork of the OpenAPI Generator code base.

For the BIP framework Proof of Concept, maven plugin **openapi-generator-maven-plugin** v3.3.4 was used vs newer version 4.0.0 due to the issues which are documented under [Challenges Encountered](#challenges-encountered) section. 

**Reference Links**

[Why was it decided to fork Swagger Codegen?](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/qna.md)

[Migration from Swagger Codegen](https://github.com/OpenAPITools/openapi-generator/blob/master/docs/migration-from-swagger-codegen.md)

[OpenAPI Generator](https://github.com/OpenAPITools/openapi-generator)

### swagger ui

Swagger UI is a collection of HTML, Javascript, and CSS assets that dynamically generates documentation from an OAS-compliant API. It allows your development team and end consumers — to visualize and interact with the API’s resources without having any of the implementation logic in place. It’s automatically generated from your OpenAPI (formerly known as Swagger) specification, with the visual documentation making it easy for back end implementation and client side consumption.

To generate Swagger UI from OAS 3.0 specification in yaml format, there are set of configuration changes required. At a high level, listed below are the steps needed. **BIP Blue Framework** would execute these steps during the application build process via Maven Parent POM configuration.

1. Download "swagger-ui" webjar and unpack it under the build directory.
2. Convert YAML specification to JSON format and copy it to swagger-ui webjar directory.
3. Set specification JSON path in Swagger UI index.html. Copy the files to the root swagger ui directory and delete version specific folder.
4. Serve the swagger ui resources with the framework Swagger Auto Configuration via Spring Resource Handler.

## Challenges encountered

#### Tooling support for OpenAPI V3 is very inconsistent
OpenAPI Generator’s support for OpenAPI V3 has some hurdles to overcome: it doesn’t play nice with YAML definitions that make use of `$ref`. $ref is a great way to break down the OpenAPI definitions, but if you want to be able to use these definitions with OpenAPI Generator, you’ll need to use swagger-cli combine to create one, combined JSON file with all your references embedded. You can then point OpenAPI Generator at this JSON file and code generation should mostly work as expected.

OpenAPI Generator latest version 4.0.0 uses an updated version of swagger-parser, which unfortunately introduced an issue where any OpenAPI files using `$ref` parameters in paths would no-longer pass validation. The issue has since been resolved on swagger-parser’s issue tracker, and is currently awaiting a dependency update in OpenAPI Generator. Unfortunately this meant disabling validation to be able to continue if using OpenAPI Generator 4. Other viable and recommended option is to stay on version 3.3.4. 

#### Polymorphism in OpenAPI Generator isn’t consistent

The model generated using Maven plugin 4.0.0 version doesn't use inheritance. There are multiple issues reported already related to this behavior. 

https://github.com/OpenAPITools/openapi-generator/issues/2888

https://github.com/OpenAPITools/openapi-generator/issues/2692

https://github.com/OpenAPITools/openapi-generator/issues/2576

One of the highly touted features of OpenAPI V3 was support for this type of polymorphism via the oneOf or anyOf features. Unfortunately OpenAPI Generator V3 did not yet support these features. When OpenAPI Generator 4 betas were announced, we spotted in the release notes that there was support for polymorphism.

Discovered that this support has only been added to the core of OpenAPI Generator, and not yet supported in the vast majority of the languages and frameworks that OpenAPI Generator can output.

Until there is a full polymorphism support in the languages we’re generating, most likely solution is to expose the more comprehensive version of each API call to make sure that you’re still able to access every feature of the framework and service. Other viable and recommended option is to stay on version 3.3.4.

#### $refs are great, but inherently limited
You can use `$ref` to modularize your OpenAPI definitions. This is great, but one of the major changes between Swagger V2, and OpenAPI V3 is that `$ref` can no-longer be used everywhere.

**From the OpenAPI documentation:**

`A common misconception is that $ref is allowed anywhere in an OpenAPI specification file. Actually $ref is only allowed in places where the OpenAPI 3.0 Specification explicitly states that the value may be a reference. For example, $ref cannot be used in the info section and directly under paths`

While it makes sense to limit the places $ref can be used, there is a problem that arise from the limitations that have been put in place. There’s no definitive list of where `$ref` can be used in the OpenAPI V3 specification or on the documentation website. This makes the barrier to entry for OpenAPI much higher, and encourages developers not to modularize their definitions because they don’t understand where you can use `$ref` and where you can’t. We appreciate that reading the specification is important, but we’re talking about extensive pages to understand it. 

#### Generated SDKs are going to closely match you API Structure

If you look closely at the Reference Person API example below, you’ll notice that we suggest following a consistent pattern with API structure, both in terms of our path layout, and the response format.

At the HTTP level, every successful request you make will return a JSON response where the objects you want to work with will be returned to the consumer. Any messages / errors needs to be returned consistently via ProviderResponse model object. This is to help our API be more flexible as we grow. We want to be able to return extra metadata about the API calls you make without having to change the fundamental return type, or resorting to passing around actionable information in HTTP headers. These concepts are borrowed from standards like JSON:API, which among other things, lets you embed objects which are referenced in data in other fields.

The OpenAPI definitions look something like this:

    PersonInfoResponse:
          title: PersonInfoResponse
          description: Model for the response from the Person Service
          allOf:
           - '$ref': './framework.yml#/components/schemas/ProviderResponse'
           - type: object
             # all other properties specific to PersonInfoResponse
             properties:
                personInfo:
                  description: The object representing the person information
                  '$ref': '#/components/schemas/PersonInfo'  
    PersonInfo:
      type: object
      properties:
        fileNumber:
          type: string
          example: 912444689
          description: The persons file number
        firstName:
          type: string
          example: JANE
          description: The persons first name
        lastName:
          type: string
          example: DOE
          description: The persons last name
        middleName:
          type: string
          example: M
          description: The persons middle name
        participantId:
          type: integer
          format: int64
          example: 6666345
          description: The persons participant ID
        socSecNo:
          type: string
          example: 912444689
          description: The persons SSN
      title: PersonInfo
      description: Model for data contained in the response from the Person Service

And we’d map this onto our HTTP API as follows:

    "/persons/pid":
    post:
      tags:
      - reference-person
      summary: Retrieve person information by PID from Person Service .
      description: Will return a person info object based on search by PID.
      operationId: personByPid
      requestBody: 
        description: personInfoRequest
        required: true
        content:
            application/json:
              schema:
                '$ref': '#/components/schemas/PersonInfoRequest'
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema:
                '$ref': '#/components/schemas/PersonInfoResponse'
        '400':
          description: There was an error encountered processing the Request.  Response
            will contain a  "messages" element that will provide further information
            on the error.  This request shouldn't be retried until corrected.
          content:
            application/problem+json:
              schema:
                '$ref': './framework.yml#/components/schemas/ProviderResponse'

#### There’s no consistent way to break down and structure your OpenAPI definitions

Typically you would end up using `$ref` to break up OpenAPI definitions and refer to the common objects, including the framework provided ones. Bigger question is to design and implement layout for these files. There is an article about good ways to break down your Swagger definitions into a more sensible directory structure, but not much has been written about doing the same with OpenAPI.

The big challenge here is that, we can only use $ref in a limited number of places in our OpenAPI definitions (Swagger was much more liberal). Much more time would need to be invested for the design and layout of OpenAPI YAML directory / repository. 

#### No feature to support JSR 303 custom messages

Based on the Proof of Concept work for Reference Person Sample API service, I can conclude that OAS and maven code generator plugin out of the box doesn't support additional configuration for the JSR 303 validation messages. One option to customize the messages is likely to override mustache templates. But this would require additional time to be spent by framework team to demosntrate via another task if its feasible option.

## Summary and Recommendations

OpenAPI is now a popular standard for modeling the APIs, and the tooling to generate SDKs from it works well across a variety of languages. However it's  still evolving, we can’t expect everything to work perfectly the first time. Team must invest time in developing the toolchain, and see if it works across the board for multiple teams. 

The speed of development of the SDKs would surely be effective and elegant to generate updated SDKs when adding new API calls to already existing products.

