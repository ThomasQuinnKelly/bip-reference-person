# Hystrix Circuit Breaker Management

## Capability (Circuit Breaker : Hystrix)
- Hystrix circuit breaker design pattern

## Hystrix pattern
- Server side Hystrix Pattern: Server side Hystrix design pattern is implemented with a fallBack at 
the ServiceImpl class level. There is provision to ignore certain exceptions from invoking the fallBack
method. Caching is disabled once fallBack is invoked. 

- Client side Hystrix Pattern: Client side Hystrix design pattern is implemented with a fallBack at 
the FeignClient level. There is provision to ignore certain exceptions from invoking the fallBack
method.

## Hystrix Server configuration
- To configure Hystrix at the Server level add the following dependency in your project and add the ocp-reference-autoconfigure dependency to the project pom, with the appropriate version to get all autoconfiguration projects:

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
		</dependency>
    
- Update the application service yml file with the following configuration (under the default profile):

	##################################################
	# hystrix command settings
	##################################################
	hystrix:
	  # set the hystrix.shareSecurityContext property to true. Doing so will auto configure an Hystrix concurrency strategy 
	  # plugin hook who will transfer the SecurityContext from your main thread to the one used by the Hystrix command
	 shareSecurityContext: true
	 # set the hystrix.wrappers.enabled property to true. Doing so will auto configure an Hystrix concurrency strategy
	 # plugin hook who will transfer the RequestContextHolder from your main thread to the one used by the Hystrix command
	 # CUSTOM property used conditionally to register bean and HystrixPlugins concurrency strategy
	 wrappers.enabled: true
	 command:
	  default:
	    metrics:
	      rollingStats:
	        # This property sets the duration of the statistical rolling window, in milliseconds. 
	        # This is how long Hystrix keeps metrics for the circuit breaker to use and for publishing 
	        # Default Value: 10000
	        timeInMilliseconds: 20000
	      # This property sets the time to wait, in milliseconds, between allowing health snapshots to be taken that calculate 
	      # success and error percentages and affect circuit breaker status. 
	      # Default Value: 500
	      healthSnapshot: 
	        intervalInMilliseconds: 1000
	    circuitBreaker:
	      # This property sets the amount of time, after tripping the circuit, to reject requests 
	      # before allowing attempts again to determine if the circuit should again be closed.
	      # Default Value: 5000
	      sleepWindowInMilliseconds: 5000
	      # This property sets the minimum number of requests in a rolling window that will trip the circuit.
	      # Default Value: 20
	      requestVolumeThreshold: 20
	    execution:
	      isolation:
	        # strategy: SEMAPHORE
	        thread:
	          # This property sets the time in milliseconds after which the caller will observe a timeout and 
	          # walk away from the command execution. Hystrix marks the HystrixCommand as a TIMEOUT, and performs fallback 
	          # logic. Note that there is configuration for turning off timeouts per-command, if that is desired (see command.timeout.enabled).
	          # Default Value: 1000
	          timeoutInMilliseconds: 20000
	    
- Use below configurations on the actual method and fallBack methods in the ServiceImpl class:

    Actual method:
	@HystrixCommand(fallbackMethod = "findPersonByParticipantIDFallBack", commandKey = "GetPersonInfoByPIDCommand",
			ignoreExceptions = { IllegalArgumentException.class })
	
    FallBack method:
     @HystrixCommand(commandKey = "FindPersonByParticipantIDFallBackCommand")
	
## Hystrix Client configuration
- To configure Hystrix at the Client level add the following dependency in your project and add the ocp-reference-autoconfigure dependency to the project pom, with the appropriate version to get all autoconfiguration projects:

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
		</dependency>
    
- Update the application service yml file with the following configuration (under the default profile):

	feign.hystrix.enabled: true
	    
- Use below configurations on the actual method in the Feign Client class. FallBack is implemented with a factory class

	@FeignClient(value = "${spring.application.name}",
	url="${ocp-reference-person.ribbon.listOfServers:}",
	name = "${spring.application.name}",
	fallbackFactory = FeignPersonClientFallbackFactory.class,
	configuration = ReferenceServiceFeignConfig.class)

- Hystrix client configuration needs OcpFeignAutoConfiguration which is part of the framework 
libraries as configuration. feignBuilder is implemented as part of OcpFeignAutoConfiguration as client side Hystrix needs a seperate configuration.

		@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	@ConditionalOnProperty(name = "feign.hystrix.enabled", matchIfMissing = true)
	public Feign.Builder feignBuilder() {
		int connTimeoutValue = 0;
		try {
			connTimeoutValue = Integer.valueOf(connectionTimeout);
		} catch (NumberFormatException e) { // NOSONAR intentionally do nothing
			// let the Defense below take care of it
		}
		Defense.state(connTimeoutValue > 0,
				"Invalid settings: Connection Timeout value must be greater than zero.\n"
						+ "  - Ensure spring scan directive includes gov.va.ocp.framework.rest.client.resttemplate;\n"
						+ "  - Application property must be set to non-zero positive integer values for ocp.rest.client.connectionTimeout {} "
						+ connectionTimeout + ".");
		final int connTimeoutValueFinal = connTimeoutValue;

		/*
		 * Used by the HystrixFeign setter factory.
		 * Equivalent to:
		 * NOSONAR commandKeyIsRequestLine = SetterFactory.create(Target<?> target, Method method) {..}
		 */
		final SetterFactory commandKeyIsRequestLine = (target, method) -> {
			final String commandKey = Feign.configKey(target.type(), method);
			LOGGER.debug("Feign Hystrix Group Key: {}", groupKey);
			LOGGER.debug("Feign Hystrix Command Key: {}", commandKey);
			return HystrixCommand.Setter
					.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
					.andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
							.withExecutionTimeoutInMilliseconds(connTimeoutValueFinal));
		};

		return HystrixFeign.builder().setterFactory(commandKeyIsRequestLine).requestInterceptor(tokenFeignRequestInterceptor());
	}
	