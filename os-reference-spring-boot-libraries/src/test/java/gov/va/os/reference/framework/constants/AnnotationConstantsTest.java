/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.os.reference.framework.constants;

import org.junit.Test;

import gov.va.os.reference.framework.constants.AnnotationConstants;

import static org.junit.Assert.*;

/**
 *
 * @author rthota
 */
public class AnnotationConstantsTest {
	public static final String UNCHECKED = "unchecked";
	
    @Test
    public void annotationConstantsTest() throws Exception {
        assertEquals(UNCHECKED, AnnotationConstants.UNCHECKED);
    }     
    
    @Test(expected = IllegalStateException.class)
    public void annotationConstantsConstructor() throws Exception {
    		new AnnotationConstants();
    }    
}
