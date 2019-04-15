package gov.va.bip.vetservices.claims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import gov.va.bip.framework.aspect.AuditableAnnotationAspect;



@SpringBootApplication
@EnableDiscoveryClient // needed to reach out to spring cloud config, eureka
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableFeignClients
@EnableHystrix
@EnableCaching
@EnableAsync
public class VetservicesClaimsApplication {

	public static void main(String[] args) {
		SpringApplication.run(VetservicesClaimsApplication.class, args);
	}
	
	@Bean
	AuditableAnnotationAspect myAspect() {
		return new AuditableAnnotationAspect();
	}
}
