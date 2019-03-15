# Service Discovery Guide

## Capability (Service Discovery: Consul)

Consul is a service mesh solution providing a full featured control plane with service discovery, configuration, and segmentation   functionality. Each of these features can be used individually as needed, or they can be used together to build a full service mesh. Clients of Consul can register a service, such as api or mysql, and other clients can use Consul to discover providers of a given service. Using either DNS or HTTP, applications can easily find the services they depend upon.

## Consul Service Discovery Configuration

- Need to use @EnableDiscoveryClient annotation on the Spring Boot Application to enable service registry and discovery in Consul

	@SpringBootApplication
	@EnableDiscoveryClient 
	@EnableAspectJAutoProxy(proxyTargetClass = true)
	@EnableFeignClients
	@EnableHystrix
	@EnableCaching
	@EnableAsync
	@Import(ReferencePersonConfig.class)
	public class ReferencePersonApplication {
	
- Update the application service yml file with the following configuration (under the default profile):

	  discovery: 
	   enabled: true  
	   register: true 
	

- Docker mode has a set of variables configured under docker-compose file as listed below which override the values in the application yml file:

	    environment:
      - spring.profiles.active=local-int
      - spring.cloud.consul.host=consul
      - spring.redis.host=redis
      - spring.cloud.vault.host=vault
      - spring.cloud.consul.config.enabled=true
      - spring.cloud.consul.config.failFast=true
      - spring.cloud.consul.discovery.enabled=true
      - spring.cloud.consul.discovery.register=true
      - spring.cloud.vault.enabled=true
      - spring.cloud.vault.consul.enabled=true
      - ocp.security.jwt.enabled=true
      
## Consul Access

- Services registered with Consul can be viewed locally at http://localhost:8500/ and the port is configured in the application yml and docker-compose files which can be overridden if needed.

- Need to enter the MASTER_ACL_TOKEN in the ACL Tab under SecretID or Token before we can get access to Consul Services. Please see image at docs/images/Consul-Token.png

- Sample services registered under Consul can be viewed at docs/images/Consul-Services.png