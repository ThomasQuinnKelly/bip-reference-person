# Reference Person Service Performance Test 

Performance testing configurations (jmx files) go in this directory.

This module was created to facilitate the performance testing of reference person Service and has no other functionality.

This test will execute requests for the rest end points available in reference person service module.

-PID based Person Info from Person Partner Service - /api/v1/persons/pid
-Person service Health check endpoint - /api/v1/persons/health

Additionally it will also hit the /token end point to generate JWT token for the users.

## Performance Test Configuration

## Reference Person Service Perftest we can use the below configuration value to execute the test in pipeline.
The test suite can be configure to execute each test a different number of time and with different number of threads.
To override any of the below properties please pass them as -D arguments (ex. -Ddomain=(env))

Below is the typical configuration values, But reference person is using the default values in JMX file.

|Property|Description|Default Value|Perf Env Test Values|
|-|-|-|-|
|domain| Reference Person service Base Url|localhost| |
|port|Reference Person Service Port|8080|443 |
|protocol|Reference Person Service Protocol|http|https |
|PersonHealth.threadGroup.threads|Number of threads for Health Status|5|150|
|PersonHealth.threadGroup.rampUp|Thead ramp up|2|150|
|PersonHealth.threadGroup.loopCount|Number of executions|10|-1|
|PersonHealth.threadGroup.duration|Scheduler Duration in seconds|200|230|
|PersonHealth.threadGroup.startUpDelay|Delay to Start|5|30|
|PersonInfo.threadGroup.threads|Number of threads for PID based Person Info|5|150|
|PersonInfo.threadGroup.rampUp|Thead ramp up|2|150|
|PersonInfo.threadGroup.loopCount|Number of executions|10|-1|
|PersonInfo.threadGroup.duration|Scheduler Duration in seconds|200|230|
|PersonInfo.threadGroup.startUpDelay|Delay to Start|2|30|
|PersonInfoNoRecordFound.threadGroup.threads|Number of threads PID based Person Info with No Record Found PID|5|150|
|PersonInfoNoRecordFound.threadGroup.rampUp|Thead ramp up|2|150|
|PersonInfoNoRecordFound.threadGroup.loopCount|Number of executions |10|-1|
|PersonInfoNoRecordFound.threadGroup.duration|Scheduler Duration in seconds|200|230|
|PersonInfoNoRecordFound.threadGroup.startUpDelay|Delay to Start|2|30|
|PersonInfoInvalidPid.threadGroup.threads|Number of threads PID based Person Info with Invalid PID|5|150|
|PersonInfoInvalidPid.threadGroup.rampUp|Thead ramp up|2|150|
|PersonInfoInvalidPid.threadGroup.loopCount|Number of executions |10|-1|
|PersonInfoInvalidPid.threadGroup.duration|Scheduler Duration in seconds|200|230|
|PersonInfoInvalidPid.threadGroup.startUpDelay|Delay to Start|2|30|
|PersonInfoNullPid.threadGroup.threads|Number of threads PID based Person Info with null PID|5|150|
|PersonInfoNullPid.threadGroup.rampUp|Thead ramp up|2|150|
|PersonInfoNullPid.threadGroup.loopCount|Number of executions |10|-1|
|PersonInfoNullPid.threadGroup.duration|Scheduler Duration in seconds|200|230|
|PersonInfoNullPid.threadGroup.startUpDelay|Delay to Start|2|30|
|BearerTokenCreate.threadGroup.threads|Number of threads for Bearer Token Create/Generate|5|150|
|BearerTokenCreate.threadGroup.rampUp|Thead ramp up|1|50|
|BearerTokenCreate.threadGroup.loopCount|Number of executions |1|1|

## Performance Test Execution

To execute test locally simply run **mvn clean verify -Pperftest**. If you need to override any of the properties please use -D arguments with the arguments mention above.

Sample Command for executing the test in performance test environment: 

**mvn clean verify -Pperftest -Dprotocol=<> -Ddomain=<> -Dport=<> -DBearerTokenCreate.threadGroup.threads=150 
 -DBearerTokenCreate.threadGroup.rampUp=50 -DBearerTokenCreate.threadGroup.loopCount=1 -DPersonHealth.threadGroup.threads=150 -DPersonHealth.threadGroup.rampUp=150 -DPersonHealth.threadGroup.loopCount=-1 -DPersonHealth.threadGroup.duration=230 -DPersonHealth.threadGroup.startUpDelay=30 -DPersonInfo.threadGroup.threads=150 -DPersonInfo.threadGroup.rampUp=150 -DPersonInfo.threadGroup.loopCount=-1 -DPersonInfo.threadGroup.duration=230 -DPersonInfo.threadGroup.startUpDelay=30 -DPersonInfoNoRecordFound.threadGroup.threads=150 -DPersonInfoNoRecordFound.threadGroup.rampUp=150 -DPersonInfoNoRecordFound.threadGroup.loopCount=-1 -DPersonInfoNoRecordFound.threadGroup.duration=230 -DPersonInfoNoRecordFound.threadGroup.startUpDelay=30 -DPersonInfoInvalidPid.threadGroup.threads=150 -DPersonInfoInvalidPid.threadGroup.rampUp=150 -DPersonInfoInvalidPid.threadGroup.loopCount=-1 -DPersonInfoInvalidPid.threadGroup.duration=230 -DPersonInfoInvalidPid.threadGroup.startUpDelay=30 -DPersonInfoNullPid.threadGroup.threads=150 -DPersonInfoNullPid.threadGroup.rampUp=150 -DPersonInfoNullPid.threadGroup.loopCount=-1 -DPersonInfoNullPid.threadGroup.duration=230 -DPersonInfoNullPid.threadGroup.startUpDelay=30**

For developing the performance test, use the GUI mode. For the actual test execution, use of NON-GUI mode is recommended.
