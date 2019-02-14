package gov.va.os.reference.framework.service;

import org.junit.Test;

import gov.va.os.reference.framework.service.BaseServiceAspect;

public class BaseServiceAspectTest {

    @Test
    public void testStandardServiceMethod(){
        BaseServiceAspect.publicStandardServiceMethod();
        //does nothing
    }
    
    @Test
    public void testRestControllereMethod(){
        BaseServiceAspect.restController();;
        //does nothing
    }

    @Test
    public void testServiceImplMethod(){
        BaseServiceAspect.serviceImpl();
        //does nothing
    }
}
