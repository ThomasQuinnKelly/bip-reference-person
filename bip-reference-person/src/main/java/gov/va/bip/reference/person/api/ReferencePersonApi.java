package gov.va.bip.reference.person.api;

import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.RequestBody;

import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;

/**
 * The contract for the Reference Person endpoint.
 *
 * @author aburkholder
 */
public interface ReferencePersonApi {

	/**
	 * Contract for the {@link Health} operation.
	 * 
	 * @return Health
	 */
	public Health health();

	/**
	 * Contract for the "get person info by PID" operation.
	 *
	 * @param personInfoRequest the person info request
	 * @return the person info response
	 */
	public PersonInfoResponse personByPid(@RequestBody final PersonInfoRequest personInfoRequest);
}
