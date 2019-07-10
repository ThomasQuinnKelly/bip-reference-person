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
	PersonRepository personRepository;

	/**
	 * Upload document to person repository
	 * 
	 * @param pid
	 * @param file
	 */
	@Auditable(event = AuditEvents.SERVICE_AUDIT, activity = "uploadDocument")
	public void uploadDocument(final Long pid, final byte[] file) {
		try {
			PersonData data = personRepository.findById(pid).get();
			data.setDocument(file);
			personRepository.save(data);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

}
