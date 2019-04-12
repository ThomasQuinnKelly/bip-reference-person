package gov.va.bip.reference.person;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import brave.sampler.Sampler;
import gov.va.bip.reference.partner.person.client.ws.PersonWsClientConfig;
import gov.va.bip.reference.person.config.ReferencePersonConfig;

/**
 * An <tt>Reference Person Service Application</tt> enabled for Spring Boot Application,
 * Spring Cloud Netflix Feign Clients, Hystrix circuit breakers, Swagger and
 * AspectJ's @Aspect annotation.
 *
 */
@SpringBootApplication
@EnableDiscoveryClient // needed to reach out to spring cloud config, eureka
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableFeignClients
@EnableHystrix
@EnableCaching
@EnableAsync
@Import({ ReferencePersonConfig.class,
		PersonWsClientConfig.class })
public class ReferencePersonApplication {

	/**
	 * Runs the spring-boot application with this class and any command-line arguments.
	 *
	 * @param args the array or command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ReferencePersonApplication.class, args);
	}

	/**
	 * Always sampler.
	 *
	 * @return the sampler
	 */
	@Bean
	Sampler alwaysSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}

}
