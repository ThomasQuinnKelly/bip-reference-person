package gov.va.os.reference.framework.log;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceLogMarkersTest {

	@Test
	public final void testReferenceLogMarkers() {
		assertNotNull(ReferenceLogMarkers.FATAL.getMarker());
		assertNotNull(ReferenceLogMarkers.EXCEPTION.getMarker());
		assertNotNull(ReferenceLogMarkers.TEST.getMarker());

		assertTrue("FATAL".equals(ReferenceLogMarkers.FATAL.getMarker().getName()));
		assertTrue("EXCEPTION".equals(ReferenceLogMarkers.EXCEPTION.getMarker().getName()));
		assertTrue("TEST".equals(ReferenceLogMarkers.TEST.getMarker().getName()));
	}

}
