package gov.va.ocp.reference.person.model.person.v1;

import gov.va.ocp.reference.framework.service.ServiceResponse;
import gov.va.ocp.reference.framework.transfer.ServiceTransferObjectMarker;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * A class to represent the data contained in the response
 * from the Person Service.
 *
 */
@ApiModel(description = "Model for the response from the Person Service")
public class PersonInfoResponse extends ServiceResponse implements ServiceTransferObjectMarker {

	/** Id for serialization. */
	private static final long serialVersionUID = 7327802119014249445L;

	/** A PersonInfo instance. */
	@ApiModelProperty(value = "The object representing the person information")
	private PersonInfo personInfo;

	/**
	 * Gets the person info.
	 *
	 * @return A PersonInfo instance
	 */
	public final PersonInfo getPersonInfo() {
		return personInfo;
	}

	/**
	 * Sets the person info.
	 *
	 * @param personInfo A PersonInfo instance
	 */
	public final void setPersonInfo(final PersonInfo personInfo) {
		this.personInfo = personInfo;
	}
}
