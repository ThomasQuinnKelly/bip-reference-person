# BIP Exception Management
This document is primarily concerned with exceptions generated in the course of executing business processes in the service and partner layers. For discussion of Provider layer validations and Service layer validations, see [validation.md](./validation.md).

## BIP Exception Implementation

Business service applications need to be able to return meaningful information to consumers. The framework has chosen to leverage Java exception propagation to carry this information.

[`BipException`](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/exception/BipException.java) and [`BipRuntimeException`](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/exception/BipRuntimeException.java) are the base exception classes for BIP service applications. Theses two classes extend their respective JVM exception classes (Exception and RuntimeException) to maintain JVM behaviors. They add BIP data by implementing [`BipExceptionExtender`](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/exception/BipExceptionExtender.java). This interface introduces the [`BipExceptionData`](https://github.com/department-of-veterans-affairs/bip-framework/blob/cmapi2-327/bip-framework-libraries/src/main/java/gov/va/bip/framework/exception/BipExceptionData.java) class, which should be populated to provide menaingful information to the service consumer.

See the class diagram at https://github.com/department-of-veterans-affairs/bip-framework/tree/master/bip-framework-libraries/#exception.

## Layer Concerns
Each layer of an BIP micro-service has different exception handling needs. Understanding layer concerns is necessary.

Associated patterns and implementation details are discussed later.

### Provider Concerns
The Provider (REST/API) layer must catch all Throwables and convert them to appropriate response messages:
- Must catch and handle exceptions that could be thrown in the frameworks and libraries used for protocol handling, deserialization/transformation, etc.
- Must catch and handle exceptions propagated from other layers in the service application.
- Response messages must be meaningful to the service consumer and the maintainers (support/development staff).
- The Provider layer must have enough information to determine appropriate severity, HTTP status codes, message keys, and text.

### Domain Concerns
The Service (domain/business) layers may generate exceptions during execution, and may receive exceptions from external entities and external clients. The business layers must be able to identify and categorize exceptions before allowing them to propagate to the Provider layer.

Service methods must also be able to validate method inputs and outputs at will. The [Defense](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/validation/Defense.java) class is used for this purpose. It is better to add generic methods as needed to the Defense class, than it is to write one-off inline value checks.

### Partner and Client Concerns
The service implementation `*PartnerName*Helper` class for the Partner client client may encounter a variety of exceptions that occur due to:
- Environment/System related problems beyond control of the application such as glitching network, failing hardware, partner service problems, service unavailable, etc. These typically resolve to 500-series http status code.
- Bugs in the BIP coding or schema interpretation inside the partner client jar/package should also be surfaced as 500-series codes.
- Request related problems that indicate some issue with the **input data**, such as invalid or malformed input, or requested data not found.

The `*Helper` class should trap legitimate faults that are returned from the client, and convert them to a `BipRuntimeException` (or subclass). Unexpected runtime exceptions can be allowed to bubble up to the `BipRestGlobalExceptionHandler`.

## Exception Hierarchy

Custom application exceptions should extend:
- `BipException` for checked exceptions that must be handled within the application
- `BipRuntimeException` for runtime exceptions that should propagate directly back to the [`BipRestGlobalExceptionHandler`](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/rest/exception/BipRestGlobalExceptionHandler.java)

<img alt="BIP Exception Hierarchy" src="images/bip-exception-class-hierarchy.png" height="66%" width="66%" />

See the BIP base exception classes in the [framework exception package](https://github.com/department-of-veterans-affairs/bip-framework/tree/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/exception)

## Exception Types
- `BipRuntimeException` and sub-classes identify conditions in which the exception should immediately propagate back to the Provider layer. Examples include validation violations, data not found, and other 400-series conditions under which processing should be aborted.
- `BipException` and sub-classes are checked exceptions that identify known conditions that may require some intervention or decision by the application logic. These exceptions might:
	- be handled, and processing allowed to continue;
	- be transformed to an BipRuntimeException and propagated immediately to the Provider layer. 
- All other exceptions, whether runtime or checked, are assumed to have originated from outside the application. Depending on the scenario and context, these would typically be candidates for 500-series handling.

## Exception Handling Patterns

### Provider Pattern
- Spring `@RestControllerAdvice` interceptor/aspect is used ...
	* Implemented in [BipRestGlobalExceptionHandler](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/rest/exception/BipRestGlobalExceptionHandler.java) class. In this class, each `@ExceptionHandler(value={ {{exception}}.class{{,...}} })` annotated method catches the specified exception(s) and formulates the message(s) and the HTTP `@ResponseStatus` to be returned to the consumer.
	* Spring processes `@ExceptionHandler` methods in the order they appear in the class, much like a try/catch block. The methods must appear in order from most specific to most general.
- Auto-configured by `BipRestAutoConfiguration`. 

### Service Patterns
- Subclass BIP exception types to help identify specific handling requirements.
- To the extent possible, convert non-BIP exception types to BIP exception types, with appropriate content.
- Exceptions directly generated by the service layer should be of type:
	* `BipRuntimeException` or sub-class if it is an issue that should immediately propagate back to the provider layer.
	* `BipPartnerRuntimeException` for exceptions raised due to interaction with external or inter-service services.
	* `BipException` or sub-class only if additional actions or decisions are required in order to resume execution.
	* `BipPartnerException` for exceptions raised due to interaction with external or inter-service services.
- The service impl *may* need to capture checked exceptions thrown from client operations and take specific actions or convert them. It should be rare that a service would need to catch runtime exceptions.
- The service helper (e.g. `PersonPartnerHelper`) *may* need to catch non-BIP partner exceptions, and convert them to and appropriate BIP exception type.

### Client Patterns

#### SOAP Partner Clients
- Known SOAP partner exceptions can be identified by searching the WSDL for "soap:fault". Each operation named in the bindings should have a fault defined.
- Spring AOP `ThrowsAdvise` implementation is provided in `InterceptingExceptionTranslator`
	* the intercepetor is made available for configuring clients in `BaseWsClientConfig.getInterceptingExceptionTranslator()`.
	* Any client implementataion of `BaseWsClientConfig` should configure a spring bean that gets the InterceptingExceptionTranslator. This configuration can cause specific exception classes to be excluded from the exception translation. For example, see [PersonWsClientConfig.personWsClientExceptionInterceptor()](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-partner-person/src/main/java/gov/va/bip/reference/partner/person/ws/client/PersonWsClientConfig.java)
	* The interceptor allows any exception under `gov.va.bip.framework.exception` to propagate untouched. All other exceptions are converted to `BipRuntimeException`. **Therefore** any exceptions that *should* propagate as checked exceptions *must* be thrown (or re-thrown) by the WsClientImpl as `BipException` or subclass.
- **Rules** for implementers of `RemoteServiceCall` (e.g. .\*RemoteServiceCallImpl, .\*RemoteServiceCallMock):
	* Allow **all** exceptions to propagate as-is.
- **Rules** for implementers of `BaseWsClientImpl`:
	* All partner exceptions that arise due to natural data issues (e.g. soap:fault raised due to "not found", "unknown format", etc) should propagate as `BipPartnerException`. The interceptor will convert non-excluded exceptions to BipPartnerRuntimeExceptions so they can be dealt with as validation-style exceptions.
	* *May* need to convert some partner exceptions that arise due to their own internal issues (e.g. database down, etc). These should be converted to `BipPartnerException` or sub-class so they can propagate to the service impl (or the provider handler) as known checked exceptions.

#### REST & Feign Clients
- Known REST partner exceptions should be identified in the partner's documentation, be it swagger or otherwise.
- Responses to REST calls typically provide a `Message` list of messages. These can be interrogated to determine the appropriate action.
- REST calls may result in a limited number of failure scenarios that require exception handling (e.g. service unavailable). Catch and handle these generically.
- Examples of REST calls made by RestTemplate and Feign are provided in the [PersonRestClientTester](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-person/src/main/java/gov/va/bip/reference/person/rest/client/provider/PersonRestClientTester.java) class.
