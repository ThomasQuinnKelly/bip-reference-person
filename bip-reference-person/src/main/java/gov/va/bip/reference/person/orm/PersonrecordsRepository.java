package gov.va.bip.reference.person.orm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonrecordsRepository extends JpaRepository<Personrecord, Long> {

	Personrecord findByPid(long pid);

}
