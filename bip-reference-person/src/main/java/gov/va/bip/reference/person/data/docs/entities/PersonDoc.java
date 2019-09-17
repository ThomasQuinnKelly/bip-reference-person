package gov.va.bip.reference.person.data.docs.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Table;

/**
 * PersonDoc POJO mapped to the records in the PERSONDOCS table in database
 *
 */
@Entity(name = "PersonDocs")
@Table(appliesTo = "PersonDocs")
@SequenceGenerator(name = "seq", initialValue = 10, allocationSize = 100)
public class PersonDoc implements Serializable {

	private static final long serialVersionUID = -1330928616668416505L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	private long id;

	private long pid;

	@Column(name = "doc_name")
	private String docName;

	@SuppressWarnings("squid:S3437") //LocalDate is serializable
	@Column(name = "doc_create_date")
	private LocalDate docCreateDate;

	public LocalDate getDocCreateDate() {
		return docCreateDate;
	}

	public void setDocCreateDate(final LocalDate docCreateDate) {
		this.docCreateDate = docCreateDate;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(final String docName) {
		this.docName = docName;
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
		return "ClassPojo [pid = " + pid + ", docName = " + docName + ", docCreateDate = "
				+ (docCreateDate == null ? "null" : docCreateDate.format(DateTimeFormatter.BASIC_ISO_DATE)) + "]";
	}
}
