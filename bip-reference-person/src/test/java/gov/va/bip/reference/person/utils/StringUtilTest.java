package gov.va.bip.reference.person.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class StringUtilTest {

    String text;
    String result;
    char charMask;
    int numViewableChars;
    Object xmlObject;

    @Before
    public void setUp() {

        charMask = '5';
        numViewableChars = 6;

        xmlObject = "string";
    }

    @Test
    public void testGetMask() {
        // null text
        text = null;

        result = StringUtil.getMask(text, charMask, numViewableChars);

        // empty text
        assertNull(result);

        text = "";

        result = StringUtil.getMask(text, charMask, numViewableChars);

        assertNotNull(result);
        assertEquals(text, result);

        // too short (under 6)
        text = "secret";

        result = StringUtil.getMask(text, charMask, numViewableChars);

        assertNotNull(result);
        assertEquals(text, result);

        // replacing characters above the viewable
        text = "**** is the secret";

        result = StringUtil.getMask(text, charMask, numViewableChars);

        assertNotNull(result);
        assertEquals("555555555555" + text.substring(text.length()-6), result);

    }

    @Test
    public void testGetMask4() {
        // replacing characters above the viewable
        text = "**** is the secret";

        result = StringUtil.getMask4(text);

        assertNotNull(result);
        assertEquals("xxxxxxxxxxxxxx" + text.substring(text.length()-4), result);
    }

    @Test
    public void testNullAndEmpty() {
        assertFalse(StringUtil.isNotNullAndNotEmpty(null));
        assertFalse(StringUtil.isNotNullAndNotEmpty(""));
        assertTrue(StringUtil.isNotNullAndNotEmpty("Not Empty"));

    }

    @Test
    public void testObjectToXMLString() {

        String xml = StringUtil.objectToXMLString(xmlObject);

        assertTrue(xml.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"));
        assertTrue(xml.contains("<string>string</string>"));
    }

}
