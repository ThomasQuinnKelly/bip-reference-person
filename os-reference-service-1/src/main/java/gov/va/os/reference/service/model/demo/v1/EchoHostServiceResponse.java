
package gov.va.os.reference.service.model.demo.v1;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.va.os.reference.framework.service.ServiceResponse;


/**
 * <p>Java class for EchoHostServiceResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EchoHostServiceResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.va.gov/reference/framework/schema/v1}serviceResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="host" type="{http://gov.va/model/v1}host" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EchoHostServiceResponse", propOrder = {
    "host"
})
@XmlRootElement(name = "echoHostServiceResponse")
public class EchoHostServiceResponse
    extends ServiceResponse
{

    @Valid
    protected Host host;

    /**
     * Gets the value of the host property.
     * 
     * @return
     *     possible object is
     *     {@link Host }
     *     
     */
    public Host getHost() {
        return host;
    }

    /**
     * Sets the value of the host property.
     * 
     * @param value
     *     allowed object is
     *     {@link Host }
     *     
     */
    public void setHost(Host value) {
        this.host = value;
    }

}
