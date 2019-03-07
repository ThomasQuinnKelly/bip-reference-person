package gov.va.ocp.reference.person.api;

import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.RequestBody;

import gov.va.ocp.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.api.model.v1.PersonInfoResponse;

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
	 * @param personInfoRequest
	 * @return
	 */
	public PersonInfoResponse personByPid(@RequestBody final PersonInfoRequest personInfoRequest);
}
