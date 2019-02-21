package gov.va.os.reference.service.rest.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.va.os.reference.service.api.v1.transfer.EchoHostServiceResponse;
import gov.va.os.reference.service.config.ReferenceServiceFeignConfig;

@FeignClient(value = "os-reference-spring-boot-service",
		fallback = FeignEchoClientFallback.class,
		configuration = ReferenceServiceFeignConfig.class)
public interface FeignEchoClient { // NOSONAR not a functional interface

	@RequestMapping(value = "/service-1/v1/echo", method = RequestMethod.GET)
	ResponseEntity<EchoHostServiceResponse> echo();

}
