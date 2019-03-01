package gov.va.ocp.reference.person.rest.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.va.ocp.reference.person.config.ReferenceServiceFeignConfig;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoResponse;
import gov.va.ocp.reference.person.rest.provider.PersonResource;

@FeignClient(value = "${spring.application.name}",
url="${ocp-reference-person.ribbon.listOfServers:}",
name = "${spring.application.name}",
configuration = ReferenceServiceFeignConfig.class)
public interface FeignPersonClient { // NOSONAR not a functional interface


	@RequestMapping(value = PersonResource.URL_PREFIX + "/pid", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	PersonInfoResponse personByPid(@RequestBody PersonInfoRequest personInfoRequest);
}
