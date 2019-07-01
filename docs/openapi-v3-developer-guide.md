# BIP OpenAPI v3 Developer Guide

OpenAPI is the open source successor to the old Swagger specification. The framework team has spent time investigating the two main approaches to using OpenAPI, and any issues that arise. There is value in being aware of [the research and its findings](https://github.com/department-of-veterans-affairs/bip-reference-person/tree/master/docs/openapi-v3-api-code-generation-journey.md).

BIP Framework began with support for the legacy "Code First" approach. With the move to OpenAPI v3, the framework is changing its API model to the "Design First" approach. In many ways this makes it easier for new projects, but imposes an upgrade requirement on existing projects.

This document describes how to use OpenAPI v3 in service applications built on BIP Framework, specifically:

- How framework prepares OpenAPI for you at build time
- Application configuration
- How to specify an API for BIP consumption
- What code gets generated, and best practices for integrating that code into your application
- Steps to migrate Code First applications to the new API paradigm

If you are migrating a BIP application from an earlier version of the framework, please see [Migrating from BIP Framework 1.x to BIP Framework 2.x](./openapi-v3-migration-guide.md).

## References

In addition to the [bip-reference-person] application and documentation, these sites provide excellent background for developers and tech leads:

- [OpenAPI Specification v 3.0.1](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md)
- [OpenAPI Tools](https://github.com/OpenAPITools) - of particular interest:
	- [openapi-generator-maven-plugin](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-maven-plugin)
	- [openapi-generator](https://github.com/OpenAPITools/openapi-generator)
	- There is also an [openapi-petstore](https://github.com/OpenAPITools/openapi-petstore)

## Prerequisites

- Java 8
- Maven 3.5.4
- Git 2.18 or newer
- STS/Eclipse users **must** use [STS](https://spring.io/tools) 4.3 / [Eclipse](https://www.eclipse.org/downloads/) 4.12 or newer
- Developers are encouraged to upgrade any other tooling they use to current versions

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

- For eclipse / STS users, the POM also uses the m2e connector to add the generated artifacts to the source classpath. If the eclipse install does not already have the required connector, the developer can open the [`bip-framework-parentpom/pom.xml`](https://github.com/department-of-veterans-affairs/bip-framework/blob/master/bip-framework-parentpom/pom.xml) in the IDE, hover over the `<execution>` tag, and select to "Discover new m2e connectors" option.

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

- The service POM (e.g. `bip-[application-name]/pom.xml`) contains the build directives for OpenAPI. The directives are in a profile that is activated only when the `openapi.yml` file is available.

	It is important to note that the path to the openapi.yml file is also used by framework, so cannot be changed.

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

- The inputSpec, output, and ignoreFileOverride directories must remain unchanged

	<details><summary>XML Snippet</summary>

	```xml
	<inputSpec>${basedir}/src/main/resources/openapi/openapi.yml</inputSpec>
	<output>${project.build.directory}/generated-sources</output>
	<ignoreFileOverride>${basedir}/.openapi-generator-ignore</ignoreFileOverride>
	```
	</details>

- **Alter the packaging values** to specify the application API java packages

	<details><summary>XML Snippet</summary>

	```xml
	<apiPackage>gov.va.bip.[application.package].api</apiPackage>
	<modelPackage>gov.va.bip.[application.package].api.model.v1</modelPackage>
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
	The options are set to produce only the interface classes and model classes, and not the resource implementation. This allows flexibility for coding the resource class and dependent functionality.

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

- The `templateDirectory` contains path to the directory with mustache templates. Basically, the API interface we got after the generation is coming from `api.mustache` file. That’s the template for the `ReferencePersonApi` class. If you want to modify the interface class generated, then copy the same file from [OpenAPI JavaSpring](https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator/src/main/resources/JavaSpring) to under `${basedir}/src/main/resources/openapi/mustache` and make updates. Mustache is a very basic templating language, I don’t want to go into details but you can find out more [here](https://mustache.github.io/mustache.5.html). 

- The profile file also includes a `<pluginManagement>` section that configures eclipse / STS lifecycle mapping for the openapiTools. This section does not require any changes.

## How to specify the API

The API specification for a BIP service application should be wholly contained within the [`bip-[application-name]/src/main/resources/openapi/openapi.yml`](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-person/src/main/resources/openapi/openapi.yml) file, without any external "ref" files. OpenAPI v3 still has outstanding issues around when and where references to external specifications can be used.

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
			# securitySchemes are defined by the framework.
			# Service applications must configure as below.
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
			# Define API (provider) model objects. Use type, format and example;
			# use standard JSR303 constraints (required, min, max, etc) where it makes sense
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
			# ... app model schemas inserted here ...

			# Schema objects below are from BIP Framework to be declared.
			# These objects need to be mapped in <importMappings> section
			# of openapi-generator-maven-plugin so that no code is generated
			# for these classes
			Message:
				type: object
				title: Message
				description: Model that identifies a single individual used in the security context
				required:
				- key
				- severity
				properties:
					key:
						# possible values enumerated by gov.va.bip.framework.messages.MessageKeys
						type: string
					severity:
						# derived from gov.va.bip.framework.messages.MessageSeverity
						type: enum
							- INFO
							- WARN
							- ERROR
					status:
						type: integer
						format: int32
					text:
						type: string
					timestamp:
						type: string
						format: date-time
						example: yyyy-MM-dd'T'HH:mm:ss.SSS
			Person:
				type: object
				title: Person
				description: Model that identifies a single individual used in the security context
				properties:
					assuranceLevel:
						type: integer
						format: int32
						example: 2
						minimum: 0
						description: The person's access assurance level
					birthDate:
						type: string
						format: date
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

	- The structure of the `responses:` section should remain relatively static, with `schema:` ref appropriately adjusted
	- Any service path that is secured must replicate the `security:` section as-is

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

- Include the **/token** path as-is

	<details><summary>YAML Snippet</summary>

	```yml
	paths:
		# ... application paths inserted here ...

		# The token path should not require any changes
		"/api/v1/token":
			post:
				tags:
				- token-resource
				summary: Get JWT Token
				description: Get a JWT bearer token with 'person' data. Include MVI correlationIds if required by the target API.
				operationId: getTokenUsingPOST
				requestBody:
					description: |-
						Identity information for the authenticated user. CorrelationIds may be null or an empty array if the target API does not require it. Otherwise, correlationIds must be the list as retrieved from MVI: <table style=\"table-layout:auto;width:700px;text-align:left;background-color:#efefef;\"><tr><th>Common ID Name</th><th>Example ID</th><th>Type</th><th>Source</th><th>Issuer</th><th>Status</th><th </tr><tr><td>Participant ID (PID)</td><td>12345678</td><td>PI</td><td>200CORP</td><td>USVBA</td><td>A</td></tr><tr><td>File Number</td><td>123456789</td><td>PI</td><td>200BRLS</td><td>USVBA</td><td>A</td></tr><tr><td>ICN</td><td>1234567890V123456</td><td>NI</td><td>200M</td><td>USVHA</td><td>A</td></tr><tr><td>EDIPI / PNID</td><td>1234567890</td><td>NI</td><td>200DOD</td><td>USDOD</td><td>A</td></tr><tr><td>SSN</td><td>123456789</td><td>SS</td><td></td><td></td><td></td></tr></table>
					required: true
					content:
							application/json:
								schema:
									'$ref': '#/components/schemas/Person'
				responses:
					'200':
						description: A Response which indicates a successful Request.	The Response
							may contain "messages" that could describe warnings or further information.
						content:
							text/plain:
								schema:
									type: string
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
				deprecated: false
	```
	</details>

#### JSR303 message overrides in messages.properties

For constraints declared in the API specification, add messages to [`src/main/resources/messages.properties`](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-person/src/main/resources/messages.properties).

- To override messages for [JSR303 messages](https://beanvalidation.org/1.0/spec/#d0e5601), the property must follow the syntax shown below.
- For a message to be activated, the relevant constraints must exist in the model definition of `openapi.yml`.

```text
Property Name Syntax
	Syntax: ConstraintName[.className][.fieldName]

	ConstraintName: the name of the JSR303 constraint name
		Required. First letter capitalized, camel-case, equivalent to the annotation name
		Effect: filters constraint violations by the constraint name
		Example: NotNull
	className: the class to which the message specifically applies
		Optional. First letter lowercase, camel-case
		Effect: filters 'ConstraintName' violations that are generated by the specified class
		Example: someProviderModelClassname
	fieldName: filters 'ConstraintName'
		Optional. First letter lowercase, camel-case
		Effect: filters 'ConstraintName' violations that are generated by the specified field
			If no 'className' was specified, provides the message for any class with 'fieldName'
		Example: someFieldName
```

<details><summary>JSR303 Properties Snippet</summary>

```properties
#####################################################################
# Messages for JSR 303 Validation annotations
#####################################################################
# no args
Min.personInfoRequest.participantID=Participant ID must be greater than zero.
# no args
NotNull.personInfoRequest.participantID=PersonInfoRequest.participantID cannot be null.
# no args
NotNull.personInfoRequest=PersonInfoRequest Payload cannot be null.
```
</details>

#### .openapi-generator-ignore

OpenAPI Generator supports a .openapi-generator-ignore file, similar to .gitignore or .dockerignore you're probably already familiar with. With the ignore file, you can specify individual files or directories can be ignored.

The openapi-generator may at times overwrite files that we do not want overwritten. To prevent this behavior, edit the [`bip-[service-name]/.openapi-generator-ignore`](https://github.com/department-of-veterans-affairs/bip-reference-person/tree/master/bip-reference-person/.openapi-generator-ignore) file.

- The `.openapi-generator-ignore` file was created by openapi-generator, but will not overwrite additive edits to it.
- The file is hidden, so (for example) users of Eclipse / STS can find this file using the _Navigator_ view.
- The format and wildcards is similar to the git ignore file

<details><summary>Ignore File Snippet</summary>

```properties
# Swagger Codegen Ignore
# Lines beginning with a # are comments

# Exclude all recursively
# target/generated-sources/openapi/**

# Exclude specific patterns
target/generated-sources/openapi/**/client/*.java
target/generated-sources/openapi/**/**ApiController*.java
target/generated-sources/openapi/**/TokenResourceApi.java
```
</details>

## Build

When maven builds are performed, if you find service classes get overwritten, you can tell openapi-generator to ignore (not overwrite) the files. See [BIP OpenAPI v3 Developer Guide - .openapi-generator-ignore](./openapi-v3-developer-guide.md#openapi-generator-ignore)

It may be necessary at times to tell Maven to update the artifacts in its local repo. Force maven to update libraries:
- In STS, right-click your reactor project and select _Maven > Update Project... > Force Update of Snapshots/Releases_.
- At command line, add `-U`, for example: `$ mvn clean install -U`

It may be necessary to run the build more than one time for necessary artifacts to get generated correctly.

## Generated code

Each time a maven build runs for a service project, the API interface classes and model classes are rebuilt.
The final location of the generated classes is `target/generated-sources/openapi/**`.
This path is added to the classpath.

The interface classes declare the API for clients and the service, and contain the request mappings (paths), JSR303 annotations, and API documentation annotations.
The service must implement the generated interface(s) in its `@RestController` resource classes.

The model classes, are likewise annotated with the JSR303 and API documentation annotations.


## Integrating the application code

As discussed in [Developing with BIP Framework](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/docs/developing-with-bip-framework.md) and [Layer and Model Separation Design](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/docs/design-layer-separation.md), normal spring provider resource classes are used to present the OpenAPI interface(s) and model object(s) to the consumer. Service patterns should be upheld by accessing the service through a `ServiceAdapter` class. See the bip-reference-person [PersonResource](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-person/src/main/java/gov/va/bip/reference/person/api/provider/PersonResource.java) class.

<details><summary>Java Snippet</summary>

```java
@RestController
public class PersonResource implements ReferencePersonApi, SwaggerResponseMessages {

	/** Logger instance */
	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonResource.class);

	/** The root path to this resource */
	public static final String URL_PREFIX = "/api/v1/persons";

	@Autowired
	ServiceAdapter serviceAdapter;

	@Autowired
	BuildProperties buildProperties;

	@PostConstruct
	public void postConstruct() {
		// Print build properties
		LOGGER.info(buildProperties.getName());
		LOGGER.info(buildProperties.getVersion());
		LOGGER.info(buildProperties.getArtifact());
		LOGGER.info(buildProperties.getGroup());
	}

	/**
	 * Registers fields that should be allowed for data binding.
	 *
	 * @param binder  Spring-provided data binding context object.
	 */
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		binder.setAllowedFields(new String[] { "[fieldOne]", "[fieldTwo]", "[etc]" });
	}

	/**
	 * [Description of the purpose for the method]
	 * <p>
	 * CODING PRACTICE FOR RETURN TYPES - Platform auditing aspects support two
	 * return types.
	 * <ol>
	 * <li>An object that implements ProviderTransferObjectMarker, e.g.:
	 * PersonInfoResponse
	 * <li>An object of type ResponseEntity&lt;ProviderTransferObjectMarker&gt;,
	 * e.g. a ResponseEntity that wraps some class that implements
	 * ProviderTransferObjectMarker.
	 * </ol>
	 * The auditing aspect won't be triggered if the return type in not one of
	 * the above.
	 *
	 * @param [openapiModel]Request the person request
	 * @return the response
	 */
	@Override
	public ResponseEntity<[openapiModel]Response> methodName(
			@ApiParam(value = "[openapiModel]Request", required = true) @Valid @RequestBody final [PpenapiModel]Request [openapiModel]Request) {
		LOGGER.debug("method invoked");

		// call service through the service adapter
		[OpenapiModel]Response providerResponse = serviceAdapter.[methodName]Pid([openapiModel]Request);

		// send provider response back to consumer
		LOGGER.debug("Returning providerResponse to consumer");
		return new ResponseEntity<>(providerResponse, HttpStatus.OK);
	}

}
```
</details>
