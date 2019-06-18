# BIP OpenAPI v3 Developer Guide

OpenAPI is the open source successor to the old Swagger specification. The framework team has spent time investigating the two main approaches to using OpenAPI, and any issues that arise. There is value in being aware of [the research and its findings](https://github.com/department-of-veterans-affairs/bip-reference-person/tree/master/docs/openapi-v3-api-code-generation-journey.md).

BIP Framework began with support for the legacy Code First approach. With the move to OpenAPI v3, the framework is changing its API model to the Design First approach. In many ways this makes it easier for new projects, but imposes an upgrade requirement on existing projects.

This document describes how to use OpenAPI v3 in service applications built on BIP Framework, specifically:

- How framework prepares OpenAPI for you at build time
- Application configuration
- How to specify an API for BIP consumption
- What code gets generated, and best practices for integrating that code into your application
- Steps to migrate Code First applications to the new API paradigm

## Framework build responsibilities

Framework reduces application configuration burden by providing an opinionated approach to acquiring and building the API model and interface classes, and the swagger UI documentation.

Configuration is in [`bip-framework-parentpom/pom.xml`](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-parentpom/pom.xml):

- Add org.openqpitools dependencies

	<details><summary>XML Snippet</summary>

	```xml
	<dependency>
		<groupId>org.openapitools</groupId>
		<artifactId>openapi-generator-cli</artifactId>
		<version>${openapi.codegen.version}</version>
	</dependency>
	<dependency>
		<groupId>org.openapitools</groupId>
		<artifactId>openapi-generator</artifactId>
		<version>${openapi.codegen.version}</version>
	</dependency>
	```
	</details>

- Provide build configuration for the Swagger UI (yes, it is still called swagger ui):

	- download and unpack the swagger-ui web jar

		<details><summary>XML Snippet</summary>

		```xml
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-dependency-plugin</artifactId>
			<executions>
				<execution>
					<id>download-swagger-ui-webjar</id>
					<phase>initialize</phase>
					<goals>
						<goal>unpack</goal>
					</goals>
					<configuration>
						<artifactItems>
							<artifactItem>
								<groupId>org.webjars</groupId>
								<artifactId>swagger-ui</artifactId>
								<version>${swagger-ui.version}</version>
								<overWrite>false</overWrite>
							</artifactItem>
						</artifactItems>
						<outputDirectory>${project.build.directory}/classes</outputDirectory>
					</configuration>
				</execution>
			</executions>
		</plugin>
		```
		</details>

	- generate the application api definition

		<details><summary>XML Snippet</summary>

		```xml
		<plugin>
			<groupId>org.openapitools</groupId>
			<artifactId>openapi-generator-maven-plugin</artifactId>
			<version>${openapi.codegen.version}</version>
			<executions>
				<execution>
					<id>generate-openapi-spec</id>
					<goals>
						<goal>generate</goal>
					</goals>
					<configuration>
						<inputSpec>${basedir}/src/main/resources/openapi/openapi.yml</inputSpec>
						<validateSpec>true</validateSpec>
						<generatorName>openapi</generatorName>
						<output>${project.build.directory}/classes/META-INF/resources/webjars/swagger-ui</output>
					</configuration>
				</execution>
			</executions>
		</plugin>
		```
		</details>

	- replace the definition file and URL config in swagger-ui

		<details><summary>XML Snippet</summary>

		```xml
		<plugin>
			<groupId>com.google.code.maven-replacer-plugin</groupId>
			<artifactId>replacer</artifactId>
			<version>${google.codereplacer.version}</version>
			<executions>
				<execution>
					<id>replace-tokens-swaggerui</id>
					<phase>prepare-package</phase>
					<goals>
						<goal>replace</goal>
					</goals>
				</execution>
			</executions>
			<configuration>
				<filesToInclude>
					${project.build.directory}/classes/META-INF/resources/webjars/swagger-ui/index.html,
					${project.build.directory}/classes/META-INF/resources/webjars/swagger-ui/${swagger-ui.version}/index.html
				</filesToInclude>
				<replacements>
					<replacement>
						<token>"https://petstore.swagger.io/v2/swagger.json"</token>
						<value>"/openapi.json"</value>
					</replacement>
				</replacements>
			</configuration>
		</plugin>
		```
		</details>

	- move swagger-ui to the appropriate location in the application /target

		<details><summary>XML Snippet</summary>

		```xml
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-antrun-plugin</artifactId>
			<executions>
				<execution>
					<id>copy-delete-swaggerui</id>
					<phase>compile</phase>
					<goals>
						<goal>run</goal>
					</goals>
					<configuration>
						<target>
							<copy
								todir="${project.build.directory}/classes/META-INF/resources/webjars/swagger-ui"
								flatten="true" overwrite="true">
								<fileset
									dir="${project.build.directory}/classes/META-INF/resources/webjars/swagger-ui/${swagger-ui.version}" />
							</copy>
							<delete
								dir="${project.build.directory}/classes/META-INF/resources/webjars/swagger-ui/${swagger-ui.version}" />
						</target>
					</configuration>
				</execution>
			</executions>
		</plugin>
		```
		</details>

- For eclipse / STS users, the POM also uses the m2e connector to add the generated artifacts to the source factory path. If the eclipse install does not already have the required connector, the developer can open the [`bip-framework-parentpom/pom.xml`](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-parentpom/pom.xml) in the IDE, hover over the `<execution>` tag, and select to "Discover new m2e connectors" option.

	<details><summary>XML Snippet</summary>

	```xml
	<plugin>
		<groupId>org.codehaus.mojo</groupId>
		<artifactId>build-helper-maven-plugin</artifactId>
		<executions>
			<!-- TODO for eclipse/m2e users: install the m2e connector 'buildhelper'
					by selecting 'Discover new m2e connectors' while hovering over the following
					execution tag -->
			<execution>
				<id>add-generated-source</id>
				<phase>generate-sources</phase>
				<goals>
					<goal>add-source</goal>
				</goals>
				<configuration>
					<sources>
						<source>${project.build.directory}/generated-sources/openapi</source>
					</sources>
				</configuration>
			</execution>
		</executions>
	</plugin>
	```
	</details>

## Application Configuration

BIP service applications should leverage the framework configuration to minimize configuration requirements in the application POM files.

#### Reactor POM

- The service reactor (root) POM must have `<parent>` set to `bip-framework-parentpom`

	<details><summary>XML Snippet</summary>

	```xml
	<parent>
		<groupId>gov.va.bip.framework</groupId>
		<artifactId>bip-framework-parentpom</artifactId>
		<version>[CURRENT_VERSION]</version>
		<relativePath/>
	</parent>
	```
	</details>

#### Service Application POM

- The service POM (e.g. `bip-[application-name]/pom.xml`) contains the build directives for OpenAPI. The directives are in a profile that is activated only when the openapi.yml file is available. It is important to note that the path to the openapi.yml file is also used by framework, so cannot be changed.

	<details><summary>XML Snippet</summary>

	```xml
	<profiles>
		<!-- OpenAPI Specification (OAS) Code generation profile. In the framework
			parent pom see profile with id: org-openapitools-codegen-parent There are
			multiple plugins in the parent POM for swagger UI download, generate. The
			<exists> section below has to match the one from parent pom for the build
			phases and goals to execute for openapi code and swagger ui generation to
			work -->
		<profile>
			<id>org-openapitools-codegen-reference-person</id>
			<activation>
				<file><!-- Existence of API YML file to activate the profile. DO NOT
							MODIFY LOCATION AND FILE NAME AS FRAMEWORK USES THE SAME FOR ACTIVATION,
							SPEC GENERATE -->
					<exists>${basedir}/src/main/resources/openapi/openapi.yml</exists>
				</file>
			</activation>
			<build>
				<!-- ... -->
			</build>
		</profile>
	</profiles>
	```
	</details>

- The `openapi-generator-maven-plugin` is used to declare service-specific additions to the generation of swagger-ui.

	<details><summary>XML Snippet</summary>

	```xml
	<build>
		<plugins>
			<!-- activate the openapi-generator-maven-plugin -->
			<plugin>
				<groupId>org.openapitools</groupId>
				<artifactId>openapi-generator-maven-plugin</artifactId>
				<version>${openapi.codegen.version}</version>
				<executions>
					<execution>
						<!-- ... -->
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>
	```
	</details>

- The **execution id should be set** to match your application name. The goal must be `generate`.

	<details><summary>XML Snippet</summary>

	```xml
	<execution>
		<id>generate-openapi-[application-name]</id>
		<goals>
			<goal>generate</goal>
		</goals>
		<configuration>
			<!-- ... -->
		</configuration>
	</executions>
	```
	</details>

- The inputSpec and output directory must remain unchanged

	<details><summary>XML Snippet</summary>

		```xml
		<inputSpec>${basedir}/src/main/resources/openapi/openapi.yml</inputSpec>
		<output>${project.build.directory}/generated-sources</output>
		```
		</details>

- **Alter the packaging values** to specify the application API java packages

	<details><summary>XML Snippet</summary>

		```xml
		<apiPackage>gov.va.bip.[application.package].api</apiPackage>
		<modelPackage>gov.va.bip.[application.package].api.model.v1</modelPackage>
		<importMappings>
						ProviderResponse=gov.va.bip.framework.rest.provider.ProviderResponse,
						ProviderRequest=gov.va.bip.framework.rest.provider.ProviderRequest,
						Message=gov.va.bip.framework.rest.provider.Message,
						Person=gov.va.bip.framework.security.model.Person,
						ProviderTransferObjectMarker=gov.va.bip.framework.transfer.ProviderTransferObjectMarker
		</importMappings>
		```
		</details>

- The import mappings must include the framework base model classes. **Classes from elsewhere can be added as required.**

	<details><summary>XML Snippet</summary>

	```xml
	<importMappings>
		ProviderResponse=gov.va.bip.framework.rest.provider.ProviderResponse,
		ProviderRequest=gov.va.bip.framework.rest.provider.ProviderRequest,
		Message=gov.va.bip.framework.rest.provider.Message,
		Person=gov.va.bip.framework.security.model.Person,
		ProviderTransferObjectMarker=gov.va.bip.framework.transfer.ProviderTransferObjectMarker
	</importMappings>
	```
	</details>

- The remaining configuration items can remain as they are, but are available for debugging and/or modification purposes.
	The options are set to produce only the interface classes and model classes. This allows flexibility for coding the resource class and dependent functionality.

	<details><summary>XML Snippet</summary>

	```xml
	<addCompileSourceRoot>false</addCompileSourceRoot>
	<configHelp>false</configHelp>
	<verbose>false</verbose>
	<generatorName>spring</generatorName>
	<generateApiDocumentation>false</generateApiDocumentation>
	<generateApiTests>false</generateApiTests>
	<generateApis>true</generateApis>
	<generateModels>true</generateModels>
	<generateModelTests>false</generateModelTests>
	<generateModelDocumentation>false</generateModelDocumentation>
	<generateSupportingFiles>false</generateSupportingFiles>
	<languageSpecificPrimitives>true</languageSpecificPrimitives>
	<templateDirectory>${basedir}/src/main/resources/openapi/mustache</templateDirectory>

	<configOptions>
		<serializableModel>true</serializableModel>
		<sourceFolder>openapi</sourceFolder>
		<interfaceOnly>true</interfaceOnly>
		<implicitHeaders>false</implicitHeaders>
		<java8>false</java8>
		<useBeanValidation>true</useBeanValidation>
		<performBeanValidation>true</performBeanValidation>
		<useTags>true</useTags>
		<swaggerDocketConfig>false</swaggerDocketConfig>
	</configOptions>
	```
	</details>

- The profile file also includes a `<pluginManagement>` section that configures eclipse / STS lifecycle mapping for the openapiTools. This section does not require any changes.

## How to specify the API

- The API specification should be wholly contained within the `bip-[application-name]/src/main/resources/openapi/openapi.yml` file, without any external "ref" files. OpenAPI v3 still has outstanding issues around when and where `ref` can be used.

- The opening sections of the YAML file (api version, info, servers, tags) should retain the formatting, but must be updated to reflect the application. **The tags section can be added to as desired.**

	<details><summary>YAML Snippet</summary>

	```yml
	# https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md
	openapi: 3.0.1
	info:
		version: [api.verion.number[-SNAPSHOT]]
		title: BIP [Application Name] Service v1 API Documentation
		description: An API that [brief description of the purpose for the API]
		termsOfService: https://developer.va.gov/terms-of-service
	license:
		name: Apache 2.0
		url: https://www.apache.org/licenses/LICENSE-2.0
	servers:
		- url: http://localhost:8080
			description: Local server
	tags:
		- name: [application-name]
			description: [Application Name] Resource
		- name: [functional-name]-rest-client-tester
			description: [Functional Name] Rest Client Tester
		- name: token-resource
				description: Token Resource
	# ...
	```
	</details>

- Components must be declared in the YAML:
	- Declare the security scheme. For BIP service applications this is currently JWT. No changes should be made in this section

		<details><summary>YAML Snippet</summary>

		```yml
		components:
			securitySchemes:
				Authorization:
					type: apiKey
					name: Authorization
					in: header
				bearerAuth:
					type: http
					bearerFormat: JWT
					scheme: bearer
		```
		</details>

	- **Declare the application model schemas** for the data model classes and their properties.

		<details><summary>YAML Snippet</summary>

		```yml
		schemas:
			[ClassName]:
				type: object
				title: [ClassName]
				description: Model for data contained in the [functional model description]
				properties:
					[fieldName]:
						type: [OpenApiType]
						example: [exampleValue]
						description: [Brief description]
					# ... continued field definitions ...
			# ... continued class definitions ...
		```

	- Declare the schemas inherited from the framework. No changes should be made in this section.

	<details><summary>YAML Snippet</summary>

		```yml
		schemas:
			# Schema objects below are from BIP Framework to be declared.
			# These objects need to be mapped in <importMappings> section
			# of openapi-generator-maven-plugin so that no code is generated
			# for these classes
			Message:
				type: object
				required:
				- key
				- severity
				properties:
					key:
						type: string
					severity:
						type: string
					status:
						type: string
					text:
						type: string
					timestamp:
						type: string
						example: yyyy-MM-dd'T'HH:mm:ss.SSS
				title: Message
			Person:
				type: object
				properties:
					assuranceLevel:
						type: integer
						format: int32
						example: 2
						description: The person's access assurance level
					birthDate:
						type: string
						example: '1978-05-20'
						description: The person's birth date
					correlationIds:
						type: array
						example:
						- 77779102^NI^200M^USVHA^P
						- 912444689^PI^200BRLS^USVBA^A
						- 6666345^PI^200CORP^USVBA^A
						- 1105051936^NI^200DOD^USDOD^A
						- 912444689^SS
						description: The MVI correlation IDs list for the person
						items:
							type: string
					email:
						type: string
						example: jane.doe@va.gov
						description: The person's email address
					firstName:
						type: string
						example: JANE
						description: The person's first name
					gender:
						type: string
						example: FEMALE
						description: The person's gender
					lastName:
						type: string
						example: DOE
						description: The person's last name
					middleName:
						type: string
						example: M
						description: The person's middle name
					prefix:
						type: string
						example: Ms
						description: The prefix for the person's full name
					suffix:
						type: string
						example: S
						description: The suffix for the person's full name
					user:
						type: string
				title: Person
				description: Model that identifies a single individual used in the security context
			ProviderResponse:
				type: object
				properties:
					messages:
						type: array
						items:
							'$ref': '#/components/schemas/Message'
				title: ProviderResponse
			ProviderRequest:
				type: object
				title: ProviderRequest
		```
		</details>

- **Define the application API in the path section.** See [Paths Object](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md#pathsObject) in the specification, and the [reference application yml](https://github.com/department-of-veterans-affairs/bip-reference-person/tree/master/bip-reference-person/src/main/resources/openapi/openapi.yml) for a working example.


	<details><summary>YAML Snippet</summary>

	```yml
	paths:
		"/api/v1/[path/to/operation]":
			[post|get|put|delete]:
				tags:
				- [tag]
				summary: An endpoint [describe the endpoint operation]
				operationId: [resourceMethodName]
				requestBody:
						description: [requestClass]
						required: true
						content:
							application/json:
								schema:
									'$ref': '#/components/schemas/[ApplicationRequestClass]'
				responses:
					'200':
						description: OK
						content:
							application/json:
								schema:
									'$ref': '#/components/schemas/[ApplicationResponseClass]'
					'400':
						description: There was an error encountered processing the Request.	Response
							will contain a	"messages" element that will provide further information
							on the error.	This request shouldn't be retried until corrected.
						content:
							application/problem+json:
								schema:
									'$ref': '#/components/schemas/ProviderResponse'
					'403':
						description: The request is not authorized.	Please verify credentials used
							in the request.
						content:
							application/problem+json:
								schema:
									'$ref': '#/components/schemas/ProviderResponse'
					'500':
						description: There was an error encountered processing the Request.	Response
							will contain a	"messages" element that will provide further information
							on the error.	Please retry.	If problem persists, please contact support
							with a copy of the Response.
						content:
							application/problem+json:
								schema:
									'$ref': '#/components/schemas/ProviderResponse'
				security:
					- bearerAuth: []
				deprecated: false
	```
	</details>


### messages.properties and MessageKeys

Add messages to src/main/resources/messages.properties and reflect them in the MessageKeys enum.
-

	<details><summary>XML Snippet</summary>

	```xml

	```
	</details>

### .openapi-generator.ignore

-

	<details><summary>XML Snippet</summary>

	```xml

	```
	</details>

## Generated code

-

	<details><summary>XML Snippet</summary>

	```xml

	```
	</details>

### The interface

-

	<details><summary>XML Snippet</summary>

	```xml

	```
	</details>

### Integrating the application code

-

<details><summary>XML Snippet</summary>

```xml

```
</details>

# Migrating from Code First to Design First

-

	<details><summary>XML Snippet</summary>

	```xml

	```
	</details>

## Get the current API definition

-

	<details><summary>XML Snippet</summary>

	```xml

	```
	</details>

## Modify the current definition

-

	<details><summary>XML Snippet</summary>

	```xml

	```
	</details>

## Update maven configuration

-

	<details><summary>XML Snippet</summary>

	```xml

	```
	</details>

## Update classes and config

- REST Resource classes Model objects and dependents messages.properties / MessageKeys and dependents

	<details><summary>XML Snippet</summary>

	```xml

	```
	</details>

## Clean up

- Remove old annotations ?

	<details><summary>XML Snippet</summary>

	```xml

	```
	</details>
