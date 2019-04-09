## Reference Person Service Performance Test Plan

## Capability and Features

- Speed -Determines whether the application responds quickly
- Scalability -Determines maximum user load the software application can handle.
- Stability -Determines if the application is stable under varying loads
- A Detailed report can be generated with the help of a dashboard report.
- Executable in multiple environments.

## Test plan in JMeter

A Test Plan can be viewed as a container for running tests. It defines what to test and how to go about it. A complete test plan consists of one or more elements such as thread groups, HTTP Request Default, User Defined Variables, listeners, timers, assertions, and configuration elements. A test plan must have at least one thread group.

Created a test plan in JMeter with Reference Person Service endpoints which can be configured individually. This document provides the details of the lists that are included in the test plan. Also, provides the detail of report that are generated after the test execution. 

The Reference Person Service performance test uses JMeter Maven plugin for executing the JMeter test. [Reference Person Service Test Plan](/jmeter/ReferencePersonServicePerformanceTestPlan.jmx)

Pom.xml has all the dependencies and user properties.[Reference Person POM](/bip-reference-perftest/pom.xml) . 

If you need to override any of the properties please use -D argument with appropriate parameter name. Eg: -DBearerTokenCreate.threadGroup.loopCount=1 
[Parameter Configuration Docs](/bip-reference-perftest/README.md) . 

The Maven properties are passed to JMeter via the userProperties option. Inside a JMeter test, you can then access the properties using the function ${__P(propertyName)}.Eg: ${__P(BearerTokenCreate.threadGroup.threads,5)}

This test will execute requests for the person service REST endpoints available in the reference person module.

 -PID based Person Info from Person Partner Service - /api/v1/persons/pid

 -Person service Health check endpoint - /api/v1/persons/health

Also, it will hit the /token endpoint to generate JWT token for the users before hits REST end point.


## Steps to install JMeter:

 * The latest version of Jmeter is 5.0.

 * You can download it https://jmeter.apache.org/download_jmeter.cgi 

 * Choose the Binaries file (either zip or tgz) to download.

 * unzip the zip/tar file into the directory where you want JMeter to be installed.

 * Once the unzipping is done get to the installation directory.

 * To start JMeter in GUI mode. Click on bin folder and double-click click on Jmeter exec file.


## Project Structure:

src/test/jmeter/ReferencePersonServicePerformanceTestPlan.jmx: JMX files are located in this folder. [Reference Person Service Test Plan](/jmeter/ReferencePersonServicePerformanceTestPlan.jmx)

src/test/jmeter/users: This folder has a list of text files. Each file has JSON header info for each concurrent user that will be used in the bearer token API. CSV files have a list of values.

[Reference Person Service Users](/jmeter/users)

## Target Folder Usage:

After the test execution, the test report and logs can be viewed in the target folder.

look in target/jmeter/report/ for the JMeter results file

look in target/jmeter/logs / for the JMeter log file

 ## Load Performance Test plan:

Load testing is performed to determine a system’s behavior under both normal and at peak conditions. It helps to identify the maximum operating capacity of an application as well as any bottlenecks and determine which element is causing degradation. For e.g., If the number of users is increased then what is the response time, throughput time and elapsed time for requests.

## Reference Person Service Performance Test Plan:

Below are the lists that are included in the Reference person service Test Plan. Using the below list a test plan can be created for any services.

1. HTTP Request Default

2. User Defined Variables

3. Bearer Token Endpoint

   A. Regular Expression Extractor

   B. BeanShell PostProcessor

   C. CSV Data Set Config

4. Thread Group

   A. CSV DataSet Config

   B. HTTP Request

   C. HTTP Header Manager

   D. Response Assertion

   E. View Result Tree

5. Tear Down Thread Group

1. HTTP Request Default:

If you need to test dozens of HTTP URL’s, then instead of adding the webserver's hostname or proxy details ,etc in all HTTP Request Samplers, you can put these in the Config Element HTTP Request Defaults. This will avoid repetition of data and also make the config manageable. In reference person service test plan we have used HTTP request default for protocol, server name, and port number.

