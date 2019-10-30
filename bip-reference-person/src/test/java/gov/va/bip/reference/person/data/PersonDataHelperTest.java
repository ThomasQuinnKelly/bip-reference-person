package gov.va.bip.reference.person.data;

import gov.va.bip.reference.person.data.docs.entities.PersonDoc;
import gov.va.bip.reference.person.data.info.entities.PersonInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@Sql(scripts = {"/createSequence.sql"}, config = @SqlConfig(dataSource = "docsDataSource", transactionManager = "docsTransactionManager"))
@Sql(scripts = {"/createSequence.sql"}, config = @SqlConfig(dataSource = "infoDataSource", transactionManager = "infoTransactionManager"))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "spring.cloud.bus.enabled=false", "spring.cloud.discovery.enabled=false",
                "spring.cloud.consul.enabled=false", "spring.cloud.config.discovery.enabled=false", "spring.cloud.vault.enabled=false"})
public class PersonDataHelperTest {

    @Autowired
    PersonDataHelper personDataHelper = new PersonDataHelper();

    private PersonInfo personInfo = new PersonInfo();
    private PersonDoc personDoc = new PersonDoc();

    public void setupPersonInfoRepo(){
        personInfo.setId(1000);
        personInfo.setDodedipnid(1000L);
        personInfo.setPnidType("Test");
        personInfo.setPnid(1000L);
        personInfo.setPid(1000L);
        personInfo.setIcn(1000L);
        personInfo.setFileNumber(1000L);
        personInfo.setTokenId("A");
        personInfo.setBirthDate(LocalDate.of(1980, 1, 1));
        personInfo.setFirstName("John");
        personInfo.setLastName("Doe");
        personInfo.setMiddleName("Jacob");
        personInfo.setPrefix("Mr.");
        personInfo.setSuffix("Sr.");
        personInfo.setGender("Male");
        personInfo.setAssuranceLevel(1);
        personInfo.setEmail("johndoe@example.com");
        personDataHelper.personInfoRepo.save(personInfo);

        personInfo.setId(2000);
        personInfo.setDodedipnid(2000L);
        personInfo.setPnidType("Test2");
        personInfo.setPnid(2000L);
        personInfo.setPid(2000L);
        personInfo.setIcn(2000L);
        personInfo.setFileNumber(2000L);
        personInfo.setTokenId("B");
        personInfo.setBirthDate(LocalDate.of(1990, 2, 2));
        personInfo.setFirstName("Jane");
        personInfo.setLastName("Doe");
        personInfo.setMiddleName("Mary");
        personInfo.setPrefix("Mrs.");
        personInfo.setSuffix("");
        personInfo.setGender("Female");
        personInfo.setAssuranceLevel(2);
        personInfo.setEmail("janedoe@example.com");
        personDataHelper.personInfoRepo.save(personInfo);

        personInfo.setId(3000);
        personInfo.setDodedipnid(3000L);
        personInfo.setPnidType("Test3");
        personInfo.setPnid(3000L);
        personInfo.setPid(3000L);
        personInfo.setIcn(3000L);
        personInfo.setFileNumber(3000L);
        personInfo.setTokenId("C");
        personInfo.setBirthDate(LocalDate.of(2000, 3, 3));
        personInfo.setFirstName("Test");
        personInfo.setLastName("Duplicates");
        personInfo.setMiddleName("M");
        personInfo.setPrefix("Mr.");
        personInfo.setSuffix("");
        personInfo.setGender("Male");
        personInfo.setAssuranceLevel(3);
        personInfo.setEmail("duplicate@example.com");
        personDataHelper.personInfoRepo.save(personInfo);

        //Creating duplicate to test exception handling
        personInfo.setId(4000);
        personDataHelper.personInfoRepo.save(personInfo);
    }

    public void setupPersonDocsRepo(){
        personDataHelper.storeMetadata(1000L, "FirstDoc", LocalDate.of(1980, 1, 1));
        personDataHelper.storeMetadata(2000L, "SecondDoc", LocalDate.of(1990, 2, 2));
        personDataHelper.storeMetadata(3000L, "DuplicateDoc", LocalDate.of(2000, 3, 3));

        //Creating duplicate to test exception handling
        personDoc.setId(3000);
        personDoc.setPid(3000L);
        personDoc.setDocName("DuplicateDoc");
        personDoc.setDocCreateDate(LocalDate.of(2000, 3, 3));
        personDataHelper.personDocsRepo.save(personDoc);
    }

    public void assertNotUniqueException(Exception e){
        Assert.assertEquals("query did not return a unique result: 2; nested exception is javax.persistence.NonUniqueResultException: query did not return a unique result: 2", e.getMessage());
    }

    @Test
    public void testPersonInfoRepoMethods(){
        setupPersonInfoRepo();

        Assert.assertNull( personDataHelper.getInfoForIcn(9999L));
        Assert.assertNull( personDataHelper.getInfoForEmail("throwaway@example.com"));

        Assert.assertEquals(new Long(1000), personDataHelper.getInfoForIcn(1000L).getIcn());
        Assert.assertEquals("johndoe@example.com", personDataHelper.getInfoForEmail("johndoe@example.com").getEmail());
        Assert.assertEquals(new Long(2000), personDataHelper.getInfoForIcn(2000L).getIcn());
        Assert.assertEquals("janedoe@example.com", personDataHelper.getInfoForEmail("janedoe@example.com").getEmail());

        try{
            personDataHelper.getInfoForIcn(3000L);
        }catch(Exception e){
            assertNotUniqueException(e);
        }

        try{
            personDataHelper.getInfoForEmail("duplicate@example.com");
        }catch(Exception e){
            assertNotUniqueException(e);
        }
    }

    @Test
    public void testPersonDocsRepoMethods(){
        setupPersonDocsRepo();

        Assert.assertNull(personDataHelper.getDocForPid(9999L));
        Assert.assertEquals(new Long(1000), personDataHelper.getDocForPid(1000L).getPid());
        Assert.assertEquals(new Long(2000), personDataHelper.getDocForPid(2000L).getPid());

        try {
            personDataHelper.getDocForPid(3000L);
        }catch (Exception e){
            assertNotUniqueException(e);
        }

        try{
            personDataHelper.storeMetadata(3000L, "DuplicateDoc", LocalDate.of(2000, 3, 3));
        }catch (Exception e){
            assertNotUniqueException(e);
        }
    }
}
