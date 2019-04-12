# Swagger Management

## Capability (Webservices Contract : Swagger)
- Swagger is a framework for describing your API using a common language that everyone can understand. 
Swagger provides more benefits than just helping create clear documentation.
	a. It's comprehensible for developers and non-developers. 
	b. It's human readable and machine readable. This means that not only can this be shared with your team internally, but the same documentation can be used to automate API-dependent processes.
	c. It's easily adjustable. This makes it great for testing and debugging API problems.
- We are using Bottom-up approach 

## Swagger configuration

- Swagger has the following dependency in autoconfigure project:

	    <dependency>
	      <groupId>io.springfox</groupId>
	      <artifactId>springfox-swagger2</artifactId>
	      <version>${springfox.version}</version>
	    </dependency>
	    <dependency>
	      <groupId>io.springfox</groupId>
	      <artifactId>springfox-swagger-ui</artifactId>
	      <version>${springfox.version}</version>
	    </dependency>

- add the bip-framework-autoconfigure dependency to the project pom, with the appropriate version that will 
  include the autoconfigure projects.
  
	   <dependency>
             <groupId>gov.va.bip.framework</groupId>
             <artifactId>bip-framework-autoconfigure</artifactId>
             <!-- add the appropriate version -->
           </dependency>
    
- Update the application service yml file with the following configuration (under the default profile):

	   bip.framework:
	     swagger:
    		title: 
    		description:
    		groupName: "@project.name@-@project.version@"
    		version: ${info.build.version}
    		securePaths: /api/v?.*/persons/.*
    			
   securePaths above secures the swagger URL's access and forwards the Security Context(JWT) to 
   actual Service API calls.  

- Add the @EnableSwagger2 annotation to the Spring Boot Application class 

		@Configuration
		@EnableConfigurationProperties(SwaggerProperties.class)
		@EnableSwagger2
		@ConditionalOnProperty(prefix = "bip.framework.swagger", name = "enabled", matchIfMissing = true)
		@Import({ BeanValidatorPluginsConfiguration.class })
		public class SwaggerAutoConfiguration {}
	
- Add ApiOperations and ApiMethod annotations on the resource class methods to describe about the 
  method and responses
  
		@ApiOperation(value = "A health check of this endpoint",
				notes = "Will perform a basic health check to see if the operation is running.")
		@ApiResponses(value = {
				@ApiResponse(code = 200, message = MESSAGE_200) })
	
## Swagger Page Security and Errors

- Security to access the Swagger page is through the JWT token generated. Need to generate the token using Token-Resource on the swagger page and add it to the Authorize section using "Bearer" as key for the JWT token.

- Swagger page has link to access the possible key-value of the errors in the REST resource responses

- Reference Swagger application snapshot:
<img alt="Swagger Person Sample" src="/docs/images/Swagger-Person-Sample.jpg" width=80% height="80%"/>

- JWT Token Generation and Authorization:

	a. Expand Token Resource end point and click on "Try it out" button:
	<img alt="Swagger Expand Token Resopurce" src="/docs/images/Swagger-Expand-Token-Resource.png" width="80%" height="80%" />
	
	b. We can view the default Person Traits in the swagger and edit as required:
	<img alt="Swagger JWT Token Person Traits" src="/docs/images/Swagger-JWTToken-PersonTraits.png" width="80%" height="80%" />
	
	c. Copy the JWT token generated:
	<img alt="JWT Copy Token" src="/docs/images/Swagger-Copy-JWTToken.png" width="80%" height="80%" />
	
	d. Paste the JWT token by clicking the Authorize button and use the key as "Bearer", and click Authorize button: 
	<img alt="JWT Authorize" src="/docs/images/Swagger-JWTToken-Bearer.png" width="80%" height="80%" />
