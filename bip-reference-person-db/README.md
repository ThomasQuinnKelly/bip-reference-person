## What is this repository for?

This project attempts to demonstrate a relatively generic pattern that should be able to serve any BIP project as a starting point for database and related java development. There are hundreds of ways to set up and configure a project like this. Here, we have strived to provide some best practices, and some suggestions for maintainable solutions to complex problems.

Use this project as a guide to set up, configure, automate, and test things such as one-time schema or data updates, datasets for testing, enhancements, on-going manual maintenance operations, etc.

## About the database project

The project consists of configurations working together using the liquibase-maven-plugin that can be used to perform repeatable or one-shot operations on databases. It provides suggestions and example implementations that can be used as a starting point, and that can be tailored to the specific needs of any project.

- Utilizes dependency management provided by the bip-framework-parent-pom project to help manage TRM compliance and product interoperability.

- Demonstrates how to use maven profiles to configure and execute various Liquibase "change" operations.

- Shows examples of how to use liquibase parameters in changelogs to dynamically execute specific operations.

- Shows examples of how to set or override parameter values using a properties file.

**Note that** a common `db.changelog-master` file is not used in this project. The intent is for each maven profile to execute independently, therefore each profile configures its own paths for any needed `*.properties` file, changelogs (`input` or `output`), and `data`.  In the service project a common master changelog is required and should be configured.

For detailed information about database configuration in BIP projects, see [Databases in BIP](https://github.com/department-of-veterans-affairs/bip-reference-person/tree/master/docs/database-config-usage.md).

## How to add the dependency

The database project is one of the sub-project `<modules>` in a reactor project.  Add the the database project to the reactor POM.
```xml
<module>bip-reference-person-db</module>
```

## Diagrams

#### Configuration Hierarchy Diagram
TBD
