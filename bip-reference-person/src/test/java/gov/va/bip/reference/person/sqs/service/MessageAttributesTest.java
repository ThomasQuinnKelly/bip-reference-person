package gov.va.bip.reference.person.sqs.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class MessageAttributesTest {

    String mockMessage = "This message.";
    String mockMessage2 = "This message2.";

    @Test
    public void testDefaults() {
        MessageAttributes messageAttributes = new MessageAttributes();

        assertNotNull(messageAttributes.getCreateTimestamp());
        assertNull(messageAttributes.getMessage());
        assertEquals(0, messageAttributes.getNumberOfRetries());
    }

    @Test
    public void testGetAndSet() {
        MessageAttributes messageAttributes = new MessageAttributes(mockMessage);

        assertNotNull(messageAttributes.getCreateTimestamp());
        assertEquals(mockMessage, messageAttributes.getMessage());
        assertEquals(0, messageAttributes.getNumberOfRetries());

        long epochSecond = new Date().toInstant().getEpochSecond();

        messageAttributes.setCreateTimestamp(epochSecond);
        messageAttributes.setMessage(mockMessage2);
        messageAttributes.setNumberOfRetries(1);

        assertEquals(epochSecond, messageAttributes.getCreateTimestamp());
        assertEquals(mockMessage2, messageAttributes.getMessage());
        assertEquals(1, messageAttributes.getNumberOfRetries());
    }

    @Test
    public void testToJson() throws JSONException {
        MessageAttributes messageAttributes = new MessageAttributes(mockMessage);

        String json = messageAttributes.toJson();

        JSONObject obj = new JSONObject(json);
        String jsonMessage = obj.getString("message");

        assertEquals(mockMessage, jsonMessage);
    }

}
