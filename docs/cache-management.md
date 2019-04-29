# Cache Management

## Capability (Cache provider : Redis)
- In memory data structure store used as a cache

## Cache pattern
- Cache bucket values to follow the naming convention as `cacheName_ProjectName_MavenVersion` or `ProjectName_MavenVersion`. See [How Version Numbers Work in Maven](https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm#MAVEN400). This would avoid cache collisions in the REDIS cache server. For example, `getCountries\_refdataService\_1.0` or `refdataService\_1.0`.

- The project name and the version can be obtained and added through one of the source files using the [templating-maven-plugin](https://www.mojohaus.org/templating-maven-plugin/). See the `pom.xml` file of the [bip-reference-person](https://github.ec.va.gov/EPMO/bip-ocp-ref-spring-boot/blob/master/bip-reference-person/pom.xml) project, under the `<build><plugins>...` section, for more details.

## Redis configuration
- To configure Redis as the cache provider add the following dependency in your project,
add the `bip-reference-autoconfigure` dependency to the project pom, with the appropriate version:

```xml
	<dependency>
        <groupId>gov.va.bip.framework</groupId>
        <artifactId>bip-framework-autoconfigure</artifactId>
        <!-- add the appropriate version -->
    </dependency>
```

- Update the application service yml file with the following configuration (under the default profile). See also [gov.va.bip.framework.cache.autoconfigure](https://github.ec.va.gov/EPMO/bip-ocp-framework/tree/CMAPI2-211_JedisPoolConfig/bip-framework-autoconfigure#govvabipframeworkcacheautoconfigure).
    - Any properties that do not appear in the appropriate hierarchy will be silently ignored, so default values, or nulls will be substituted for properties that were believed to be configured.
    - Redis connection configuration is declared under `spring:redis:*`. The host and port properties should always be declared. SSL is disabled on the local EmbeddedRedisServer, so the property should be false. In hosted environments, devops can override the setting if necessary.
    - Jedis Connection Pooling is enabled simply by adding one or more properties under `spring:redis:jedis:pool:*`. At least one property must be declared under the pool property.
    - It is possible to configure teh environment to support Redis Cluster and Sentinel if needed, however this is not necessary for local environments, and should be driven by devops and their hosted environments.

```yaml
	spring: 
	  cache:
	    type: redis
	  redis: 
	    ssl: false
	    host: localhost
	    port: 6379
		...
	    jedis:
	      ...
	      pool:
	        ...
	        max-wait: -1
```

- Add the `@EnableCaching` annotation to the Spring Boot Application class (Please note there will most likely be many other annotations on this class):

```java
	@SpringBootApplication
	@EnableCaching
	public class MySweetServiceApplication {
	
	    public static void main(String[] args) {
	        SpringApplication.run(MySweetServiceApplication.class, args);
	    }
```

- Add the following properties and set the to appropriate values to configure Redis caching properties. The reference.cache.defaultExpires property is the default ttl for a cache bucket if it is unspecified. The list reference.cache.expires is a list of cacheNames and the corresponding ttls:

```yaml
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
```

## Cache Design Standards
- Do **NOT** use `@Cacheable` annotation with `@HystrixCommand`. @Cacheable skips the method invocation if the key is already in the cache and hence when used with @HystrixCommand, it skips execution of hystrix annotation as well.

- Use `@CachePut` annotation with `@HystrixCommand`. @CachePut annotation does not cause the advised method to be skipped. Hystrix captures the execution of method each time its called. Cache existence *must* be checked in the business methods to make a decision of returning cached data vs calling partner / third party services

- The class `gov.va.bip.framework.cache.BipCacheUtil` in [bip-framework-libraries](https://github.ec.va.gov/EPMO/bip-ocp-framework/tree/master/bip-framework-libraries) project has functions to generate keys and conditionals for `@CachePut` operations using Spring Expression Language (SpEL). Add more such methods as required and use them accordingly.

## Clearing the Cache
Developers needing to clear the cache for local testing purposes have a tool available, as outlined in [Clearing the Redis Cache](https://github.ec.va.gov/EPMO/bip-ocp-ref-spring-boot/tree/master/local-dev#clearing-the-redis-cache).

