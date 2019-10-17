package gov.va.bip.reference.person.data;

import gov.va.bip.reference.person.data.docs.entities.PersonDoc;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class PersonDocTest {

    private PersonDoc personDoc = new PersonDoc();

    @Test
    public void idTest(){
        personDoc.setId(1000);
        Assert.assertEquals(1000, personDoc.getId());
        personDoc.setId(2000);
        Assert.assertEquals(2000, personDoc.getId());
    }

    @Test
    public void pidTest(){
        personDoc.setPid(1000L);
        Assert.assertEquals(new Long(1000), personDoc.getPid());
        personDoc.setPid(2000L);
        Assert.assertEquals(new Long(2000), personDoc.getPid());
    }

    @Test
    public void docNameTest(){
        personDoc.setDocName("Initial Document");
        Assert.assertEquals("Initial Document", personDoc.getDocName());
        personDoc.setDocName("Final Document");
        Assert.assertEquals("Final Document", personDoc.getDocName());
    }

    @Test
    public void docCreateDateTest(){
        personDoc.setDocCreateDate(LocalDate.of(1980, 1, 1));
        Assert.assertEquals(LocalDate.of(1980, 1, 1), personDoc.getDocCreateDate());
        personDoc.setDocCreateDate(LocalDate.of(1990, 2, 2));
        Assert.assertEquals(LocalDate.of(1990, 2, 2), personDoc.getDocCreateDate());
    }

    @Test
    public void toStringTest(){
        personDoc.setPid(1000L);
        personDoc.setDocName("Initial Document");
        personDoc.setDocCreateDate(LocalDate.of(1980, 1, 1));
        Assert.assertEquals("ClassPojo [pid = 1000, docName = Initial Document, docCreateDate = 19800101]", personDoc.toString());
    }
}
