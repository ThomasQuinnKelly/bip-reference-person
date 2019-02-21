package gov.va.os.reference.framework.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import gov.va.os.reference.framework.config.ReferenceCommonSpringProfiles;

public class ReferenceCommonSpringProfilesTest {

	/**
	 * Spring default profile
	 */
	public static final String TEST_PROFILE_DEFAULT = "default";

	/**
	 * Spring profile for local dev environment
	 */
	public static final String TEST_PROFILE_ENV_LOCAL_INT = "local-int";

	/**
	 * Spring profile for local dev environment
	 */
	public static final String TEST_PROFILE_ENV_DOCKER_DEMO = "docker-demo";

	/**
	 * Spring profile for AWS CI environment
	 */
	public static final String TEST_PROFILE_ENV_CI = "ci";

	/**
	 * Spring profile for AWS DEV environment
	 */
	public static final String TEST_PROFILE_ENV_DEV = "dev";

	/**
	 * Spring profile for AWS STAGE environment
	 */
	public static final String TEST_PROFILE_ENV_STAGE = "stage";

	/**
	 * Spring profile for AWS PROD environment
	 */
	public static final String TEST_PROFILE_ENV_PROD = "prod";

	/**
	 * Spring profile for remote client real implementations
	 */
	public static final String TEST_PROFILE_REMOTE_CLIENT_IMPLS = "remote_client_impls";

	/**
	 * Spring profile for remote client simulator implementations
	 */
	public static final String TEST_PROFILE_REMOTE_CLIENT_SIMULATORS = "remote_client_sims";

	/**
	 * Spring profile for unit test specific impls
	 */
	public static final String TEST_PROFILE_UNIT_TEST = "unit_test_sims";

	/**
	 * Spring profile for remote audit simulator implementations
	 */
	public static final String TEST_PROFILE_REMOTE_AUDIT_SIMULATORS = "remote_audit_client_sims";

	/**
	 * Spring profile for remote audit impl implementations
	 */
	public static final String TEST_PROFILE_REMOTE_AUDIT_IMPLS = "remote_audit_client_impl";

	/**
	 * Spring Profile to signify that the application will run embedded redis
	 */
	public static final String TEST_PROFILE_EMBEDDED_REDIS = "embedded-redis";

	/**
	 * Spring Profile to signify that the application will run embedded AWS
	 */
	public static final String TEST_PROFILE_EMBEDDED_AWS = "embedded-aws";

	/**
	 * Spring Profile to signify that the configuration will not be loaded in embedded aws
	 */
	public static final String TES_NOT_PROFILE_EMBEDDED_AWS = "!embedded-aws";

	@Test
	public void profileDefaultTest() throws Exception {
		assertEquals(TEST_PROFILE_DEFAULT, ReferenceCommonSpringProfiles.PROFILE_DEFAULT);
	}

	@Test
	public void profileLocalIntTest() throws Exception {
		assertEquals(TEST_PROFILE_ENV_LOCAL_INT, ReferenceCommonSpringProfiles.PROFILE_ENV_LOCAL_INT);
	}

	@Test
	public void profileDockerDemoTest() throws Exception {
		assertEquals(TEST_PROFILE_ENV_DOCKER_DEMO, ReferenceCommonSpringProfiles.PROFILE_ENV_DOCKER_DEMO);
	}

	@Test
	public void profileAwsCITest() throws Exception {
		assertEquals(TEST_PROFILE_ENV_CI, ReferenceCommonSpringProfiles.PROFILE_ENV_CI);
	}

	@Test
	public void profileAwsDevTest() throws Exception {
		assertEquals(TEST_PROFILE_ENV_DEV, ReferenceCommonSpringProfiles.PROFILE_ENV_DEV);
	}

	@Test
	public void profileAwsStageTest() throws Exception {
		assertEquals(TEST_PROFILE_ENV_STAGE, ReferenceCommonSpringProfiles.PROFILE_ENV_STAGE);
	}

	@Test
	public void profileAwsProdTest() throws Exception {
		assertEquals(TEST_PROFILE_ENV_PROD, ReferenceCommonSpringProfiles.PROFILE_ENV_PROD);
	}

	@Test
	public void profileRemoteClientSimulatorsTest() throws Exception {
		assertEquals(TEST_PROFILE_REMOTE_CLIENT_SIMULATORS, ReferenceCommonSpringProfiles.PROFILE_REMOTE_CLIENT_SIMULATORS);
	}

	@Test
	public void profileRemoteClientImplsTest() throws Exception {
		assertEquals(TEST_PROFILE_REMOTE_CLIENT_IMPLS, ReferenceCommonSpringProfiles.PROFILE_REMOTE_CLIENT_IMPLS);
	}

	@Test
	public void profileRemoteAuditSimulatorsTest() throws Exception {
		assertEquals(TEST_PROFILE_REMOTE_AUDIT_SIMULATORS, ReferenceCommonSpringProfiles.PROFILE_REMOTE_AUDIT_SIMULATORS);
	}

	@Test
	public void profileRemoteAuditImplsTest() throws Exception {
		assertEquals(TEST_PROFILE_REMOTE_AUDIT_IMPLS, ReferenceCommonSpringProfiles.PROFILE_REMOTE_AUDIT_IMPLS);
	}

	@Test
	public void profileUnitTestingTest() throws Exception {
		assertEquals(TEST_PROFILE_UNIT_TEST, ReferenceCommonSpringProfiles.PROFILE_UNIT_TEST);
	}

	@Test
	public void profileEmbeddedRedisTest() throws Exception {
		assertEquals(TEST_PROFILE_EMBEDDED_REDIS, ReferenceCommonSpringProfiles.PROFILE_EMBEDDED_REDIS);
	}

	@Test
	public void profileEmbeddedAwsTest() throws Exception {
		assertEquals(TEST_PROFILE_EMBEDDED_AWS, ReferenceCommonSpringProfiles.PROFILE_EMBEDDED_AWS);
	}

	@Test
	public void notProfileEmbeddedAwsTest() throws Exception {
		assertEquals(TES_NOT_PROFILE_EMBEDDED_AWS, ReferenceCommonSpringProfiles.NOT_PROFILE_EMBEDDED_AWS);
	}

	@Test(expected = IllegalStateException.class)
	public void ascentCommonSpringProfilesConstructor() throws Exception {
		new ReferenceCommonSpringProfiles();
	}
}
