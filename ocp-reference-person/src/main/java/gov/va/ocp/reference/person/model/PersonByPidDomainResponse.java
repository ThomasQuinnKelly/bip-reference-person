package gov.va.ocp.reference.person.model;

import gov.va.ocp.reference.framework.service.DomainResponse;

/**
 * This domain model represents a response from processing
 * a request for PersonInfoDomain by participant ID.
 * <p>
 * The domain service implementation returns this response to the provider.
 */
public class PersonByPidDomainResponse extends DomainResponse {

	/** Id for serialization. */
	private static final long serialVersionUID = 8470614006372046829L;

	/** A PersonInfoDomain instance. */
	private PersonInfoDomain personInfoDomain;

	/**
	 * Gets the person info.
	 *
	 * @return A PersonInfoDomain instance
	 */
	public final PersonInfoDomain getPersonInfo() {
		return personInfoDomain;
	}

	/**
	 * Sets the person info.
	 *
	 * @param personInfoDomain A PersonInfoDomain instance
	 */
	public final void setPersonInfo(final PersonInfoDomain personInfoDomain) {
		this.personInfoDomain = personInfoDomain;
	}
}
