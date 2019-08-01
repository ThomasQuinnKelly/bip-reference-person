package gov.va.bip.reference.person.orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name="seq", initialValue=10, allocationSize=100)
public class Personrecord implements Serializable {

	private static final long serialVersionUID = -1330928616668416505L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
	private long id;

	private long pid;

	@Lob
	@Column(name = "document", columnDefinition = "BLOB")
	private byte[] document;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public byte[] getDocument() {
		return document;
	}

	public void setDocument(final byte[] document) {
		this.document = document;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(final Long pid) {
		this.pid = pid;
	}

	@Override
	public String toString() {
		return "ClassPojo [pid = " + pid + ", document = " + document.toString() + "]";
	}
}
