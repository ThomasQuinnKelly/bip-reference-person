package gov.va.bip.reference.person.data;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.reference.person.data.dao.PersonrecordsRepository;
import gov.va.bip.reference.person.data.orm.entity.Personrecord;

/**
 * Helper class for abstracting the data base layer
 *
 */
@Component
public class PersonDatabaseHelper {

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonDatabaseHelper.class);

	@Autowired
	PersonrecordsRepository personRecordsRepository;

	/**
	 * Store meta-data about document in person repository for a given pid
	 * 
	 * @param pid the pid
	 * @param documentName the name of the document
	 * @param documentCreationDate the date of creation of the document
	 * 
	 * @return a file as a byte array
	 * @throws IOException
	 */
	public void storeMetadata(final Long pid, final String documentName, final LocalDate documentCreationDate) {
		try {
			Personrecord result = personRecordsRepository.findByPid(pid);
			if (result == null) {
				result = new Personrecord();
				result.setPid(pid);
			}
			result.setDocumentName(documentName);
			result.setDocumentCreationDate(documentCreationDate);
			personRecordsRepository.save(result);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Get the record corresponding to the pid in PERSONRECORD table
	 * 
	 * @param pid the pid
	 * 
	 * @return
	 */
	public Personrecord getDataForPid(final Long pid) {
		try {
			Personrecord result = personRecordsRepository.findByPid(pid);
			return result;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

}
