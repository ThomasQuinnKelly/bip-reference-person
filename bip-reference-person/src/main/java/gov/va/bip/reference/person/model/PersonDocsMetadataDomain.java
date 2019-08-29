package gov.va.bip.reference.person.model;

import java.io.Serializable;

/**
 * This domain model represents the relevant subset of the data returned from the database layer for use in the person business layer,
 * as required by the REST "person" controller.
 */
public class PersonDocsMetadataDomain implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The name of the document. */
	private String docName;

	/** The date of creation of the document. */
	private String docCreateDate;

	public String getDocName() {
		return docName;
	}

	public void setDocName(final String docName) {
		this.docName = docName;
	}

	public String getDocCreateDate() {
		return docCreateDate;
	}

	public void setDocCreateDate(final String docCreateDate) {
		this.docCreateDate = docCreateDate;
	}

}
