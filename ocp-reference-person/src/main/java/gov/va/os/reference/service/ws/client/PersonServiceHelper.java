package gov.va.os.reference.service.ws.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import gov.va.os.reference.framework.log.ReferenceLogger;
import gov.va.os.reference.framework.log.ReferenceLoggerFactory;
import gov.va.os.reference.partner.person.ws.client.PersonWsClient;
import gov.va.os.reference.service.transform.AbstractPartnerTransformer;
import gov.va.os.reference.service.transform.impl.TransformFindPersonByPtcpntId;

@Component(PersonServiceHelper.BEAN_NAME)
public class PersonServiceHelper {
	/** Spring bean name for beans of this class */
	public static final String BEAN_NAME = "personServiceHelper";

	/** Logger */
	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(PersonServiceHelper.class);

	/** String to prepend messages for re-thrown exceptions */
	private static final String THROWSTR = "Rethrowing the following exception:  ";

	/** WS client to run all intent to file operations via SOAP */
	@Autowired
	private PersonWsClient personWsClient;

	/** Message source for error messages */
	@Autowired
	private MessageSource messageSource;

// TODO
//	@Autowired
//	private PersonServiceValidatorImpl personServiceValidatorImpl;

	@Autowired
	@Qualifier(TransformFindPersonByPtcpntId.BEAN_NAME)
	AbstractPartnerTransformer<?, ?> transformPersonInfoResponse;

	public PersonServiceHelper() {
		// TODO Auto-generated constructor stub
	}

}
