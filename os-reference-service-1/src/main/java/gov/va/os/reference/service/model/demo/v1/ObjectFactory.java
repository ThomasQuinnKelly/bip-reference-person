
package gov.va.os.reference.service.model.demo.v1;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.va.os.reference.service.api.v1.transfer package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.va.os.reference.service.api.v1.transfer
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DemoServiceResponse }
     * 
     */
    public DemoServiceResponse createDemoServiceResponse() {
        return new DemoServiceResponse();
    }

    /**
     * Create an instance of {@link Demo }
     * 
     */
    public Demo createDemo() {
        return new Demo();
    }

    /**
     * Create an instance of {@link DemoServiceRequest }
     * 
     */
    public DemoServiceRequest createDemoServiceRequest() {
        return new DemoServiceRequest();
    }

    /**
     * Create an instance of {@link EchoHostServiceResponse }
     * 
     */
    public EchoHostServiceResponse createEchoHostServiceResponse() {
        return new EchoHostServiceResponse();
    }

    /**
     * Create an instance of {@link Host }
     * 
     */
    public Host createHost() {
        return new Host();
    }

    /**
     * Create an instance of {@link ServiceInstancesServiceResponse }
     * 
     */
    public ServiceInstancesServiceResponse createServiceInstancesServiceResponse() {
        return new ServiceInstancesServiceResponse();
    }

    /**
     * Create an instance of {@link ServiceInstanceDetail }
     * 
     */
    public ServiceInstanceDetail createServiceInstanceDetail() {
        return new ServiceInstanceDetail();
    }

}
