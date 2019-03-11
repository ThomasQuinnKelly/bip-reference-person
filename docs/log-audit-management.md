# Log and Audit Management

## Capability
- Centralized logs for all the services
- Quick search of any logs
- Filtering Support

## Log and Audit patterns
- Application Framework team has proposed Logback for service application and audit logging as it's fast, light and internally Spring Boot provides a great support for console and file appending. In addition Logback provides appenders for logstash configuration, classic async and email for error notification.

- JSON encoder pattern is defined in [ocp-reference-logback-starter.xml](https://github.com/department-of-veterans-affairs/ocp-framework/blob/master/ocp-framework-autoconfigure/src/main/resources/gov/va/ocp/reference/starter/logger/ocp-reference-logback-starter.xml)

- Logback requires the [Janino library](https://logback.qos.ch/setup.html#janino) for conditional logging. You don't need it if you aren't using the structures in your config files. If you are using conditionals, you will need to add the Janino dependency. You can add this to your pom.xml file to get the dependency

- Applications will run in docker containers on OpenShift Container Platform. Platform to provide OpenShift Elasticsearch, FluentD, and Kibana (EFK) stack. Docker logs will be collected by a Fluentd process on each node and forwarded to Elasticsearch to store, and Kibana UI for lookup and quick search of any logs

## Logback configuration
- In the application, you must specify a Logback XML configuration file as logback-spring.xml in the project classpath. The Spring Boot team recommends using the -spring variant for your logging configuration, logback-spring.xml is preferred over logback.xml. If you use the standard logback.xml configuration, Spring Boot may not be able to completely control log initialization. 

Here is the code of the logback-spring.xml file from src/main/resources directory.

	<?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE xml>
	<configuration scan="false" debug="false">
		<!-- Resource is available in shared auto-configure included via pom.xml dependency-->
	    <include resource="gov/va/ocp/reference/starter/logger/ocp-reference-logback-starter.xml" />
	    
	    <root level="INFO">
	    	<if condition='"local-int,ci,dev,stage,prod".contains("${spring.profiles.active}")'>
	           <then>
	                <appender-ref ref="OCP_REFERENCE_ASYNC_CONSOLE_APPENDER" />
	           </then>
	           <else>
	                <appender-ref ref="CONSOLE" />
	           </else>
	        </if>
	    </root>
	</configuration>
	
- In the logback-spring.xml file, resource  `gov/va/ocp/reference/starter/logger/ocp-reference-logback-starter.xml` is available from shared auto configuration library. Logback dependency comes from spring boot starter, so add only the Logstash logback encoder and also requires the Janino library for conditional logging. Libraries added via pom.xml dependencies as shown below.

	   <dependency>
            <groupId>gov.va.ocp.framework</groupId>
            <artifactId>ocp-framework-autoconfigure</artifactId>
            <version><!-- add the appropriate version --></version>
           </dependency>
           <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
           </dependency>
           <!-- https://mvnrepository.com/artifact/org.codehaus.janino/janino -->
	   <dependency>
	    <groupId>org.codehaus.janino</groupId>
	    <artifactId>janino</artifactId>
	    </dependency>
	    
- Modify application service YML file to change logging levels for the application packages, classes

       logging: 
	  level:
	    gov.va.ocp.framework.ws.client: DEBUG
	    gov.va.ocp.framework.rest.provider: DEBUG
	    gov.va.ocp.reference.partner: DEBUG

	
## References
- For Logstash Logback Encoder Usage, refer to https://github.com/logstash/logstash-logback-encoder#usage

- For ELK Docker documentation, refer to http://elk-docker.readthedocs.io

