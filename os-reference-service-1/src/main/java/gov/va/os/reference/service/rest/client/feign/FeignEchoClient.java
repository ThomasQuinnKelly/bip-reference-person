package gov.va.os.reference.service.rest.client.feign;

import org.springframework.cloud.openfeign.FeignClient;

import gov.va.os.reference.service.config.ReferenceServiceFeignConfig;

@FeignClient(value = "os-reference-spring-boot-service",
		fallback = FeignEchoClientFallback.class,
		configuration = ReferenceServiceFeignConfig.class)
public interface FeignEchoClient { // NOSONAR not a functional interface

//	TODO
//	@RequestMapping(value = "/service-1/v1/echo", method = RequestMethod.GET)
//	EchoHostServiceResponse echo();

}
