# Hystrix Circuit Breaker Management

## Recent Important Change for Hystrix

In December 2018,  Netflix made an important change in Hystrix’ README.md, announcing that the project `is no longer in active development, and is currently in maintenance mode`.  Spring Cloud also noted it and listed few Spring Cloud Netflix Projects Entering Maintenance Mode. Refer to below links

https://github.com/Netflix/Hystrix
https://spring.io/blog/2018/12/12/spring-cloud-greenwich-rc1-available-now#spring-cloud-netflix-projects-entering-maintenance-mode

In BIP framework, we will continue supporting Hystrix for few more releases, but also plan to add Resilience4J support as an alternative, which is proposed by Spring Cloud and Hystrix as well.

## Capability (Circuit Breaker Design Pattern: Hystrix)

- Circuit Breaker Design Pattern is to wrap a protected function call in a circuit breaker object, which monitors for failures. Once the failures reach a certain threshold, the circuit breaker trips, and all further calls to the circuit breaker return with an error or with some alternative service or default message, without the protected call being made at all. This will make sure system is responsive and threads are not waiting for an unresponsive call.

- FallBack methods are to enable fault tolerance in the application where some underlying service is down/throwing error permanently, we need to fall back to different path of program execution automatically. 

- The circuit breaker has three distinct states: Closed, Open, and Half-Open

- Hystrix is a tool to implement Circuit Breaker Design Pattern. 

- Service implementation class must define a Thread Pool (Group Key) and unique Command Keys for each method.

- If a command key isn't defined for a method, then by default name of command key is command method name. For example , getStates but you can rename it to GetStatesRefDataCommand

- **Group Key naming** convention must follow pattern `<<repository-name+Group>>` with camel case letters. For example, "VetsAPIRefDataGroup"

- **Command Key naming** convention must follow pattern as `<<method-name+repo-name+Command>>` with camel case letters. For example, method getStates would have a command key as "GetStatesRefDataCommand"

## Hystrix Server configuration
- Server side Hystrix design pattern is implemented with a fallBack at the ServiceImpl class level. There is provision to ignore certain exceptions from invoking the fallBack method. Caching is disabled once fallBack is invoked. 

- To configure Hystrix at the Server level add the following dependency in your project and add the bip-reference-autoconfigure dependency to the project pom, with the appropriate version to get all autoconfiguration projects:

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

- Update the application service yml file with the following configuration (under the default profile):

```yaml
hystrix:
    wrappers.enabled: true
    command:
        default:
            metrics:
                rollingStats:
                    timeInMilliseconds: 20000
                healthSnapshot:
                    intervalInMilliseconds: 1000
                circuitBreaker:
                    sleepWindowInMilliseconds: 5000
                    requestVolumeThreshold: 20
            execution:
                isolation:
                    thread:
                        timeoutInMilliseconds: 20000
```

- Use below configurations on the actual method and fallBack methods in the ServiceImpl class:

Actual method:
```java
@HystrixCommand(fallbackMethod = "findPersonByParticipantIDFallBack", commandKey = "GetPersonInfoByPIDCommand",
	ignoreExceptions = { IllegalArgumentException.class })
```

FallBack method:
```java
@HystrixCommand(commandKey = "FindPersonByParticipantIDFallBackCommand")
```

## Hystrix Client configuration

- Client side Hystrix Pattern: Client side Hystrix design pattern is implemented with a fallBack at 
the Feign Client level. There is provision to ignore certain exceptions from invoking the fallBack
method.

- To configure Hystrix at the Client level add the following dependency in your project and add the bip-reference-autoconfigure dependency to the project pom, with the appropriate version to get all autoconfiguration projects:

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

- Update the application service yml file with the following configuration (under the default profile):
```
feign.hystrix.enabled: true
```

- Use below configurations on the actual method in the Feign Client class. FallBack is implemented with a factory class
```java
@FeignClient(value = "${spring.application.name}",
url="${bip-reference-person.ribbon.listOfServers:}",
name = "${spring.application.name}",
fallbackFactory = FeignPersonClientFallbackFactory.class,
configuration = ReferenceServiceFeignConfig.class)
```
