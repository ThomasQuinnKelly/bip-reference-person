# Application SSL Certificates Management

## Capability and Features
- Able to connect Vault
- Ability to read and load SSL Certificates from Vault

## SSL Certificate Storage
- DevOps to load SSL Certificates in Vault for the application service
- Example of how to load the PEM format certificate/key into vault as well as any number of CA certificates needed
    - https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/local-dev/vault-config/vault-config.sh

## BIP Framework Library Support

- Provides a utility class for creating KeyStore objects from PEM format certificates
    - https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/security/jks/KeystoreUtils.java
	
- Creates web service template using the supplied HTTP request/response interceptors, provided web service interceptors and message factory to add SSL context 
    - https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-libraries/src/main/java/gov/va/bip/framework/client/ws/BaseWsClientConfig.java
    
## Service Implementation Guidance

### Configuration
- Application must be configured for the successful vault connection
- Demonstrated in [BIP Reference Person Example Service](https://github.com/department-of-veterans-affairs/bip-reference-person) the configurations required to retrieve SSL certificates. 
- BIP services use Spring Cloud Vault that supports the versioned Key-Value secret backend. The key-value backend allows storage of arbitrary values as key-value store. 

### Example of pulling client certificates from Vault 
    
- Example of consuming the secrets from Vault in the application:
    - https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-partner-person/src/main/java/gov/va/bip/reference/partner/person/client/ws/PersonWsClientProperties.java
    
- Example of using the resulting KeyStore objects to configure your client connection:
    - https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-partner-person/src/main/java/gov/va/bip/reference/partner/person/client/ws/PersonWsClientConfig.java#L131
    
- Example of unit tests and application yml:
    - https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-partner-person/src/test/java/gov/va/bip/reference/partner/person/client/ws/PersonWsClientPropertiesTest.java
    - https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-partner-person/src/test/resources/application.yml


