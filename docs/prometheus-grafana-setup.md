#  Actuator: Monitoring and Management

## Capability (Monitoring and Management : Spring Boot Actuator)
Spring Boot Actuator is one of the sub-projects of Spring Boot, which adds monitoring and management support for your applications. It exposes various HTTP or JMX endpoints you can interact with. In essence, Actuator brings production-ready features to our application. Monitoring our app, gathering metrics, understanding traffic or the state of our database becomes trivial with this dependency. The main benefit of this library is that we can get production grade tools without having to actually implement these features ourselves. Actuator is mainly used to expose operational information about the running application – health, metrics, info, dump, env, etc. It uses HTTP endpoints or JMX beans to enable us to interact with it. Once this dependency is on the classpath several endpoints are available for us out of the box. 

## Endpoints

Actuator comes with most endpoints disabled. Thus, the only two available by default are /health and /info.Endpoints exposed to and availble but some may need configuration

	/auditevents – lists security audit-related events such as user login/logout. Also, we can filter by principal or type among others fields
	/beans – returns all available beans in our BeanFactory. Unlike /auditevents, it doesn’t support filtering
	/conditions – formerly known as /autoconfig, builds a report of conditions around auto-configuration
	/configprops – allows us to fetch all @ConfigurationProperties beans
	/env – returns the current environment properties. Additionally, we can retrieve single properties
	/flyway – provides details about our Flyway database migrations
	/health – summarises the health status of our application
	/heapdump – builds and returns a heap dump from the JVM used by our application
	/info – returns general information. It might be custom data, build information or details about the latest commit
	/liquibase – behaves like /flyway but for Liquibase
	/logfile – returns ordinary application logs
	/loggers – enables us to query and modify the logging level of our application
	/metrics – details metrics of our application. This might include generic metrics as well as custom ones
	/prometheus – returns metrics like the previous one, but formatted to work with a Prometheus server
	/scheduledtasks – provides details about every scheduled task within our application
	/sessions – lists HTTP sessions given we are using Spring Session
	/shutdown – performs a graceful shutdown of the application
	/threaddump – dumps the thread information of the underlying JVM

## Spring Boot Actuator configuration
- Add the following dependency in your project, to the project pom:

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
    
- Update the application service yml file with the following configuration (under the default profile) to enable/disable endpoints:

		# Expose over JMX
		management.endpoints.jmx.exposure.exclude=shutdown
		management.endpoints.jmx.exposure.include=*
		# Expose HTTP REST Endpoint
		management.endpoints.web.exposure.exclude=
		management.endpoints.web.exposure.include=health,info,metrics

## Enabling/Disabling endpoints

- You can configure not only whether an endpoint is exposed over HTTP or JMX, but also you can turn specific endpoints on/off. All the endpoints except shutdown are enabled by default (although not exposed over HTTP).

		# Disable an endpoint
		management.endpoint.[endpoint-name].enabled=false

		# Specific example for 'health' endpoint
		management.endpoint.health.enabled=false

		# Instead of enabled by default, you can change to mode
		# where endpoints need to be explicitly enabled
		management.endpoints.enabled-by-default=false
	
## Prometheus with Spring Boot

- As from Spring Boot 2.0, Micrometer is the default metrics export engine. Micrometer is an application metrics facade that supports numerous monitoring systems. Atlas, Datadog, Prometheus, etc. to name a few

- Add the following dependency in your project, to the project pom, to add the dependency for micrometer-core and micrometer-prometheus-registry by adding following snippet.xml

		<!-- Spring boot actuator to expose metrics endpoint -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
		<!-- Micormeter core dependecy  -->
		<dependency>
			<groupId>io.micrometer</groupId>
			<artifactId>micrometer-core</artifactId>
		</dependency>
		<!-- Micrometer Prometheus registry  -->
		<dependency>
			<groupId>io.micrometer</groupId>
			<artifactId>micrometer-registry-prometheus</artifactId>
		</dependency>
	
- Update the application service yml file: unable the actuator and Prometheus endpoints to be exposed by adding below properties

		#Metrics related configurations
		management.endpoint.metrics.enabled=true
		management.endpoints.web.exposure.include=*
		management.endpoint.prometheus.enabled=true
		management.metrics.export.prometheus.enabled=true
	
- Actuator has enabled an endpoint http://localhost:8080/actuator/prometheus for us. If you browse this URL, you will be able to see the metrics exported from the application. The data is the actual metrics collected from the application and exported as JSON.

