package gov.va.bip.reference.person.orm;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.reference.person.api.model.v1.PersonDocumentMetadata;

@Component
public class PersonDatabaseHelper {

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonDatabaseHelper.class);

	@Autowired
	PersonrecordsRepository personRecordsRepository;

	/**
	 * Store meta-data about document in person repository for a given pid
	 * 
	 * @param pid
	 * @return a file as a byte array
	 * @throws IOException
	 */
	public void storeMetadata(final Long pid, final PersonDocumentMetadata personDocumentMetadata) {
		// TODO Auto-generated method stub
	}

}
