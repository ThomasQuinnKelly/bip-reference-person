# Validation

## Capability
- Standard JSR303 validation in the Provider (REST) layer.
- Validation interface for @Service API Domain objects (domain request & response).

## Validation Patterns

### Provider (REST) layer
Any provider object models that appear as parameters to methods in @Controller classes can be validated using standard JSR303 annotations. BIP uses the hibernate.validator implementation.

Provider-level validations are intentionally kept simple. More complex validations should be done in the service layer.

### Service (DOMAIN) Layer
Domain model objects entering into the service layer are subject to business validations.
- Service validations are invoked on any class that uses the spring @Service annotation.
- Business validators are
  - implementations of the [Validator](https://github.ec.va.gov/EPMO/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/validation/Validator.java) interface.
- Validator implementations **must** appear in a `**.validators` package under the package in which the validated object model appears. For example, see the classes in the [reference model validators package](https://github.ec.va.gov/EPMO/bip-reference-person/tree/master/bip-reference-person/src/main/java/gov/va/bip/reference/person/model/validators). The model object to be validated could be located in any package - the requirement is that the validator appear in a `validators` package under it.
  - If a validator does not exist, a warning will be logged and validation for that object will be skipped.

This approach to validation requires discipline from the developer. It is their responsibility to demonstrate good logic and coding practices to ensure the validating code is air tight.

### Partner (PARTNER) Layer, and Partner Clients
Object models for external APIs that BIP calls are under the control of the external provider. For SOAP services, a WSDL and schema are provided. For REST, the model might be communicated by OpenAPI, or by nothing more than the JSON expected in the request and response. These external models are typically generated at build time with XJC, Swagger, or some other tool - or are provided statically in a client JAR. In any case, validating these external models presents its own challenges, and depends on what the needs and requirements are.

Validations for Partner clients should occur in the related service imiplementation's `*PartnerName*Helper `class. In BIP applications, you can use the Framework `Validator` interface.

Some pointers to consider with Partner client validations:
- Inputs to partner clients typically originate with consumer inputs to the BIP endpoints. JSR 303 validations (in the Provider layer) and business validations (in the service layer) are important.
- Validations should *fail-fast*. There is no point continuing with service processes if the data is invalid.
- Service implementation Helper classes can use the [Validator](https://github.ec.va.gov/EPMO/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/validation/Validator.java) interface in partner model validators.
