package gov.va.os.reference.framework.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import gov.va.os.reference.framework.transfer.AbstractTransferObject;
import gov.va.os.reference.framework.transfer.ServiceTransferObjectMarker;

/**
 * A base Request object capable of representing the payload of a service request.
 *
 * @see gov.va.os.reference.framework.transfer.AbstractTransferObject
 * @author jshrader
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "serviceRequest")
public class ServiceRequest extends AbstractTransferObject implements ServiceTransferObjectMarker {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8521125059263688741L;

	/**
	 * Instantiates a new rest request.
	 */
	protected ServiceRequest() {
		super();
	}

}
