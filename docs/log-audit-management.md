# Log and Audit Management

## Capability
- Centralized logs for all the services
- Quick search of any logs
- Filtering Support

## Log and Audit patterns
- Application Framework team has proposed Logback for service application and audit logging as it's fast, light and internally Spring Boot provides a great support for console and file appending. In addition Logback provides appenders for logstash configuration, classic async and email for error notification.

- JSON encoder pattern is defined in [bip-framework-logback-starter.xml](https://github.ec.va.gov/EPMO/bip-framework/blob/master/bip-framework-autoconfigure/src/main/resources/gov/va/bip/framework/starter/logger/bip-framework-logback-starter.xml)

- Logback requires the [Janino library](https://logback.qos.ch/setup.html#janino) for conditional logging. You don't need it if you aren't using the structures in your config files. If you are using conditionals, you will need to add the Janino dependency. You can add this to your pom.xml file to get the dependency

- Applications will run in docker containers on OpenShift Container Platform. Platform to provide OpenShift Elasticsearch, FluentD, and Kibana (EFK) stack. Docker logs will be collected by a Fluentd process on each node and forwarded to Elasticsearch to store, and Kibana UI for lookup and quick search of any logs

- Applications can control log levels in the *application*.yml file under the `logging:level:**` property. For an example, see [bip-reference-person.yml](https://github.ec.va.gov/EPMO/bip-reference-person/blob/master/bip-reference-person/src/main/resources/bip-reference-person.yml)

## Logback configuration
- In the application, you must specify a Logback XML configuration file as logback-spring.xml in the project classpath. The Spring Boot team recommends using the -spring variant for your logging configuration, logback-spring.xml is preferred over logback.xml. If you use the standard logback.xml configuration, Spring Boot may not be able to completely control log initialization. 

Here is the code of the logback-spring.xml file from src/main/resources directory.

```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE xml>
	<configuration scan="false" debug="false">
		<!-- Resource is available in shared auto-configure included via pom.xml dependency-->
	    <include resource="gov/va/bip/framework/starter/logger/bip-framework-logback-starter.xml" />
	    
	    <root level="INFO">
	    	<if condition='"local-int,ci,dev,stage,prod".contains("${spring.profiles.active}")'>
	           <then>
	                <appender-ref ref="BIP_FRAMEWORK_ASYNC_CONSOLE_APPENDER" />
	           </then>
	           <else>
	                <appender-ref ref="CONSOLE" />
	           </else>
	        </if>
	    </root>
	</configuration>
```

- In the logback-spring.xml file, resource  `gov/va/bip/framework/starter/logger/bip-framework-logback-starter.xml` is available from shared auto configuration library. Logback dependency comes from spring boot starter, so add only the Logstash logback encoder and also requires the Janino library for conditional logging. Libraries added via pom.xml dependencies as shown below.
  
```xml
	  <dependency>
	    <groupId>gov.va.bip.framework</groupId>
	    <artifactId>bip-framework-autoconfigure</artifactId>
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
```

- Modify application service YML file to change logging levels for the application packages, classes

```yaml
		logging: 
		   level:
		     gov.va.bip.framework.client.ws: DEBUG
		     gov.va.bip.framework.rest.provider: DEBUG
		     gov.va.bip.reference.partner: DEBUG
```

## Sample JSON Output for application and audit logs

- Log type as Audit. "logType":"auditlogs"

      "@timestamp":"2019-03-11T15:38:31.047+00:00","app_name":"bip-reference-person","app_version":"0.0.1-SNAPSHOT","app_profile":"dev","logType":"auditlogs","severity":"INFO","class":"gov.va.bip.framework.audit.AuditLogger","pid":"83960","thread_name":"SimpleAsyncTaskExecutor-1","traceId":"0edb6c1db42c8075","spanExportable":"true","logType":"auditlogs","X-Span-Export":"true","activity":"personByPid","tokenId":"2bd8e44b-f879-4f68-82c7-db2a1f62ac67","X-B3-ParentSpanId":"0edb6c1db42c8075","parentId":"0edb6c1db42c8075","spanId":"f48e133ad1866e43","audit_class":"gov.va.bip.reference.person.api.provider.PersonResource","X-B3-SpanId":"f48e133ad1866e43","X-B3-TraceId":"0edb6c1db42c8075","event":"REST_REQUEST","user":"JANE DOE","message":"{\\\"headers\\\":{\\\"authorization\\\":\\\"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJWZXRzLmdvdiIsImlhdCI6MTU1MjMxODY5OCwianRpIjoiMmJkOGU0NGItZjg3OS00ZjY4LTgyYzctZGIyYTFmNjJhYzY3IiwiZXhwIjoxNTUyMzE5NTk4LCJmaXJzdE5hbWUiOiJKQU5FIiwibWlkZGxlTmFtZSI6Ik0iLCJsYXN0TmFtZSI6IkRPRSIsInByZWZpeCI6Ik1zIiwic3VmZml4IjoiUyIsImJpcnRoRGF0ZSI6IjE5NzgtMDUtMjAiLCJnZW5kZXIiOiJGRU1BTEUiLCJhc3N1cmFuY2VMZXZlbCI6MiwiZW1haWwiOiJqYW5lLmRvZUB2YS5nb3YiLCJjb3JyZWxhdGlvbklkcyI6WyI3Nzc3OTEwMl5OSV4yMDBNXlVTVkhBXlAiLCI5MTI0NDQ2ODleUEleMjAwQlJMU15VU1ZCQV5BIiwiNjY2NjM0NV5QSV4yMDBDT1JQXlVTVkJBXkEiLCIxMTA1MDUxOTM2Xk5JXjIwMERPRF5VU0RPRF5BIiwiOTEyNDQ0Njg5XlNTIl19.0zKCuj1DxS83uBVs48aLvv18wqiv87-TD_wOm8OioLQ\\\",\\\"content-length\\\":\\\"30\\\",\\\"referer\\\":\\\"http://localhost:8080/swagger-ui.html\\\",\\\"accept-language\\\":\\\"en-US,en;q=0.9\\\",\\\"cookie\\\":\\\"_ga=GA1.1.1949281902.1538404227; _vagovRollup=GA1.1.1701694839.1538404227; kampyleUserSession=1542202513069; kampyleUserSessionsCount=89; kampyleSessionPageCounter=1; JSESSIONID=A0757F34A7DAB940DD4539022400D264; rack.session=BAh7C0kiD3Nlc3Npb25faWQGOgZFVEkiRTJiNDA3YzY5MDJhODQ1NTdiZjQx%0ANDYxNTQxMTFhYmE4MmQ4ZTc2MTAyNTExZDQ0ZTA1MGFmN2U4ZGRkNzVmMDMG%0AOwBGSSIKdG9rZW4GOwBGSSIteXF6NHZqU3o2RlBHNFFXYzU3b0FYRWZ6ajJT%0ATlhlYnZZeWtKWjJodgY7AFRJIgl1dWlkBjsARkkiJTg1YzUwYWE3NjkzNDQ2%0AMGM4NzM2ZjY4N2E2YTMwNTQ2BjsAVEkiD2NyZWF0ZWRfYXQGOwBGSXU6CVRp%0AbWUNtMYdwFemZsUJOgl6b25lSSIIVVRDBjsARjoNbmFub19udW1pAiADOg1u%0AYW5vX2RlbmkGOg1zdWJtaWNybyIGgEkiCWNzcmYGOwBGSSIxRjlFTWVjZGgr%0AckVlcE1xaUdkeG5HVElmQ1dvcDA4VFJrblQrK09NS0QxTT0GOwBGSSINdHJh%0AY2tpbmcGOwBGewdJIhRIVFRQX1VTRVJfQUdFTlQGOwBUSSItOTBhMjk1MDg0%0AYzIzMmQwY2YwMDIzZTVlMmQ0YmQ5MzlmMWI2NGFkNgY7AEZJIhlIVFRQX0FD%0AQ0VQVF9MQU5HVUFHRQY7AFRJIi1kNGU3ZDMzZjRlYjczYmNlOGZmNzA3Yzgw%0AYjY5YWM1N2VlNWM5ZmJjBjsARg%3D%3D%0A--776597bfe414af4a1b324907ab9468b41d44388a; vagov_session_dev=fzdVGr0CT1FgDv_SxjWQt-AxLTscyKD2z2suQqqR6RmS61D3iFXoCFyT_IhWnsuwfzhiiVqlJEj1LEF1wV9HxMC5CA-G79erHNC8GpemHMKF5GAsswKKEarItEInhusP3xs4gAon3tHnZvs-zmfbSAotEntJF8O3fVhaPZHU7oJj8csWs32wT5Ls62msF4BoWrXng-QFY1gsEpaC_d5NBXZerLWj5Vk27kKJTk0n_TTqwieR8Pb4uNMoSsG7nuDk; api_session=dmNtYkF6ejNNazMrZTNEM1J6MnJ4aXhJNU4yWTlidHdveWRmbE9pLzh1ZHN2Ny9KbDBESjVsZ0o4M1RHc0w4a2UzcFV6VnYvNldzVTZVbzk0d3k5bmpUV2NXTFU0cUNqVFh6L2g0aVRDQStHZ25kNmVVc2U1TURhZGpnS1dVQVRabXRBWWFrNnVzd3o3dkNhYlEzeGt3ZUM3c0NXRWJhMWZMT1psTk5uTXB4TUJESWVjc0lvUTNXQUVLRUd2K1c0ZDFqTzl6NkNKNlc4eHo3VWZEYU5od05zS3RTOGRseVRzQlA1L1lFcW16UEV4Q050MW9HUGNmaWllUllQbDBJdnN0Zy9CUDROc3IrSjZYWks2dG9GQTF2RTVtaDBSTGtsR08yRWIrellOR0ZFRWRUYTYrZHNqK1BRMDB5Unp0TVB5dzdxNkJTSkE4NlhDcDRhS1pySDFDMzJENjFDQklpdGU4NWYzaGlGMSt5WUphSDNxRFprYldYQStaTDFYcUhybGNJc0pvdTdpMDdKeFQzUFBvU3FRc2ZsZGJWdzc2Nm42M1hYWkV6Nmh4Vlo4RVBSUUorRFA1UjNnQm9vMG1VblREVytvY1B3TEYxdlpsamhkKzFNU3g4QmZ6MXorWXQwZVJDWXNNcCsweTZHSGM3Nkh5Ykc4UmllY0I2Z1J1cmpKWVpuYm1ZZjcrL0Y1MkZlVGRkaHpKZmJJUzd4ZDBsQWNLcWNsMXE2L25YM3dPUTRzNnlsMXlZRWh0dHBydytTbzNTbnprVUcrTkdUU0hNMXVSU2duMkh0c3RZNFJlb3JHc1BMa0owZ1oxaHh5cWNlaWRmWitBNDBtMDIyL0VJaStNRXBGSU1RK1ltdGxKUUhNdEpVdmUrb0poMGFLNXBnSEVnZGxXeFZBYU5KNFB2b2tWdHdmQlRqSzhJbkpTN0UtLXR4K3RwTkdtN2V0QXY1QmdELzJGdnc9PQ%3D%3D--6063ba16870cb61bc6ea147e05a7925f6dd0fee7; grafana_sess=8f2256611acd950f\\\",\\\"origin\\\":\\\"http://localhost:8080\\\",\\\"host\\\":\\\"localhost:8080\\\",\\\"connection\\\":\\\"keep-alive\\\",\\\"content-type\\\":\\\"application/json\\\",\\\"accept-encoding\\\":\\\"gzip, deflate, br\\\",\\\"accept\\\":\\\"application/json\\\",\\\"user-agent\\\":\\\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36\\\"},\\\"uri\\\":\\\"/api/v1/persons/pid\\\",\\\"method\\\":\\\"POST\\\",\\\"request\\\":[{\\\"participantID\\\":6666345}]}"}

- Log type as Application. "logType":"applogs"

       {"@timestamp":"2019-03-11T15:38:31.637+00:00","app_name":"bip-reference-person","app_version":"0.0.1-SNAPSHOT","app_profile":"dev","logType":"applogs","severity":"INFO","class":"g.v.o.r.p.i.ReferencePersonServiceImpl","pid":"83960","thread_name":"http-nio-8080-exec-1","traceId":"0edb6c1db42c8075","spanId":"0edb6c1db42c8075","spanExportable":"true","X-Span-Export":"true","X-B3-SpanId":"0edb6c1db42c8075","X-B3-TraceId":"0edb6c1db42c8075","message":"exit [ReferencePersonServiceImpl.findPersonByParticipantID] in elapsed time [0.587 secs]"}
	
## References
- For Logstash Logback Encoder Usage, refer to https://github.com/logstash/logstash-logback-encoder#usage
- For ELK Docker documentation, refer to http://elk-docker.readthedocs.io

