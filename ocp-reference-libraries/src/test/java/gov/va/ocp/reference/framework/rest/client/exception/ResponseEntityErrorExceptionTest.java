package gov.va.ocp.reference.framework.rest.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gov.va.ocp.reference.framework.messages.MessageSeverity;
import gov.va.ocp.reference.framework.service.DomainResponse;

public class ResponseEntityErrorExceptionTest {

	private static final String TEST_KEY = "test.key";
	private static final String TEST_TEXT = "text for test.key";
	DomainResponse domainResponse = new DomainResponse();

	@Test
	public void instantiateResponseEntityErrorExceptionTest() {
		domainResponse.addMessage(MessageSeverity.INFO, TEST_KEY, TEST_TEXT, HttpStatus.ACCEPTED);
		ResponseEntity<DomainResponse> errorResponse = new ResponseEntity<DomainResponse>(domainResponse, HttpStatus.ACCEPTED);
		ResponseEntityErrorException responseEntityErrorException = new ResponseEntityErrorException(errorResponse);

		assertTrue(responseEntityErrorException.getErrorResponse().getStatusCode().equals(HttpStatus.ACCEPTED));
	}

}
