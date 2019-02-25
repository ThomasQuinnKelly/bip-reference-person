package gov.va.ocp.reference.person.transform;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import gov.va.ocp.reference.framework.log.ReferenceLogger;
import gov.va.ocp.reference.framework.log.ReferenceLoggerFactory;
import gov.va.ocp.reference.framework.transfer.PartnerTransferObjectMarker;
import gov.va.ocp.reference.framework.transfer.ServiceTransferObjectMarker;

/**
 * Transform data between the partner model and the service model.
 * <p>
 * Specifically transforms {@link PartnerTransferObjectMarker} objects to {@link ServiceTransferObjectMarker} objects, and vice versa.
 *
 * @param P <b>extends PartnerTransferObjectMarker</b> - the partner model object
 * @param S <b>extends ServiceTransferObjectMarker</b> - the domain model object
 */
public abstract class AbstractPartnerTransformer<P extends PartnerTransferObjectMarker, S extends ServiceTransferObjectMarker> {

	/** Constant for the logger for this class */
	public static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(AbstractPartnerTransformer.class);

	/**
	 * Transform an {@link PartnerTransferObjectMarker} to an {@link ServiceTransferObjectMarker}
	 *
	 * @param toTransform P the PartnerTransferObjectMarker to transform
	 * @return S the ServiceTransferObjectMarker
	 */
	public abstract S transformToService(P toTransform);

	/**
	 * Transform an {@link ServiceTransferObjectMarker} to an {@link PartnerTransferObjectMarker}
	 *
	 * @param toTransform S the ServiceTransferObjectMarker to transform
	 * @return P the PartnerTransferObjectMarker
	 */
	public abstract P transformToPartner(S toTransform);

	/**
	 * Convert XMLGregorianCalendar to java.util.Date
	 *
	 * @param xmlDate
	 * @return Date
	 */
	public static Date toDate(final XMLGregorianCalendar xmlDate) {
		if (xmlDate == null) {
			return null;
		}
		return xmlDate.toGregorianCalendar().getTime();
	}

	/**
	 * convert java.util.Date to XMLGregorianCalendar
	 *
	 * @param date the java.util.Date
	 * @param datatypeFactoryManager the object that is used to get the datatype factory (added to ease testing, see
	 *            {@link DatatypeFactoryManager})
	 * @return XMLGregorianCalendar object
	 */
	public static XMLGregorianCalendar toXMLGregorianCalendar(final Date date, final DatatypeFactoryManager datatypeFactoryManager) {
		try {
			final GregorianCalendar gc = new GregorianCalendar(Locale.getDefault());
			gc.setTimeInMillis(date.getTime());
			return datatypeFactoryManager.getDatatypeFactory().newXMLGregorianCalendar(gc);

		} catch (final DatatypeConfigurationException dcEx) { // NOSONAR
			LOGGER.error(dcEx.getMessage());
			return null;
		}
	}

	/**
	 * Convert "now" to XMLGregorianCalendar
	 *
	 * @param datatypeFactoryManager the object that is used to get the datatype factory (added to ease testing, see
	 *            {@link DatatypeFactoryManager})
	 * @return XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar getCurrentDate(final DatatypeFactoryManager datatypeFactoryManager) {
		try {
			return datatypeFactoryManager.getDatatypeFactory().newXMLGregorianCalendar(new GregorianCalendar(Locale.getDefault()));

		} catch (final DatatypeConfigurationException dcEx) { // NOSONAR
			LOGGER.error(dcEx.getMessage());
			return null;
		}
	}

	/**
	 * This class is added to substitute for the static method javax.xml.datatype.DatatypeFactory.newInstance() to aid in testing. The
	 * class can be mocked and an exception can be thrown while calling the static method to test the exception handling code. The call
	 * to the static method is wrapped in getDatatypeFactory() method in this class.
	 *
	 */
	public static class DatatypeFactoryManager {
		/**
		 * This method is a wrapper for the static method javax.xml.datatype.DatatypeFactory.newInstance() call. While testing, a mock
		 * object could deliberately throw an exception to test the error handling code.
		 *
		 * @return a DatatypeFactory instance
		 * @throws DatatypeConfigurationException
		 */
		public DatatypeFactory getDatatypeFactory() throws DatatypeConfigurationException {
			return DatatypeFactory.newInstance();
		}
	}
}
