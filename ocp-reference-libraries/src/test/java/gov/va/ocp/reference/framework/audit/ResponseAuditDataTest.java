package gov.va.ocp.reference.framework.audit;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Test;

public class ResponseAuditDataTest {

	@Test
	public void toStringTest() {
		ResponseAuditData responseAuditData = new ResponseAuditData();
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("testKey", "testValue");
		responseAuditData.setHeaders(headers);
		String response = "test response";
		responseAuditData.setResponse(response);
		assertTrue(responseAuditData.toString().equals("ResponseAuditData{headers=" + ReflectionToStringBuilder.toString(headers)
				+ ", uri='" + ", response=" + ReflectionToStringBuilder.toString(response) + "}"));
	}
}
