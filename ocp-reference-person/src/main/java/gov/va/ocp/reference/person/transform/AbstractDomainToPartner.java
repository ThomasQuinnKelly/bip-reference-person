package gov.va.ocp.reference.person.transform;

import gov.va.ocp.framework.transfer.DomainTransferObjectMarker;
import gov.va.ocp.framework.transfer.PartnerTransferObjectMarker;

/**
 * The contract for transforming a domain {@link DomainTransferObjectMarker} object to a partner
 * {@link PartnerTransferObjectMarker} object.
 * <p>
 * Implementations should declare the generic parameters with the specific classes involved in the transformation.
 *
 * @author aburkholder
 *
 * @param &lt;D extends DomainTransferObjectMarker&gt; the domain object type
 * @param &lt;P extends PartnerTransferObjectMarker&gt; the partner object type
 */
public abstract class AbstractDomainToPartner<D extends DomainTransferObjectMarker, P extends PartnerTransferObjectMarker>
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
