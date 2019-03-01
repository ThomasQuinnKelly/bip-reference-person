package gov.va.ocp.reference.framework.rest.client.exception;

import org.springframework.http.ResponseEntity;

import gov.va.ocp.reference.framework.service.ServiceResponse;

/**
 * Exception Class for REST Template calls for ServiceResponse
 *
 * @author akulkarni
 *
 */
public class ResponseEntityErrorException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/** The error response. */
	private ResponseEntity<ServiceResponse> errorResponse;
	
	/**
	 * Instantiates a new response entity error exception.
	 *
	 * @param errorResponse the error response
	 */
	public ResponseEntityErrorException(ResponseEntity<ServiceResponse> errorResponse) {
	     this.errorResponse = errorResponse;
	}
	  
  	/**
  	 * Gets the error response.
  	 *
  	 * @return the error response
  	 */
  	public ResponseEntity<ServiceResponse> getErrorResponse() {
	      return errorResponse;
	  }
	}
