
package gov.va.os.reference.service.model.demo.v1;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import gov.va.os.reference.framework.transfer.ServiceTransferObjectMarker;


/**
 * <p>Java class for host complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="host"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="localAddress" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
 *         &lt;element name="localPort" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
 *         &lt;element name="hostName" type="{http://www.w3.org/2001/XMLSchema}anyType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "host", propOrder = {
    "localAddress",
    "localPort",
    "hostName"
})
public class Host
    implements ServiceTransferObjectMarker
{

    @XmlElement(required = true)
    @NotNull
    @Valid
    protected Object localAddress;
    @XmlElement(required = true)
    @NotNull
    @Valid
    protected Object localPort;
    @XmlElement(required = true)
    @NotNull
    @Valid
    protected Object hostName;

    /**
     * Gets the value of the localAddress property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getLocalAddress() {
        return localAddress;
    }

    /**
     * Sets the value of the localAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setLocalAddress(Object value) {
        this.localAddress = value;
    }

    /**
     * Gets the value of the localPort property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getLocalPort() {
        return localPort;
    }

    /**
     * Sets the value of the localPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setLocalPort(Object value) {
        this.localPort = value;
    }

    /**
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setHostName(Object value) {
        this.hostName = value;
    }

}
