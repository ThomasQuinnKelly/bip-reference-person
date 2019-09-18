package gov.va.bip.reference.person.data.info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gov.va.bip.reference.person.data.info.entities.PersonInfo;

@Repository
public interface PersonInfoRepo extends JpaRepository<PersonInfo, Long> {

	PersonInfo findByIcn(Long icn);

	PersonInfo findByEmail(String email);
}
