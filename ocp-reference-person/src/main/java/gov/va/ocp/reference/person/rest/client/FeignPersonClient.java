package gov.va.ocp.reference.person.rest.client;

import javax.validation.Valid;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.va.ocp.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.api.model.v1.PersonInfoResponse;
import gov.va.ocp.reference.person.api.provider.PersonResource;
import gov.va.ocp.reference.person.config.ReferenceServiceFeignConfig;

/**
 * The Interface FeignPersonClient.
 */
@FeignClient(value = "${spring.application.name}",
url="${ocp-reference-person.ribbon.listOfServers:}",
name = "${spring.application.name}",
fallbackFactory = FeignPersonClientFallbackFactory.class,
configuration = ReferenceServiceFeignConfig.class)
public interface FeignPersonClient { // NOSONAR not a functional interface


	/**
  * Person by pid.
  *
  * @param personInfoRequest the person info request
  * @return the person by pid domain response
  */
 @RequestMapping(value = PersonResource.URL_PREFIX + "/pid", 
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	PersonInfoResponse personByPid(@Valid @RequestBody PersonInfoRequest personInfoRequest);
}
