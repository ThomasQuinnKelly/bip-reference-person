package gov.va.ocp.reference.framework.log;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.event.Level;

import gov.va.ocp.reference.framework.log.ReferenceBanner;
import gov.va.ocp.reference.framework.log.ReferenceLogger;
import gov.va.ocp.reference.framework.log.ReferenceLoggerFactory;

public class ReferenceBaseLoggerTest {

	@Test
	public final void testGetSetLevel() {
		ReferenceLogger logger = ReferenceLoggerFactory.getLogger(ReferenceBanner.class);
		Level level = logger.getLevel();
		assertNotNull(level);
		logger.setLevel(Level.INFO);
		assertTrue(Level.INFO.equals(logger.getLevel()));
		logger.info("Test message");
	}

}
