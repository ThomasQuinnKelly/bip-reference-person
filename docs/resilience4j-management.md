# Resilience4j Management

## Capability (Circuit Breaker Design Pattern: Resilience4j)

- Resilience4j is a lightweight fault tolerance library inspired by `Netflix Hystrix`, but designed for Java 8 and functional programming. Lightweight, because the library only uses Vavr, which does not have any other external library dependencies. Netflix Hystrix, in contrast, has a compile dependency to Archaius which has many more external library dependencies such as Guava and Apache Commons Configuration.

- Netflix Hystrix is no longer in active development, and is currently in maintenance mode.

- Resilience4j CircuitBreaker is implemented via a finite state machine with three normal states: CLOSED, OPEN and HALF_OPEN and two special states DISABLED and FORCED_OPEN.

- BIP framework supports using Resilience4J Spring Boot 2 Starter. Spring Cloud Circuit Breaker doesn't offer annotations at this time. However, the Spring Boot starter of Resilience4J (not part of Spring Cloud) allows, among other features, to use annotations. By using Resilience4J spring boot starter, we lose the layer of abstraction offered by Spring Cloud Circuit Breaker (i.e. to replace resilience4j by another implementation). 

## Resilience4j configuration
- Server side resilience4j design pattern is implemented with a fallBack at the ServiceImpl class level. There is provision to ignore certain exceptions from invoking the fallBack method. 

- To configure resilience4j at the server level, add the `bip-framework-autoconfigure` dependency to the project pom, with the appropriate version to get the required dependency libraries namely `resilience4j-spring-boot2` and `resilience4j-feign`

- Update the application service YAML file with the following configuration (under the default profile):

```yaml
	#################################################
	# resilience4j settings
	##################################################
	management.health.circuitbreakers.enabled: true
	management.health.ratelimiters.enabled: true
	management.metrics.distribution.percentiles-histogram.resilience4j.circuitbreaker.calls: true
	resilience4j.circuitbreaker:
	  configs:
	    default:
	        registerHealthIndicator: false
	        slidingWindowSize: 10
	        minimumNumberOfCalls: 5
	        permittedNumberOfCallsInHalfOpenState: 3
	        automaticTransitionFromOpenToHalfOpenEnabled: true
	        waitDurationInOpenState: 2s
	        failureRateThreshold: 50
	        eventConsumerBufferSize: 10
	        ignoreExceptions:
	           - feign.FeignException
	           - java.lang.IllegalArgumentException 
	           - gov.va.bip.framework.exception.BipException
	           - gov.va.bip.framework.exception.BipRuntimeException
	    shared:
	        registerHealthIndicator: true
	        slidingWindowSize: 100
	        permittedNumberOfCallsInHalfOpenState: 30
	        waitDurationInOpenState: 1s
	        failureRateThreshold: 50
	        eventConsumerBufferSize: 10
	        ignoreExceptions:
	           - feign.FeignException
	           - java.lang.IllegalArgumentException 
	           - gov.va.bip.framework.exception.BipException
	           - gov.va.bip.framework.exception.BipRuntimeException 
	resilience4j.ratelimiter:
	  configs:
	    default:
	      registerHealthIndicator: true
	resilience4j.retry:
	  configs:
	    default:
	      maxRetryAttempts: 2
	      waitDuration: 100
	      retryExceptions:
	        - org.springframework.web.client.HttpServerErrorException
	        - java.io.IOException
```

- Use below configurations on the actual method and fallBack methods in the ServiceImpl class:

Actual method:

```java
@CircuitBreaker(name = "findPersonByParticipantID", fallbackMethod = "findPersonByParticipantIDFallBack")
```

FallBack method:

It's important to remember that a fallback method should be placed in the same class and must have the same method signature with just ONE extra target exception parameter).

If there are multiple fallbackMethod methods, the method that has the most closest match will be invoked, for example:

	If you try to recover from NumberFormatException, the method with signature String findPersonByParticipantIDFallBack(String parameter, IllegalArgumentException exception)} will be invoked.

You can define one global fallback method with an exception parameter only if multiple methods has the same return type and you want to define the same fallback method for them once and for all.

## Resilience4j Client configuration

- Client side Resilience4j Pattern: Client side Resilience4j design pattern is implemented with a fallBack at the Feign Client level. There is provision to ignore certain exceptions from invoking the fallBack method to be configured in application yml file.
	
		resilience4j.circuitbreaker.configs.<<circuit_breaker_name>>.ignoreExceptions:
			- feign.FeignException
            - java.lang.IllegalArgumentException 
           	- gov.va.bip.framework.exception.BipException
            - gov.va.bip.framework.exception.BipRuntimeException

- To configure resilience4j at the server level, add the `bip-framework-autoconfigure` dependency to the project pom, with the appropriate version to get the required dependency libraries namely `resilience4j-spring-boot2` and `resilience4j-feign`

```xml
<dependency>
	<groupId>gov.va.bip.framework</groupId>
	<artifactId>bip-framework-auotconfigure</artifactId>
</dependency>
```

- Use below configurations on the actual method in the Feign Client class. FallBack is implemented with a factory class

```java
@FeignClient(value = "${spring.application.name}",
		url = "${bip-reference-person.ribbon.listOfServers:}",
		name = "${spring.application.name}",
		fallbackFactory = FeignPersonClientFallbackFactory.class,
		configuration = ReferenceServiceFeignConfig.class)
@CircuitBreaker(name = "FeignPersonClient")
```
