package gov.va.ocp.reference.service.transform.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;

import gov.va.ocp.reference.service.model.person.v1.PersonInfoResponse;
import gov.va.ocp.reference.service.transform.AbstractPartnerTransformer;
import gov.va.ocp.reference.service.utils.HystrixCommandConstants;
import gov.va.ocp.reference.partner.person.ws.transfer.FindPersonsByPtcpntIds;

@Service(value = TransformFindPersonByPtcpntId.BEAN_NAME)
@Qualifier(TransformFindPersonByPtcpntId.BEAN_NAME)
@Scope("prototype")
@RefreshScope
@DefaultProperties(groupKey = HystrixCommandConstants.REFERENCE_PERSON_DEMO_SERVICE_GROUP_KEY)
public class TransformFindPersonByPtcpntId extends AbstractPartnerTransformer<FindPersonsByPtcpntIds, PersonInfoResponse> {
	public static final String BEAN_NAME = "transformFindPersonByPtcpntId";

	@Override
	public PersonInfoResponse transformToService(FindPersonsByPtcpntIds toTransform) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FindPersonsByPtcpntIds transformToPartner(PersonInfoResponse toTransform) {
		// TODO Auto-generated method stub
		return null;
	}

}
