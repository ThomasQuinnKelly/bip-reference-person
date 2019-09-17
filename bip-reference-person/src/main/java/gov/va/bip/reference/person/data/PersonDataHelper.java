package gov.va.bip.reference.person.data;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.reference.person.data.docs.PersonDocsRepo;
import gov.va.bip.reference.person.data.docs.entities.PersonDoc;
import gov.va.bip.reference.person.data.info.PersonInfoRepo;
import gov.va.bip.reference.person.data.info.entities.PersonInfo;

/**
 * Helper class for abstracting the data base layer
 *
 */
@Component
public class PersonDataHelper {

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonDataHelper.class);

	@Autowired
	PersonDocsRepo personDocsRepo;

	@Autowired
	PersonInfoRepo personInfoRepo;

	/**
	 * Find from PersonInfoRepo by veteran's ICN.
	 *
	 * @param icn
	 * @return PersonInfo
	 */
	public PersonInfo getInfoForIcn(Long icn) {
		try {
			return personInfoRepo.findByIcn(icn);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Find from PersonInfoRepo by veteran's email address.
	 *
	 * @param email
	 * @return PersonInfo
	 */
	public PersonInfo getInfoForEmail(String email) {
		try {
			return personInfoRepo.findByEmail(email);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

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
			PersonDoc result = personDocsRepo.findByPid(pid);
			if (result == null) {
				result = new PersonDoc();
				result.setPid(pid);
			}
			result.setDocName(docName);
			result.setDocCreateDate(docCreateDate);
			personDocsRepo.save(result);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Get the document corresponding to the pid in PERSONDOCS table.
	 *
	 * @param pid the pid
	 * @return the data for pid
	 */
	public PersonDoc getDocForPid(final Long pid) {
		try {
			return personDocsRepo.findByPid(pid);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

}
