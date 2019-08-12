package gov.va.bip.reference.person.data.orm.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Personrecord POJO mapped to the records in the PERSONRECORD table in database
 *
 */
@Entity
@SequenceGenerator(name="seq", initialValue=10, allocationSize=100)
public class Personrecord implements Serializable {

	private static final long serialVersionUID = -1330928616668416505L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	private long id;

	private long pid;

	private String documentName;

	private LocalDate documentCreationDate;

	public LocalDate getDocumentCreationDate() {
		return documentCreationDate;
	}

	public void setDocumentCreationDate(final LocalDate documentCreationDate) {
		this.documentCreationDate = documentCreationDate;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(final String documentName) {
		this.documentName = documentName;
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(final Long pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		return "ClassPojo [pid = " + pid + ", documentName = " + documentName + ", documentCreationDate = "
				+ documentCreationDate.format(DateTimeFormatter.BASIC_ISO_DATE) + "]";
	}
}
