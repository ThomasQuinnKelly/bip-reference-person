package gov.va.bip.reference.person.orm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRecordsRepository extends JpaRepository<PersonRecord, Long> {

	PersonRecord findByPid(long pid);

}
