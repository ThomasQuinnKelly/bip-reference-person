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
	private String documentName;

	/** The date of creation of the document. */
	private String documentCreationDate;

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(final String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentCreationDate() {
		return documentCreationDate;
	}

	public void setDocumentCreationDate(final String documentCreationDate) {
		this.documentCreationDate = documentCreationDate;
	}

}
