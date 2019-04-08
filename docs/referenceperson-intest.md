# Reference Person Service Integration Test

## Capability and Features

- Feature files written by non developers can be used directly in development process.
- Multiple scenarios under one feature can be grouped as one feature file.
- Step definitions can be reused.
- One step definition for each feature file.
- Detailed report can be generated with the help of cucumber-maven plugin.
- Executable in multiple environments.

## Inttest Pattern 

This project is customized to support testing the REST Services used by Reference applications. It uses bip-framework-test-lib project that contains all the core framework for supporting API testing. Cucumber tool is integrated to assist with the needs of Acceptance Test Driven Development (ATDD), BDD by capturing the Acceptance criteria and implementing the scenarios with automated tests. It can be easily integrated with Continuous Integration (CI) Pipeline.

This project can be used as a skeleton to start writing test cases.


## Cucumber:

Cucumber is a testing framework which supports Behaviour Driven Development (BDD). It lets us define application behaviour in plain meaningful English text using a simple grammar defined by a language called Gherkin. Cucumber itself is written inRuby, but it can be used to “test” code written in Ruby or other languages including but not limited to Java, C# and Python.

[Cucumber Layers](/docs/images/Cucumber-Layers.png)

## Feature file: 

A feature file describes the features of the system or a particular aspect of a feature. Cucumber test are grouped in to features.

## Sample Feature Definition Template 

@tag
         Feature: Title of your feature
         I want to use this template for my feature file

         @tag1
         Scenario: Title of your scenario
         Given I want to write a step with precondition
         And some other precondition
         When I complete action
         And some other action
         And yet another action
         Then I validate the outcomes
         And check more outcomes

         @tag2
         Scenario Outline: Title of your scenario outline
         Given I want to write a step with <name>
         When I check for the <value> in step
         Then I verify the <status> in step

          Examples: 
             | name  | value | status  |
             | name1 |     5 | success |
             | name2 |     7 | Fail    |

## Scenario:

Each Cucumber test is called a scenario, and each scenario contains steps that tell Cucumber what to do. Every scenario consists of a list of steps, which must start with one of the keywords Given, When, Then, But or And .

Given: This step is to put the system into a well-defined state before users start interacting with the application. A Given clause can by considered a precondition for the use case.

When: A When step is used to describe an event that happens to the application. This can be an action taken by users, or an event triggered by another system.

Then: This step is to specify an expected outcome of the test. The outcome should be related to business values of the feature under test.

And and But: These keywords can be used to replace the above step keywords when there are multiple steps of the same type.

## Scenario Outline: 

Same scenario can be executed for multiple sets of data using scenario outline. Variables in the Scenario Outline steps are marked up with < and >. A Scenario Outline section is always followed by one or more Examples sections, which are a container for a table. The table must have a header row corresponding to the variables in the Scenario Outline steps. Each of the rows below will create a new Scenario, filling in the variable values.

## Step Definition:

A step definition is an annotated Java method with an attached pattern whose job is to convert Gherkin steps in plain text to executable code. After parsing a feature document, Cucumber will search for step definitions that match predefined Gherkin steps to execute.

## Tags:

Tags are a way to group Scenarios. They are @-prefixed strings we can place as many tags above Feature, Scenario, Scenario Outline or Examples keywords. Space character are invalid in tags and may separate them.

## Data Table:

It is a way to pass the list of values to step definition as input. Multiple data condition can be passed against one scenario.

PID based Person Info from Person Partner Service for valid PID. In the below example multiple conditions are passed against one scenario.

Examples: 
      
      | Veteran           | tokenrequestfile               | ServiceURL          | RequestFile               | participantID |
      | dev-janedoe       | dev/janedoetoken.request       | /api/v1/persons/pid | dev/janedoe.request       |       6666345 |
      | dev-russellwatson | dev/russellwatsontoken.request | /api/v1/persons/pid | dev/russellwatson.request |      13364995 |


## 	Inttest configuration

- Add dependency in pom.xml to include bip-framework-test-lib library

        <dependency>
		<groupId>gov.va.bip.framework</groupId>
		<artifactId>bip-framework-test-lib</artifactId>
		<version><!-- add the appropriate version --></version>
		</dependency>

- Add Maven Cucumber Reporting:
 
        <groupId>net.masterthought</groupId>
	    <artifactId>maven-cucumber-reporting</artifactId>

-Add maven Failsafe Plugin:
       
     <groupId>org.apache.maven.plugins</groupId>
	 <artifactId>maven-failsafe-plugin</artifactId>

-Add Spring Boot maven plugin
 
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-maven-plugin</artifactId>

## Reports

Cucumber JVM reports is configured to show the execution status of the tests run on Jenkins job.

Maven-Cucumber Reporting

Provides pretty html reports for Cucumber. It works by generating html from the cucumber json file. The result is at /target/site/cucumber-reports/cucumber-html-reports/overview-features.html

[Feature Reporting](/docs/images/feature-report.png)

[Tags Reporting](/docs/images/tag-reporting.jpg)


## References:

Cucumber : https://docs.cucumber.io
	