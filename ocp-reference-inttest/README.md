This document provides the details of **Reference Person Service Functional Testing**.

## Functional test for Reference Person Service ##
Functional test are created to make sure the end points in reference person are working as expected.

Project uses Java - Maven platform, the REST-Assured jars for core API validations.

## Project Structure ##

src/inttest/resources/gov/va/referenceperson/feature - This is where you will create the cucumber feature files that contain the Feature
and Scenarios for the Reference service you are testing.

src/inttest/java/gov/va/ocp/reference/service/steps- The implementation steps related to the feature
and scenarios mentioned in the cucumber file for the API needs to be created in this location. 

src/inttest/java/gov/va/ocp/reference/service/runner - Cucumber runner class that contains all feature file entries that needs to be executed at runtime.
The annotations provided in the cucumber runner class will assist in bridging the features to step definitions.

src/inttest/resources/request/dev – This folder contains DEV request files if the body of your API call is static and needs to be sent as a XML/JSON file.

src/inttest/resources/request/va – This folder contains VA request files if the body of your API call is static and needs to be sent as a XML/JSON file.

src/inttest/resources/response – This folder contains response files that you may need to compare output, you can store the Response files in this folder. 


src/test/resources/users/dev: All the property files for DEV users should go under this folder.

src/test/resources/users/va: All the property files for VA users should go under this folder.

src/inttest/resources/logback-test.xml - Logback Console Appender pattern and loggers defined for this project

src/inttest/resources/config/referenceperson-dev.properties – DEV configuration properties such as URL are specified here.

src/inttest/resources/config/referenceperson-stage.properties – STAGE configuration properties such as URL are specified here.

## Execution ##
1. In ocp-reference-person/src/main/resources/ocp-reference-person.yml file JWT by default is set to false, enable that to true.  
 
 os.reference:
  security:
    jwt:
      enabled: true

2. To execute the functional test in local ocp-reference-person service needs to be up and running.

# How to Build and Test reference-person service ##
[quick-start-guide](/docs/quick-start-guide.md)

**Command Line:** Use this command(s) to execute the reference person service Functional test. 

 Default Local: mvn verify -Pinttest -Dcucumber.options="--tags @DEV"
 

## More Details For Functional Test ##
 [Read Guide](/docs/referenceperson-intest.md)

