
package gov.va.os.reference.service.model.demo.v1;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import gov.va.os.reference.framework.service.ServiceResponse;


/**
 * <p>Java class for ServiceInstancesServiceResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceInstancesServiceResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.va.gov/reference/framework/schema/v1}serviceResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="serviceInstanceDetail" type="{http://gov.va/model/v1}serviceInstanceDetail" maxOccurs="100" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceInstancesServiceResponse", propOrder = {
    "serviceInstanceDetails"
})
@XmlRootElement(name = "serviceInstancesServiceResponse")
public class ServiceInstancesServiceResponse
    extends ServiceResponse
{

    @XmlElement(name = "serviceInstanceDetail")
    @Size(min = 0, max = 100)
    @Valid
    protected List<ServiceInstanceDetail> serviceInstanceDetails;

    /**
     * Gets the value of the serviceInstanceDetails property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceInstanceDetails property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceInstanceDetails().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceInstanceDetail }
     * 
     * 
     */
    public List<ServiceInstanceDetail> getServiceInstanceDetails() {
        if (serviceInstanceDetails == null) {
            serviceInstanceDetails = new ArrayList<ServiceInstanceDetail>();
        }
        return this.serviceInstanceDetails;
    }

}
