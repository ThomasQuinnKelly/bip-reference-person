package gov.va.ocp.reference.person.rest.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.va.ocp.reference.person.config.ReferenceServiceFeignConfig;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoResponse;
import gov.va.ocp.reference.person.rest.provider.PersonResource;

@FeignClient(value = "os-reference-person",
		fallback = FeignPersonClientFallback.class,
		configuration = ReferenceServiceFeignConfig.class)
public interface FeignPersonClient { // NOSONAR not a functional interface

	@RequestMapping(value = PersonResource.URL_PREFIX + "/pid", method = RequestMethod.POST)
	PersonInfoResponse personByPid(@RequestBody final PersonInfoRequest personInfoRequest);
}
