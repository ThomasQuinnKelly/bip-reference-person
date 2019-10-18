package gov.va.bip.reference.person.client.ws.validate;

import org.junit.Assert;
import org.junit.Test;

import gov.va.bip.framework.exception.BipValidationRuntimeException;
import gov.va.bip.reference.person.model.PersonByPidDomainRequest;

public class PersonDomainValidatorTest {
	
	/**
	 * Test a validation on a valid object. Expect that no RuntimeException is thrown.
	 */
	@Test
	public void validatePersonInfoRequest() {
		PersonByPidDomainRequest request = new PersonByPidDomainRequest();
		request.setParticipantID(1L);
		
		PersonDomainValidator.validatePersonInfoRequest(request);
	}
	
	/**
	 * Test validating a null object.
	 * 
	 * Expect that a BipValidationRuntimeException will be thrown with the message:
	 * "PersonByPidDomainRequest cannot be null"
	 */
	@Test
	public void validatePersonInfoRequest_NullObject() {
		try {
			PersonDomainValidator.validatePersonInfoRequest(null);
			Assert.fail("BipValidationRuntimeException should be thrown when object is null");
		} catch (BipValidationRuntimeException ex) {
			Assert.assertEquals("PersonByPidDomainRequest cannot be null", ex.getMessage());
		}
	}
	
	/**
	 * Test validating a object with a null ParticipantId.
	 * 
	 * Expect that a BipValidationRuntimeException will be thrown with the message:
	 * "PersonByPidDomainRequest.participantID cannot be null"
	 */
	@Test
	public void validatePersonInfoRequest_NullPID() {
		PersonByPidDomainRequest request = new PersonByPidDomainRequest();
		request.setParticipantID(null);
		
		try {
			PersonDomainValidator.validatePersonInfoRequest(request);
			Assert.fail("BipValidationRuntimeException should be thrown when object is null");
		} catch (BipValidationRuntimeException ex) {
			Assert.assertEquals("PersonByPidDomainRequest.participantID cannot be null", ex.getMessage());
		}
	}
	
	/**
	 * Test validating a object with a null ParticipantId.
	 * 
	 * Expect that a BipValidationRuntimeException will be thrown with the message:
	 * "PersonByPidDomainRequest.participantID cannot be zero"
	 */
	@Test
	public void validatePersonInfoRequest_ZeroPID() {
		PersonByPidDomainRequest request = new PersonByPidDomainRequest();
		request.setParticipantID(0L);
		
		try {
			PersonDomainValidator.validatePersonInfoRequest(request);
			Assert.fail("BipValidationRuntimeException should be thrown when object is null");
		} catch (BipValidationRuntimeException ex) {
			Assert.assertEquals("PersonByPidDomainRequest.participantID cannot be zero", ex.getMessage());
		}
	}

}
