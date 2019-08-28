package gov.va.bip.reference.person.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gov.va.bip.reference.person.data.entities.PersonDocs;

/**
 * Repository Class to handle database access operation to the PERSONDOCS table associated with the PersonDocs POJO
 *
 */
@Repository
public interface PersonDocsRepository extends JpaRepository<PersonDocs, Long> {

	/**
	 * Retrieve a PersonDocs based on the pid.
	 *
	 * @param pid the pid
	 * @return PersonDocs
	 */
	PersonDocs findByPid(Long pid);

}
