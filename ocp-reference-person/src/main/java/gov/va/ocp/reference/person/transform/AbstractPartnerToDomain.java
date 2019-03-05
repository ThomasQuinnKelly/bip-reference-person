package gov.va.ocp.reference.person.transform;

import gov.va.ocp.framework.transfer.DomainTransferObjectMarker;
import gov.va.ocp.framework.transfer.PartnerTransferObjectMarker;

/**
 * The contract for transforming a partner {@link PartnerTransferObjectMarker} object to a domain
 * {@link DomainTransferObjectMarker} object.
 * <p>
 * Implementations should declare the generic parameters with the specific classes involved in the transformation.
 *
 * @author aburkholder
 *
 * @param &lt;P extends PartnerTransferObjectMarker&gt; the partner object type
 * @param &lt;D extends DomainTransferObjectMarker&gt; the domain object type
 */
public abstract class AbstractPartnerToDomain<P extends PartnerTransferObjectMarker, D extends DomainTransferObjectMarker>
		extends AbstractBaseTransformer {

	/**
	 * The contract for transforming a domain object to a partner object.
	 * <p>
	 * Implementations should declare the generic parameters with the specific classes involved in the transformation.
	 *
	 * @param partnerObject the type of the partner object to transform
	 * @return D the transformed equivalent domain object
	 */
	public abstract D transform(P partnerObject);

}
