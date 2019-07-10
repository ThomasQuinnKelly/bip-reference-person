package gov.va.bip.reference.person.orm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class PersonData implements Serializable {

	private static final long serialVersionUID = -1330928616668416505L;

	@Id
	private long pid;

	@Lob
	@Column(name = "document", columnDefinition = "BLOB")
	private byte[] document;

	public byte[] getDocument() {
		return document;
	}

	public void setDocument(final byte[] document) {
		this.document = document;
	}

	public long getPid()
	{
		return pid;
	}

	public void setPid(final Long pid)
	{
		this.pid = pid;
	}

	@Override
	public String toString()
	{
		return "ClassPojo [pid = " + pid + ", document = " + document.toString() + "]";
	}
}
