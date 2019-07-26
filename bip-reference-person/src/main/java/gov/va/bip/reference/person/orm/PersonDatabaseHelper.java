package gov.va.bip.reference.person.orm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.bip.framework.audit.AuditEvents;
import gov.va.bip.framework.audit.annotation.Auditable;
import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;

@Component
public class PersonDatabaseHelper {

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(PersonDatabaseHelper.class);

	@Autowired
	PersonrecordsRepository personRecordsRepository;

	/**
	 * Upload document to person repository to the record with the given pid
	 * 
	 * @param pid
	 * @param file
	 */
	@Auditable(event = AuditEvents.SERVICE_AUDIT, activity = "uploadDocument")
	public void uploadDocument(final Long pid, final byte[] file) {
		try {
			Personrecord results = personRecordsRepository.findByPid(pid);
			results.setDocument(file);
			personRecordsRepository.save(results);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * Retrieve document from person repository for a given pid
	 * 
	 * @param pid
	 * @return a file as a byte array
	 */
	@Auditable(event = AuditEvents.SERVICE_AUDIT, activity = "downloadDocument")
	public byte[] getDocument(final Long pid) {

		try {
			return personRecordsRepository.findByPid(pid).getDocument();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

}
