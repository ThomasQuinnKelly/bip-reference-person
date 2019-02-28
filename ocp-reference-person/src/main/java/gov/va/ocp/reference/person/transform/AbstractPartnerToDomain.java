package gov.va.ocp.reference.person.transform;

import gov.va.ocp.reference.framework.transfer.PartnerTransferObjectMarker;
import gov.va.ocp.reference.framework.transfer.ServiceTransferObjectMarker;

/**
 * The contract for transforming a partner {@link PartnerTransferObjectMarker} object to a domain
 * {@link ServiceTransferObjectMarker} object.
 * <p>
 * Implementations should declare the generic parameters with the specific classes involved in the transformation.
 *
 * @author aburkholder
 *
 * @param &lt;P extends PartnerTransferObjectMarker&gt; the partner object type
 * @param &lt;D extends ServiceTransferObjectMarker&gt; the domain object type
 */
public abstract class AbstractPartnerToDomain<P extends PartnerTransferObjectMarker, D extends ServiceTransferObjectMarker>
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