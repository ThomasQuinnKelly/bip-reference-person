package gov.va.bip.reference.person.api.model.v1;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * This class represents the relevant subset of the data returned from the Person Web Service.
 *
 */
@ApiModel(description = "Model for data contained in the response from the Person Service")
public class PersonInfo implements Serializable {
	private static final long serialVersionUID = 5791227842810442936L;

	/** The person's file number. */
	@ApiModelProperty(value = "The persons file number", example = "912444689")
	private String fileNumber;

	/** The person's first name. */
	@ApiModelProperty(value = "The persons first name", example = "JANE")
	private String firstName;

	/** the person's middle name. */
	@ApiModelProperty(value = "The persons middle name", example = "M")
	private String middleName;

	/** the person's last name. */
	@ApiModelProperty(value = "The persons last name", example = "DOE")
	private String lastName;

	/** the person's participant id. */
	@ApiModelProperty(value = "The persons participant ID", example = "6666345")
	private Long participantId;

	/** the person's social security number. */
	@ApiModelProperty(value = "The persons SSN", example = "912444689")
	private String socSecNo;

	/**
	 * Gets the file number.
	 *
	 * @return The person file number
	 */
	public final String getFileNumber() {
		return fileNumber;
	}

	/**
	 * Sets the file number.
	 *
	 * @param fileNumber The person file number
	 */
	public final void setFileNumber(final String fileNumber) {
		this.fileNumber = fileNumber;
	}

	/**
	 * Gets the first name.
	 *
	 * @return The person first name
	 */
	public final String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName The person first name
	 */
	public final void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 *
	 * @return The person last name
	 */
	public final String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName The person last name
	 */
	public final void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the middle name.
	 *
	 * @return The person middle name
	 */
	public final String getMiddleName() {
		return middleName;
	}

	/**
	 * Sets the middle name.
	 *
	 * @param middleName The person middle name
	 */
	public final void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Gets the participant id.
	 *
	 * @return The person participant Id
	 */
	public final Long getParticipantId() {
		return participantId;
	}

	/**
	 * Sets the participant id.
	 *
	 * @param participantId The person participant Id
	 */
	public final void setParticipantId(final Long participantId) {
		this.participantId = participantId;
	}

	/**
	 * Gets the ssn.
	 *
	 * @return The person SSN
	 */
	public String getSocSecNo() {
		return socSecNo;
	}

	/**
	 * Sets the ssn.
	 *
	 * @param socSecNo the new soc sec no
	 */
	public void setSocSecNo(final String socSecNo) {
		this.socSecNo = socSecNo;
	}
}
