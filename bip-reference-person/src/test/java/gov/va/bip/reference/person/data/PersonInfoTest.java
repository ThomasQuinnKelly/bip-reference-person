package gov.va.bip.reference.person.data;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import gov.va.bip.reference.person.data.info.entities.PersonInfo;

public class PersonInfoTest {

    private PersonInfo personInfo = new PersonInfo();

    @Test
    public void idTest(){
        personInfo.setId(1000);
        Assert.assertEquals(1000, personInfo.getId());
        personInfo.setId(2000);
        Assert.assertEquals(2000, personInfo.getId());
    }

    @Test
    public void dodedipnidTest(){
        personInfo.setDodedipnid(1000L);
        Assert.assertEquals(new Long(1000), personInfo.getDodedipnid());
        personInfo.setDodedipnid(2000L);
        Assert.assertEquals(new Long(2000), personInfo.getDodedipnid());
    }

    @Test
    public void pnidTypeTest(){
        personInfo.setPnidType("type1");
        Assert.assertEquals("type1", personInfo.getPnidType());
        personInfo.setPnidType("type2");
        Assert.assertEquals("type2", personInfo.getPnidType());
    }

    @Test
    public void pnidTest(){
        personInfo.setPnid(1000L);
        Assert.assertEquals(new Long(1000), personInfo.getPnid());
        personInfo.setPnid(2000L);
        Assert.assertEquals(new Long(2000), personInfo.getPnid());
    }

    @Test
    public void pidTest(){
        personInfo.setPid(1000L);
        Assert.assertEquals(new Long(1000), personInfo.getPid());
        personInfo.setPid(2000L);
        Assert.assertEquals(new Long(2000), personInfo.getPid());
    }

    @Test
    public void icnTest(){
        personInfo.setIcn(1000L);
        Assert.assertEquals(new Long(1000), personInfo.getIcn());
        personInfo.setIcn(2000L);
        Assert.assertEquals(new Long(2000), personInfo.getIcn());
    }

    @Test
    public void fileNumberTest(){
        personInfo.setFileNumber(1000L);
        Assert.assertEquals(new Long(1000), personInfo.getFileNumber());
        personInfo.setFileNumber(2000L);
        Assert.assertEquals(new Long(2000), personInfo.getFileNumber());
    }

    @Test
    public void tokenIdTest(){
        personInfo.setTokenId("tokenId1");
        Assert.assertEquals("tokenId1", personInfo.getTokenId());
        personInfo.setTokenId("tokenId2");
        Assert.assertEquals("tokenId2", personInfo.getTokenId());
    }

    @Test
    public void birthDateTest(){
        personInfo.setBirthDate(LocalDate.of(1980, 1, 1));
        Assert.assertEquals(LocalDate.of(1980, 1, 1), personInfo.getBirthDate());
        personInfo.setBirthDate(LocalDate.of(1990, 2, 2));
        Assert.assertEquals(LocalDate.of(1990, 2, 2), personInfo.getBirthDate());
    }

    @Test
    public void firstNameTest(){
        personInfo.setFirstName("Jane");
        Assert.assertEquals("Jane", personInfo.getFirstName());
        personInfo.setFirstName("John");
        Assert.assertEquals("John", personInfo.getFirstName());
    }

    @Test
    public void lastNameTest(){
        personInfo.setLastName("Doe");
        Assert.assertEquals("Doe", personInfo.getLastName());
        personInfo.setLastName("Smith");
        Assert.assertEquals("Smith", personInfo.getLastName());
    }

    @Test
    public void middleNameTest(){
        personInfo.setMiddleName("Mary");
        Assert.assertEquals("Mary", personInfo.getMiddleName());
        personInfo.setMiddleName("Jacob");
        Assert.assertEquals("Jacob", personInfo.getMiddleName());
    }

    @Test
    public void prefixTest(){
        personInfo.setPrefix("Mrs.");
        Assert.assertEquals("Mrs.", personInfo.getPrefix());
        personInfo.setPrefix("Mr.");
        Assert.assertEquals("Mr.", personInfo.getPrefix());
    }

    @Test
    public void suffixTest(){
        personInfo.setSuffix("Jr.");
        Assert.assertEquals("Jr.", personInfo.getSuffix());
        personInfo.setSuffix("Sr.");
        Assert.assertEquals("Sr.", personInfo.getSuffix());
    }

    @Test
    public void genderTest(){
        personInfo.setGender("Female");
        Assert.assertEquals("Female", personInfo.getGender());
        personInfo.setGender("Male");
        Assert.assertEquals("Male", personInfo.getGender());
    }

    @Test
    public void assuranceLevelTest(){
        personInfo.setAssuranceLevel(1);
        Assert.assertEquals(new Integer(1), personInfo.getAssuranceLevel());
        personInfo.setAssuranceLevel(2);
        Assert.assertEquals(new Integer(2), personInfo.getAssuranceLevel());
    }

    @Test
    public void emailTest(){
        personInfo.setEmail("test@example.com");
        Assert.assertEquals("test@example.com", personInfo.getEmail());
        personInfo.setEmail("test@va.gov");
        Assert.assertEquals("test@va.gov", personInfo.getEmail());
    }

    @Test
    public void toStringTest(){
        personInfo.setId(1000);
        personInfo.setDodedipnid(1000L);
        personInfo.setPnidType("type1");
        personInfo.setPnid(1000L);
        personInfo.setPid(1000L);
        personInfo.setIcn(1000L);
        personInfo.setFileNumber(1000L);
        personInfo.setTokenId("tokenId1");
        personInfo.setBirthDate(LocalDate.of(1980, 1, 1));
        personInfo.setFirstName("Jane");
        personInfo.setLastName("Doe");
        personInfo.setMiddleName("Mary");
        personInfo.setPrefix("Mrs.");
        personInfo.setSuffix("Jr.");
        personInfo.setGender("Female");
        personInfo.setAssuranceLevel(1);
        personInfo.setEmail("test@example.com");

        Assert.assertEquals("PersonInfo [id=1000, dodedipnid=1000, pnidType=type1, pnid=1000, pid=1000, icn=1000, fileNumber=1000," +
                " tokenId=tokenId1, birthDate=1980-01-01, firstName=Jane, lastName=Doe, middleName=Mary, prefix=Mrs., suffix=Jr., gender=Female, assuranceLevel=1, email=test@example.com]",
                personInfo.toString());
    }
}
