# Developing with BIP Framework
This page provides a quick overview of the primary capabilities that the BIP Framework offers, and how to use them.

For more information, refer to the [bip-framework README.md](https://github.com/department-of-veterans-affairs/ocp-framework).

## Design Considerations
BIP Framework provides for distinct separation of model objects between the **Provider**, **Domain**, and **Partner** layers. For more information, see [Design: Layer and Model Separation](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/tree/master/docs/design-layer-separation.md).

Refer to the [bip-reference-spring-boot](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot) application for detailed information. It demonstrate suggested patterns, packaging, and framework usage when developing a new application for the BIP platform.

The BIP Framework makes use of the Spring AOP implementation of AspectJ for audit and performance logging. Compile-time code weaving is not used.

## Configuration
Platform capabilities are initialized in the bip-framework-autoconfigure artifact. The service application must enable these capabilities with the appropriate annotations in the application classes. Properties for managing these capabilities can be added to the application YAML. The [bip-reference README](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/tree/master#application-core-concepts-and-patterns) section provides links for more detailed information.

Application configuration of framework capabilities can be managed in the application YAML as identified in the sections below. See [bip-reference-person.yml](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/blob/mastaer/bip-reference-person/src/main/resources/bip-reference-person.yml) for an example of a functioning configuration.

Remaining application configuration should conform to normal spring configuration with a `@SpringBootApplication` class and other classes annotated with `@Configuration`.

## Security
The framework provides configured SSL certificates, partner client certificates, and applies JWT processing at the REST interface. For more information, see the following pages:
* [Secrets Management](secrets.md)
* [Secure Service Communications](secure-communication.md)
* [Application Security Management](application-security-management.md)

## Exceptions
The base `BipExceptionExtender` interface allows exception types to conform to BIP messaging requirements. For more information, see the [Exception Handling](exception-handling.md) page.

When something goes wrong, the response to the consumer must contain a meaningful message. This is managed by the framework `BipRestGlobalExceptionHandler`. It will use the message information found in any ot the `Bip*Exception` classes, and include it. For `java.lang.Exception` types, a standard message will be added to the consumer response.

## Logging
The framework offers useful extensions that help exceptions natively integrate the propagation of meaningful messages to the consumer(s) of the micro-service. When instantiating a logger, it is recommended to use the `BipLoggerFactory` to create a `BipLogger`. For example:

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(MyBipClass.class);

`BipLoggerFactory` creates `BipLogger` instances which are fully compatible with slf4j and logback. The logger also provides `BipBanner` (an ASCII-text banner) and severity `Level`. The logger also splits and manages exceptions so they can cross the docker 16KB comm channel limitation (https://github.com/kubernetes/kubernetes/issues/52444).

For configuration and implementation information, see [Log and Audit Management](log-audit-management.md).

## Audit Logging
Audit events may be triggered from an aspect or interceptor, and occur automatically when:
* A request is received at a REST resource class annotated with `@Controller`
* A call is made to a remoted or inter-service partner
* Data is retrieved from the Cache

Audit can manually be invoked on a class or method with the `@Auditable` annotation.

`AuditLogger` uses `AuditEventData` - and implementers of the `AuditableData` interface - to asynchronously log audit events through the `AuditLogSerializer`.

For configuration and implementation information, see [Log and Audit Management](log-audit-management.md).

## Performance Logging
Performance logging is automatically invoked by the `RestProviderTimerAspect`, and occurs automatically.

## Model Transformation
The framework encourages separation of layers (or "tiers") and their associated model objects. Layer separation is supported with a simple transformer pattern. For more information, see [Layer and Model Separation Design](design-layer-separation.md).

For SOAP partner clients, framework provides a [Date Adapter class](https://github.com/department-of-veterans-affairs/ocp-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/transfer/jaxb/adapters/DateAdapter.java) for date conversions. Other data type adapters can be added to the framework upon request. 

## Validation
Validation at the REST API should be performed using only standard JSR 303 annotations on the rest model objects. More complex validation can easily be added at the service layer interface. Currently, if validations are required prior to calling into a partner client, it must be invoked manually.

For more information, see [Validation](validation.md).

## Cache
Service impelementation classes can add properly declared `@CachePut` annotations to the overridden methods of their inteface. Once configuration and annotation is done, no other intervention is needed.

For more information, see [Cache Management](cache-management.md). An example of annotating a method, see the [ReferencePersonServiceImpl class](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/blob/master/bip-reference-person/src/main/java/gov/va/bip/reference/person/impl/ReferencePersonServiceImpl.java).

