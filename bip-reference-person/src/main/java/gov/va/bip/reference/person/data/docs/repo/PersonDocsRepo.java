package gov.va.bip.reference.person.data.docs.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gov.va.bip.reference.person.data.docs.entities.PersonDoc;

/**
 * Repository Class to handle database access operation to the PERSONDOCS table associated with the PersonDoc POJO
 *
 */
@Repository
public interface PersonDocsRepo extends JpaRepository<PersonDoc, Long> {

	/**
	 * Retrieve a PersonDoc based on the pid.
	 *
	 * @param pid the pid
	 * @return PersonDoc
	 */
	PersonDoc findByPid(Long pid);

}
