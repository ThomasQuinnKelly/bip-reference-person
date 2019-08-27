# What is the `db` directory for?

This directory contains the input files and output files for Liquibase.

Currently, Liquibase is run only by invoking an appropriate maven profile. To see all profiles for a project open a terminal and navigate to the project, then run `mvn help:all-profiles`.

The profiles are set up to utilize specific property files of the same name as the profile. These property files determine what datasource(s) and potentially other settings will be used when you run the profile. It is important to confirm that the properties file is set as you need it before executing any of the maven profiles.

Details on databases in BIP, and the liquibase configurations, is available in the bip-reference-person [Databases in BIP](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/docs/database-config-usage.md) document.
