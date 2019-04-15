# Layer and Model Separation Design

The separation of layers (or application "tiers") for BIP projects is enforced by the BIP Framework which defines three layers: Provider, Domain, and Partner.

Separation of concerns is of primary importance for ensuring encapsulation of functional requirements. It is important that this separation of concerns also be reflected in the packages created for any application.

## Rationale

The BIP development approach separates code into layers to:

- allow for service evolution (bug fixes, etc) without requiring API version increments
- allow for consumer API evolution without requiring service major-version increments
- help avoid implementation changes in one layer from polluting implementation details in other layers
- maintain clear separation of concerns, reducing developer confusion between layer responsibilities
- reduce the long term maintenance burden

## Layer definitions and where to find them

Framework layers are defined as:
* **Provider layer** (also known as "the web tier") is the service apps "public" API module, handling the interface exposed to external consumers. Provider packages are `framework.rest.*` and `framework.swagger`. The Provider tier also depends on `framework.security.**` to enforce consumer access security (see [Application Security Management](./application-security-management.md). Model objects must be of type `ProviderTransferObjectMarker`.
* **Domain layer** (also known as "the service layer") is where all business validations and execution of business logic takes place. Domain packages are all those which are not for the Provider or Partner layers. Model objects must be of type `DomainTransferObjectMarker`.
* **Partner layer** is the client module for making remote calls to other services that are external to the service app.  Partner packages are `framework.client.**`, with support for Web Service (SOAP) clients, and for RESTful (Feign) client calls. Model objects for remote client implementations must be of type `PartnerTransferObjectMarker`.

## Model transformation between layers

Layer separation - and separation of models between these layers - means that model transformation is required to bridge the interfaces between them. A simple Transformer pattern based on Spring's Converter pattern is provided by the abstract classes in the [framework.transfer.transform package](https://github.com/department-of-veterans-affairs/ocp-framework/tree/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/transfer/transform). 

Examples of transformation classes can be found in the [reference transform.impl package](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/tree/master/bip-reference-person/src/main/java/gov/va/bip/reference/person/transform/impl). How these transformers are used can be found in the [ReferencePersonServiceImpl class](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/blob/master/bip-reference-person/src/main/java/gov/va/bip/reference/person/impl/ReferencePersonServiceImpl.java) and the [PersonPartnerHelper class](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/blob/master/bip-reference-person/src/main/java/gov/va/bip/reference/person/client/ws/PersonPartnerHelper.java).
