/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ocp.reference.framework.config;

import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import gov.va.ocp.reference.framework.config.BasePropertiesConfig;
import gov.va.ocp.reference.framework.config.BasePropertiesConfig.BasePropertiesEnvironment;

/**
 *
 * @author rthota
 */
public class BasePropertiesConfigTest {

    /**
     * Test of properties method, of class BasePropertiesConfig.
     */
    @Test
    public void testProperties() {
        PropertySourcesPlaceholderConfigurer result = BasePropertiesConfig.properties();
        assertNotNull(result);
        
        BasePropertiesEnvironment basePropertiesEnvironment = new BasePropertiesEnvironment();
        basePropertiesEnvironment.postConstruct();
    }
    @Test
    public void testConstructor() {
    	BasePropertiesConfig test = new BasePropertiesConfig();
    	assertNotNull(test);
    }
}