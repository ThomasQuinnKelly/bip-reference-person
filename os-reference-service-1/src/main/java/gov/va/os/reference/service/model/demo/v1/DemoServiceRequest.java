
package gov.va.os.reference.service.model.demo.v1;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.va.os.reference.framework.service.ServiceRequest;


/**
 * <p>Java class for DemoServiceRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DemoServiceRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.va.gov/reference/framework/schema/v1}serviceRequest"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="demo" type="{http://gov.va/model/v1}demo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DemoServiceRequest", propOrder = {
    "demo"
})
@XmlRootElement(name = "demoServiceRequest")
public class DemoServiceRequest
    extends ServiceRequest
{

    @Valid
    protected Demo demo;

    /**
     * Gets the value of the demo property.
     * 
     * @return
     *     possible object is
     *     {@link Demo }
     *     
     */
    public Demo getDemo() {
        return demo;
    }

    /**
     * Sets the value of the demo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Demo }
     *     
     */
    public void setDemo(Demo value) {
        this.demo = value;
    }

}
