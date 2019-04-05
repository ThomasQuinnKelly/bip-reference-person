# Cache Management

## Capability (Cache provider : Redis)
- In memory data structure store used as a cache

## Cache pattern
- Cache bucket values to follow the naming convention as "cacheName\_ProjectName\_MavenVersion" or "ProjectName\_MavenVersion". See [How Version Numbers Work in Maven](https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm#MAVEN400). This would avoid cache collisions in the REDIS cache server. For example, "getCountries\_refdataService\_1.0" or "refdataService\_1.0".

- The project name and the version can be obtained and added through one of the source files using the [templating-maven-plugin](https://www.mojohaus.org/templating-maven-plugin/). See the pom.xml file of the [bip-reference-person](https://github.com/department-of-veterans-affairs/ocp-reference-spring-boot/blob/master/bip-reference-person/pom.xml) project, under the build.plugins section, for more details.

## Redis configuration
- To configure Redis as the cache provider add the following dependency in your project,
add the bip-reference-autoconfigure dependency to the project pom, with the appropriate version:

	<dependency>
        <groupId>gov.va.bip.framework</groupId>
        <artifactId>bip-framework-autoconfigure</artifactId>
        <!-- add the appropriate version -->
    </dependency>
    
- Update the application service yml file with the following configuration (under the default profile):

	spring: 
	  cache:
	    type: redis
	  redis: 
	    host: localhost
	    port: 6379

- Add the @EnableCaching annotation to the Spring Boot Application class (Please note there will most likely be many other annotations on this class):

	@SpringBootApplication
	@EnableCaching
	public class MySweetServiceApplication {
	
	    public static void main(String[] args) {
	        SpringApplication.run(MySweetServiceApplication.class, args);
	    }

- Add the following properties and set the to appropriate values to configure Redis caching properties. The reference.cache.defaultExpires property is the default ttl for a cache bucket if it is unspecified. The list reference.cache.expires is a list of cacheNames and the corresponding ttls:

	reference:
	  cache:
	    defaultExpires: 86400 # (Seconds)
	    expires:
	#     -
	#       name: Cache Name
	#       ttl:  TTL (In Seconds)
	      -
	        name: refPersonService_@project.name@_@project.version@
	        ttl: 1800
	    redis-config:
	      host: localhost
	      port: 6379
	
## Cache Design Standards
- Do NOT use @Cacheable annotation with @HystrixCommand. @Cacheable skips the method invocation if the key is already in the cache and hence when used with @HystrixCommand, it skips execution of hystrix annotation as well.

- Use @CachePut annotation with @HystrixCommand. @CachePut annotation does not cause the advised method to be skipped. Hystrix captures the execution of method each time its called. Cache existence to be checked in the business methods to make a decision of returning cached data vs calling partner / third party services

- The class gov.va.bip.framework.cache.BipCacheUtil in [bip-framework-libraries](https://github.com/department-of-veterans-affairs/bip-framework/tree/master/bip-framework-libraries) project has functions to generate keys and conditionals for @CachePuts operation using Spring Expression Language (SpEL). Add more such methods as required and use them accordingly.