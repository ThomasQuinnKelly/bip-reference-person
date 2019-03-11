# Application Security Management

## Capability and Features
- Self contained Secure Token for Authentication
- Stateless Authentication
- Trusted Partner
- Server to Server communication

## Security Pattern
- JSON Web Token (JWT) Security using Transitive Trust Relationship to be enabled for the application
- JWT signature algorithm to sign the token must use HS256 as defined in [JSON Web Algorithms](https://tools.ietf.org/html/draft-ietf-jose-json-web-algorithms-31#section-3.1)
- JWT needs to be signed with a signing key and must match for the trusted services that communicates.
- JWT issuer to exists in the parsed JWT as specified in "iss" and must match for the trusted services that communicates.
- OCP Framework library would DeCrypt the JWT and attempt to construct a PersonTraits object from it. [PersonTraits.java](https://github.com/department-of-veterans-affairs/ocp-framework/blob/master/ocp-framework-libraries/src/main/java/gov/va/ocp/framework/security/PersonTraits.java)

## Security configuration
- Add dependency in pom.xml to include ocp-framework-autoconfigure library

	 <dependency>
        <groupId>gov.va.ocp.framework</groupId>
        <artifactId>ocp-framework-autoconfigure</artifactId>
        <version><!-- add the appropriate version --></version>
     </dependency>
     
 - Java source file from OCP Framework library that does AutoConfiguration for JWT. [OcpSecurityAutoConfiguration.java](https://github.com/department-of-veterans-affairs/ocp-framework/blob/master/ocp-framework-autoconfigure/src/main/java/gov/va/ocp/framework/security/autoconfigure/OcpSecurityAutoConfiguration.java)
 
 - JWT authentication properties are set via prefix "ocp.security.jwt". Java source file: https://github.com/department-of-veterans-affairs/ocp-framework/blob/master/ocp-framework-libraries/src/main/java/gov/va/ocp/framework/security/jwt/JwtAuthenticationProperties.java
 
 - By default in ${spring.config.name}.yml file, ascent security is enabled for all the application profiles. To disable the security, developer would have to set the property ocp.security.jwt.enabled to false
 	
		Example from ocp-reference-person service is shown below.
		
		###############################################################################
		#Property configuration to enable or disable JWT security for the service calls 
		#JWT security is enabled by default. So to disable it you have to
		#set the environment system property os.reference.security.jwt.enabled=false
		###############################################################################
		ocp:
		  security:
		    jwt:
		      enabled: true
		      filterProcessUrls: 
		        - /api/v1/persons/**
		        - /api/v2/persons/**
		      excludeUrls:
		        - /**
-  All the properties for JWT Security with prefix `ocp.security.jwt` that are configurable are listed below.

a. **ocp.security.jwt.enabled**: Boolean property to enable or disable JWT security on the service end points. Defaults to "**true**"

b.**ocp.security.jwt.filterProcessUrls**: List of strings to specify the URLs that are allowed for any authenticated user. Defaults to "**/api/****"

c. **ocp.security.jwt.excludeUrls**: List of strings that allows adding RequestMatcher instances which Spring Security should ignore. Defaults to "**/****"

d. **ocp.security.jwt.issuer**: Ensures that the specified "iss" exists in the parsed JWT. If missing or if the parsed value does not equal the specified value, an exception will be thrown indicating that the JWT is 								 invalid and may not be used. Defaults to "**Vets.gov**"

e. **ocp.security.jwt.secret**: JWT needs to be signed with a signing key and must match for the trusted services that communicates. Defaults to "**secret**"

f. **ocp.security.jwt.header**: Request header name to read JWT token value from. Defaults to "**Authorization**"
	

