package gov.va.bip.framework.log.logback;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.rule.OutputCapture;

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
	public final void testEvaluate() {
		capture.reset();

		String msg = "Test File Number 123456789 value";
		Logger logger = LoggerFactory.getLogger(BipMaskingFilter.class);
		logger.error(msg);
		String log = capture.toString();
		assertTrue(log.contains("Test File Number *****6789 value"));
	}
}
