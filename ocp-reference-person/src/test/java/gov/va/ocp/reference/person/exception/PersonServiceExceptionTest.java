/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ocp.reference.person.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gov.va.ocp.framework.messages.MessageSeverity;

/**
 *
 * @author rthota
 */
public class PersonServiceExceptionTest {
	PersonServiceException instance;

	private static final String KEY = "EVSS-7001";
	private static final String MESSAGE = "Invalid Pid";

	@Before
	public void setUp() {
		instance = new PersonServiceException(KEY, MESSAGE, MessageSeverity.ERROR, null);
	}

	/**
	 * Test of getSeverity method, of class PersonServiceException.
	 */
	@Test
	public void testGetSeverity() {
		System.out.println("getSeverity");
		MessageSeverity expResult = MessageSeverity.ERROR;
		MessageSeverity result = instance.getSeverity();
		assertEquals(expResult, result);
	}

	/**
	 * Test of getKey method, of class PersonServiceException.
	 */
	@Test
	public void testGetKey() {
		System.out.println("getKey");
		String expResult = KEY;
		String result = instance.getKey();
		assertEquals(expResult, result);
	}

	/**
	 * Test of setKey method, of class PersonServiceException.
	 */

	/**
	 * Test of getMessage method, of class PersonServiceException.
	 */
	@Test
	public void testGetMessage() {
		System.out.println("getMessage");
		String expResult = MESSAGE;
		String result = instance.getMessage();
		assertEquals(expResult, result);

	}

}