2. User Defined Variables:

User-defined variables are used to defined specific variables which can store some values which you need in different places. We have used this for protocol, server name, and port number to have some default values.

3. Bearer Token Endpoint:

It fetches token from the token API. The token will be used as a header while invoking actual endpoints. The response of the bearer token endpoint is extracted using regular expression extractor so we could reuse the value later.

A. Regular Expression Extractor:

In JMeter, the Regular Expression Extractor is useful for extracting information from the response. With JMeter, you could use regex to extract values from the response during test execution and store it in a variable (also called as a reference name) for further use. Regular Expression Extractor is a post-processor that can be used to apply regex on response data. The matched expression derived on applying the regex can then be used in a different sampler dynamically in the test plan execution. We used this post processor for extracting the response from bearer token endpoint.

B. BeanShell PostProcessor:

It is a post process to store the output of bearer token endpoint to CSV file. A post-processor that is executed after the sampler and can be used for recovery or clean-up. We used this bean shell post processor to set values globally so other thread groups can use this.

C. CSV Data Set Config:

CSV data set allows us to define external dataset in CSV file. In our case, concurrent users are listed in the CSV file. JMeter will read the CSV file line by line, and then use values for different threads in the current thread group. The current value will be given to bearer token endpoint thread. The file will be shared between all threads and each request will read one line in the CSV file, in sequential order.

4. Thread Group:

Thread Group elements are the beginning points of the test plan. The thread group elements control the number of threads JMeter will use during the test. We can also control the following with the Thread Group−

Setting the number of threads
Setting the ramp-up time
Setting the number of test iteration
Scheduler Configuration : Duration (Seconds) and Startup delay (seconds)
Each endpoints are independent on its own thread group. Thread properties and scheduler configuration are declared as a function with a default value in it. If we need to override the values during the test execution add -D parameter with the properties name and value. Each Thread group has a list of

- CSV Data Set Config
- HTTP request
- HTTP Header Manager
- Response Assertion
- View Result Tree

A. CSV DataSet Config:

CSV data set allows us to define external dataset in CSV file. In our case, bearer tokens is listed in the CSV file. JMeter will read the CSV file line by line, and then use values for different threads in the current thread group. The current value of token will be given to end point thread. The file will be shared between all threads and each request will read one line in the CSV file.

B. HTTP Request:

This sampler allows sending an HTTP/HTTPS request to a web server. We are sending multiple requests to the same web server, then instead of adding the webserver's hostname or proxy details ,etc in all HTTP Request Samplers, you can put these in the Config Element HTTP Request Defaults. This will avoid repetition of data and also make the config manageable.

C. HTTP Header Manager:

The Header Manager lets you add or override HTTP request headers.

D. Response Assertion:

The response assertion control panel lets to add pattern strings to be compared against various fields of the request or response. The pattern strings are Contains, Matches: Equals, Substring. In our case, we are asserting the response code. 

E. View Result Tree:

The View Results Tree listener displays samples that the JMeter samplers generate, and the assertion results that are related to these samples. This listener displays the samples in the order they are generated by the JMeter scripts and provides parameters and data for each of them. Result tree is used if the test are executed in GUI mode.

5. Tear Down Thread Group:

It is a special form of Thread Group used to perform necessary actions after the execution of regular thread group completes. In our case, we delete the token.CSV file once the test is completed.

## Reporting:

## Dashboard Report:

For all test plan after execution of performance test, it generates a dashboard report.

JMeter supports dashboard report generation to get graphs and statistics from a test plan. The dashboard generator is a modular extension of JMeter. Its default behavior is to read and process samples from CSV files to generate HTML files containing graph views. It can generate the report at the end of a load test or on demand.

For more info on Dashboard report: http://jmeter.apache.org/usermanual/generating-dashboard.html
