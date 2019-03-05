package gov.va.ocp.reference.person.transform;

import gov.va.ocp.framework.transfer.DomainTransferObjectMarker;
import gov.va.ocp.framework.transfer.ProviderTransferObjectMarker;

/**
 * The contract for transforming a provider {@link ProviderTransferObjectMarker} object to a domain
 * {@link DomainTransferObjectMarker} object.
 * <p>
 * Implementations should declare the generic parameters with the specific classes involved in the transformation.
 *
 * @author aburkholder
 *
 * @param &lt;P extends ProviderTransferObjectMarker&gt; the provider object type
 * @param &lt;D extends DomainTransferObjectMarker&gt; the domain object type
 */
public abstract class AbstractProviderToDomain<P extends ProviderTransferObjectMarker, D extends DomainTransferObjectMarker>
		extends AbstractBaseTransformer {

	/**
	 * The contract for transforming a domain object to a provider object.
	 * <p>
	 * Implementations should declare the generic parameters with the specific classes involved in the transformation.
	 *
	 * @param providerObject the type of the provider object to transform
	 * @return D the transformed equivalent domain object
	 */
	public abstract D transform(P providerObject);

}
