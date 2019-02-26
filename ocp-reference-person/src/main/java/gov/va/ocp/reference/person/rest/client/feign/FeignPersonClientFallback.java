package gov.va.ocp.reference.person.rest.client.feign;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import gov.va.ocp.reference.framework.messages.MessageSeverity;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.model.person.v1.PersonInfoResponse;

@Component
/**
 * This class provides the Hystrix fallback implementation for Feign Client calls to the service
 *
 * @author
 *
 */
public class FeignPersonClientFallback implements FeignPersonClient {

	@Override
	public PersonInfoResponse personByPid(@RequestBody final PersonInfoRequest personInfoRequest) {
		PersonInfoResponse response = new PersonInfoResponse();
		response.setDoNotCacheResponse(true);
		response.addMessage(MessageSeverity.FATAL, "SERVICE_NOT_AVAILABLE",
				"This is feign fallback handler, the service wasn't available");
		return response;
	}

}
