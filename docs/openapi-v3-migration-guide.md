# Migrating from BIP Framework 1.x to BIP Framework 2.x

This document describes the steps required to migrate an existing BIP service from the "code first" swagger API documentation to the "design first" OpenAPI v3 documentation.

If you are creating a new service, please see [BIP OpenAPI v3 Developer Guide](./openapi-v3-developer-guide.md).

There are a variety of ways to upgrade an API specification from Swagger 2 to OpenAPI v3. For the purposes of BIP Framework 1.x applications, there are two approaches that are relatively straight forward.

## Reference Information

The [BIP OpenAPI v3 Developer Guide](./openapi-v3-developer-guide) provides a great deal of useful information that is relevant to the upgrade/conversion effort. It is worth reviewing the document, and will occasionally be referenced in the sections below.

## API Conversion, Option 1: SwaggerHub

SwaggerHub allows you to upgrade an application specification, and natively represent the converted spec interpreted in swagger-ui. This is the recommended approach.

For the developer performing the upgrade, it is recommended to watch the video at [How to Convert to OAS 3.0 with Swagger Tools](https://swagger.io/resources/webinars/convert-api-to-oas-3-with-swagger-tools/). The video is 1 hour long, and parts of it do not apply directly to the BIP application upgrade - pay attention in the parts that speak to conversion of "Swagger Core" (the Code First style used in BIP Framework 1.x), and the post-conversion processes.

It is your choice to use the local NPMs of SwaggerHub, or to use the online versions - installing the local NPMs may ultimately be easier, as it can access localhost URLs. The Swagger Editor used by SwaggerHub will automatically convert the JSON representation of the app spec into YAML.

Steps:
- Manually get the Swagger 2 JSON spec (see [Get the existing Swagger 2 application spec](#Get-the-existing-Swagger-2-application-spec) below), **OR** point SwaggerHub to a running instance of the app.
- View the converted spec in swagger-ui. Attention to detail is required in this step, and will require an iterative change-confirm cycle.
- When the entire API seems correct, have others review the v3 spec, and make any necessary changes

Save a copy of the completed spec in a safe place, then continue with [Application Updates](#Application-Updates) below.

## API Conversion, Option 2: Manual Conversion

Conversion can be performed manually.

1. Get the existing Swagger 2 application spec
- Make sure the current project builds and runs as desired before the cut-over to the new version of bip-framework
- Run the application and confirm the swagger UI comes up: http://localhost:8080/swagger-ui.html
- In the browser, enter http://localhost:8080/swagger-ui.html to bring up the swagger-ui page.
	- Under the page title should be a URL link for the current running version. Click it. The JSON specification for the application will open.
	- Copy the JSON by clicking the little "Copy" button (your choice to do this on the formatted JSON tab or the RAW tab), and save a backup of the JSON somewhere safe

	```json
	{"swagger":"2.0","info":{"version":"[VERSION]-SNAPSHOT","title":"BIP VetServices [Application Name] API Documentation"},"host":"localhost:8080","basePath":"/","...":"...etc..."}
	```

- Convert the JSON to YAML. There are many ways to do this. A couple examples:
	- https://swagger.io/tools/swagger-editor/ - paste the JSON into the editor, it will automatically convert to YAML
	- https://codebeautify.org/json-to-yaml (there are many of this type, google: json to yaml)
	- https://www.npmjs.com/package/json2yaml (an NPM to be installed locally)

	```yaml
	swagger: '2.0'
	info:
	  version: '[VERSION]-SNAPSHOT'
	  title: 'BIP VetServices [Application Name] API Documentation'
	host: 'localhost:8080'
	basePath: /
	...: ...etc...
	```
2. Convert the Swagger 2 spec to OpenAPI v3

- Convert the Swagger 2 API spec to an OpenAPI v3 spec with a tool:
	- [OpenAPI GUI](https://mermade.github.io/openapi-gui/) or
	- [Mermade Converter](https://mermade.org.uk/openapi-converter) or
	- other possible options available at [OpenAPI.Tools](https://openapi.tools/) that will not be discussed here

3. Validate and refine the OpenAPI v3 application spec

- Review the [BIP OpenAPI v3 Developer Guide - How to specify the API](./openapi-v3-developer-guide.md#How-to-specify-the-API) sections for guidance on how to specify the opening sections and security.
- Use a tool to edit and validate the specification:
	- [OpenAPI GUI](https://mermade.github.io/openapi-gui/) (use the Upload feature) or
	- [Swagger Editor](https://github.com/swagger-api/swagger-editor) or
	- other possible options available at [OpenAPI.Tools](https://openapi.tools/) that will not be discussed here

Save a copy of the completed spec in a safe place, then continue with [Application Updates](#Application-Updates) below.

## Application Updates

#### Maven configuration

- Add the Maven configuration to your POM files, as described in [BIP OpenAPI v3 Developer Guide - Application Configuration](./openapi-v3-developer-guide.md#Application-Configuration)

## Clean up

**NOTE:** configuration references to swagger-ui and related paths, and `gov.va.bip.framework.*Swagger*` classes have not changed.

Also, Junit tests may need to be modified later after the maven build runs - `@Ignore` or delete tests as seems reasonable.

- Remove all Swagger 2 references from the project(s). Use the full-text search in your IDE to search for:
	- "springfox"
	- "io.swagger"
- In POM files, delete any properties, plugins and other swagger / springfox sections that will no longer be used.
- Delete the api `model/v?/` package/directory.
- Delete any remaining the references in java files, and any annotations related to the imports.
- At some point, update any documentation related to the swagger-ui and the API documentation.

## Update classes and config

- Update `messages.properties` to conform to the JSR303 standards as documented in [BIP OpenAPI v3 Developer Guide - JSR303 message overrides in messages.properties](./openapi-v3-developer-guide.md#jsr303-message-overrides-in-messagesproperties)

## Build

When maven builds are performed, if you find service classes get overwritten, you can tell openapi-generator to ignore (not overwrite) the files. See [BIP OpenAPI v3 Developer Guide - .openapi-generator-ignore](./openapi-v3-developer-guide.md#openapi-generator-ignore)

It may be necessary at times to tell Maven to update the artifacts in its local repo. Force maven to update libraries:
- In STS, right-click your reactor project and select _Maven > Update Project... > Force Update of Snapshots/Releases_.
- At command line, add `-U`, for example: `$ mvn clean install -U`

It may be necessary to run the build more than one time for necessary artifacts to get generated correctly.

## Test and refine

- Build the project and run it. Resolve any issues.
- Test through the swagger-ui http://localhost:8080/swagger-ui.html. Resolve any issues.
- Update documentation to reflect the new Design First approach.
