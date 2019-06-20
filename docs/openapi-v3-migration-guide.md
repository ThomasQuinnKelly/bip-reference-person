# Migrating from BIP Framework 1.x to BIP Framework 2.x

This document describes the steps required to migrate an existing BIP service from the "code first" swagger API documentation to the "design first" OpenAPI v3 documentation.

If you are creating a new service, please see [BIP OpenAPI v3 Developer Guide](./openapi-v3-developer-guide.md).

## Code First to Design First

Migrating from Code First swagger to Design First openapi is not difficult, and the tedium can be reduced by using the tools available in the IDE.

The steps to migrate are:

- Get a copy of the generated swagger schema from current project, and put the file in a safe place
- Edit the swagger specification to be OpenAPI v3 compliant, and to build the interface and model classes as desired
- Add the edited specification to the service `resources/openapi` directory
- Remove all references to springfox and any springfox annotations
- Adjust message properties to reflect JSR303 naming standards
- Update service POM to reference the new framework parent POM, and declare the openapi plugins
- Test and adjust as necessary until correct interface and model classes are built, and appropriate documentation is generated

Details for each of these steps appears below.

## Get the current API definition

- Make sure the current project builds as desired before the cut-over to the new version of bip-framework
-

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
