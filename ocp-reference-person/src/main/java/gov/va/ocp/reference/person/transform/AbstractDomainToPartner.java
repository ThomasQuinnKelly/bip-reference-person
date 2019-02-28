package gov.va.ocp.reference.person.transform;

import gov.va.ocp.reference.framework.transfer.PartnerTransferObjectMarker;
import gov.va.ocp.reference.framework.transfer.ServiceTransferObjectMarker;

/**
 * The contract for transforming a domain {@link ServiceTransferObjectMarker} object to a partner
 * {@link PartnerTransferObjectMarker} object.
 * <p>
 * Implementations should declare the generic parameters with the specific classes involved in the transformation.
 *
 * @author aburkholder
 *
 * @param &lt;D extends ServiceTransferObjectMarker&gt; the domain object type
 * @param &lt;P extends PartnerTransferObjectMarker&gt; the partner object type
 */
public abstract class AbstractDomainToPartner<D extends ServiceTransferObjectMarker, P extends PartnerTransferObjectMarker>
		extends AbstractBaseTransformer {

	/**
	 * The contract for transforming a domain object to a partner object.
	 * <p>
	 * Implementations should declare the generic parameters with the specific classes involved in the transformation.
	 *
	 * @param domainObject the type of the domain object to transform
	 * @return P the transformed equivalent partner object
	 */
	public abstract P transform(D domainObject);

}
