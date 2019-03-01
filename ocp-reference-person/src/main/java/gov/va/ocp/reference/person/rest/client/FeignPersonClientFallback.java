package gov.va.ocp.reference.person.rest.client;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import gov.va.ocp.reference.framework.messages.MessageSeverity;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;

@Component
/**
 * This class provides the Hystrix fallback implementation for Feign Client calls to the service
 *
 * @author
 *
 */
public class FeignPersonClientFallback implements FeignPersonClient {

	@Override
	public PersonByPidDomainResponse personByPid(@RequestBody final PersonByPidDomainRequest personByPidDomainRequest) {
		PersonByPidDomainResponse response = new PersonByPidDomainResponse();
		response.setDoNotCacheResponse(true);
		response.addMessage(MessageSeverity.FATAL, HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
				"This is feign fallback handler, the service wasn't available", HttpStatus.SERVICE_UNAVAILABLE);
		return response;
	}

}
