package gov.va.ocp.reference.person.transform;

import gov.va.ocp.reference.framework.transfer.ProviderTransferObjectMarker;
import gov.va.ocp.reference.framework.transfer.DomainTransferObjectMarker;

/**
 * The contract for transforming a domain {@link DomainTransferObjectMarker} object to a provider
 * {@link ProviderTransferObjectMarker} object.
 * <p>
 * Implementations should declare the generic parameters with the specific classes involved in the transformation.
 *
 * @author aburkholder
 *
 * @param &lt;D extends DomainTransferObjectMarker&gt; the domain object type
 * @param &lt;P extends ProviderTransferObjectMarker&gt; the provider object type
 */
public abstract class AbstractDomainToProvider<D extends DomainTransferObjectMarker, P extends ProviderTransferObjectMarker>
		extends AbstractBaseTransformer {

	/**
	 * The contract for transforming a domain object to a provider object.
	 * <p>
	 * Implementations should declare the generic parameters with the specific classes involved in the transformation.
	 *
	 * @param domainObject the type of the domain object to transform
	 * @return P the transformed equivalent provider object
	 */
	public abstract P transform(D domainObject);

}
