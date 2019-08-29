package gov.va.bip.reference.person.data;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.reference.person.data.entities.PersonDocs;
import gov.va.bip.reference.person.data.repositories.PersonDocsRepository;

/**
 * Helper class for abstracting the data base layer
 *
 */
@Component
public class PersonDataHelper {

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonDataHelper.class);

	@Autowired
	PersonDocsRepository personDocsRepository;

	/**
	 * Store meta-data about document in person repository for a given pid.
	 *
	 * @param pid the pid
	 * @param docName the name of the document
	 * @param docCreateDate the date of creation of the document
	 * @return a file as a byte array
	 */
	public void storeMetadata(final Long pid, final String docName, final LocalDate docCreateDate) {
		try {
			PersonDocs result = personDocsRepository.findByPid(pid);
			if (result == null) {
				result = new PersonDocs();
				result.setPid(pid);
			}
			result.setDocName(docName);
			result.setDocCreateDate(docCreateDate);
			personDocsRepository.save(result);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Get the record corresponding to the pid in PERSONDOCS table.
	 *
	 * @param pid the pid
	 * @return the data for pid
	 */
	public PersonDocs getDataForPid(final Long pid) {
		try {
			PersonDocs result = personDocsRepository.findByPid(pid);
			return result;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

}
