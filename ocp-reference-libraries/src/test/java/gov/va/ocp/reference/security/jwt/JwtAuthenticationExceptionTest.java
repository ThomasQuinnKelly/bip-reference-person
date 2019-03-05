package gov.va.ocp.reference.security.jwt;

import org.junit.Test;

import gov.va.ocp.reference.security.jwt.JwtAuthenticationException;

public class JwtAuthenticationExceptionTest {

	public JwtAuthenticationExceptionTest() {
	}

	@Test
	public void testSomeMethod() {
		new JwtAuthenticationException("testmessage");
	}

	@Test
	public void testSomeMethod1() {
		new JwtAuthenticationException("testmessage", new Throwable());
	}

}
