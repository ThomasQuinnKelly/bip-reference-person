package gov.va.bip.reference.person.data.info.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * PersonInfo POJO mapped to the records in the PERSONDOCS table in database
 *
 */
@Entity
@SequenceGenerator(name = "seq", initialValue = 10, allocationSize = 100)
public class PersonInfo implements Serializable {

	private static final long serialVersionUID = 4055558783926008842L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	private long id;

	private Long dodedipnid;
	private String pnidType;
	private Long pnid;
	private Long pid;
	private Long icn;
	private Long fileNumber;
	private String tokenId;

	private String birthDate;
	private String firstName;
	private String lastName;
	private String middleName;
	private String prefix;
	private String suffix;
	private String gender;
	private Integer assuranceLevel;
	private String email;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the dodedipnid
	 */
	public Long getDodedipnid() {
		return dodedipnid;
	}

	/**
	 * @param dodedipnid the dodedipnid to set
	 */
	public void setDodedipnid(Long dodedipnid) {
		this.dodedipnid = dodedipnid;
	}

	/**
	 * @return the pnidType
	 */
	public String getPnidType() {
		return pnidType;
	}

	/**
	 * @param pnidType the pnidType to set
	 */
	public void setPnidType(String pnidType) {
		this.pnidType = pnidType;
	}

	/**
	 * @return the pnid
	 */
	public Long getPnid() {
		return pnid;
	}

	/**
	 * @param pnid the pnid to set
	 */
	public void setPnid(Long pnid) {
		this.pnid = pnid;
	}

	/**
	 * @return the pid
	 */
	public Long getPid() {
		return pid;
	}

	/**
	 * @param pid the pid to set
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}

	/**
	 * @return the icn
	 */
	public Long getIcn() {
		return icn;
	}

	/**
	 * @param icn the icn to set
	 */
	public void setIcn(Long icn) {
		this.icn = icn;
	}

	/**
	 * @return the fileNumber
	 */
	public Long getFileNumber() {
		return fileNumber;
	}

	/**
	 * @param fileNumber the fileNumber to set
	 */
	public void setFileNumber(Long fileNumber) {
		this.fileNumber = fileNumber;
	}

	/**
	 * @return the tokenId
	 */
	public String getTokenId() {
		return tokenId;
	}

	/**
	 * @param tokenId the tokenId to set
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	/**
	 * @return the birthDate
	 */
	public String getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the assuranceLevel
	 */
	public Integer getAssuranceLevel() {
		return assuranceLevel;
	}

	/**
	 * @param assuranceLevel the assuranceLevel to set
	 */
	public void setAssuranceLevel(Integer assuranceLevel) {
		this.assuranceLevel = assuranceLevel;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "PersonInfo [id=" + id + ", dodedipnid=" + dodedipnid + ", pnidType=" + pnidType + ", pnid=" + pnid + ", pid=" + pid
				+ ", icn=" + icn + ", fileNumber=" + fileNumber + ", tokenId=" + tokenId + ", birthDate=" + birthDate + ", firstName="
				+ firstName + ", lastName=" + lastName + ", middleName=" + middleName + ", prefix=" + prefix + ", suffix=" + suffix
				+ ", gender=" + gender + ", assuranceLevel=" + assuranceLevel + ", email=" + email + "]";
	}

}
