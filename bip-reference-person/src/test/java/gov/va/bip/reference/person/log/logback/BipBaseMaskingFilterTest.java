package gov.va.bip.reference.person.log.logback;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.rule.OutputCapture;

import gov.va.bip.framework.log.logback.BipMaskingFilter;

public class BipBaseMaskingFilterTest {

	@Rule
	public OutputCapture capture = new OutputCapture();

	class TestBipBaseMaskingFilter extends BipMaskingFilter {
		// test class
	}

	TestBipBaseMaskingFilter sharedBBMF;

	@Before
	public void setUp() {
		sharedBBMF = new TestBipBaseMaskingFilter();
	}

	@Test
	public final void testEvaluatePattern() {
		capture.reset();

		String msg = "Test Pattern 123-456 value";
		Logger logger = LoggerFactory.getLogger(BipMaskingFilter.class);
		logger.error(msg);
		String log = capture.toString();
		assertTrue(log.contains("Test Pattern ****456 value"));
	}

	@Test
	public final void testEvaluateDate() {
		capture.reset();

		String msg = "Test Date Pattern 1234-56-78 value";
		Logger logger = LoggerFactory.getLogger(BipMaskingFilter.class);
		logger.error(msg);
		String log = capture.toString();
		assertTrue(log.contains("Test Date Pattern ********78 value"));
	}
}
