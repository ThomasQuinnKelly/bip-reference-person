package gov.va.bip.reference.person.messages;

import java.util.Locale;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import gov.va.bip.framework.messages.MessageKey;

/**
 * The source for messages generated by the micro-service code.
 *
 * @author aburkholder
 */
public enum PersonMessageKeys implements MessageKey {

	/** Minimum allowed value validation for PID; no args */
	BIP_PERSON_INFO_REQUEST_PID_MIN("Min.personInfoRequest.participantID",
			"PersonInfoRequest.participantID cannot be zero"),
	/** Minimum allowed value validation for PID; no args */
	BIP_PERSON_DOCS_METADATA_REQUEST_PID_MIN("Min.personDocsMetadataRequest.participantID",
			"personDocsMetadataRequest.participantID cannot be zero"),
	/** PID cannot be null validation; no args */
	BIP_PERSON_INFO_REQUEST_PID_NOTNULL("NotNull.personInfoRequest.participantID",
			"PersonInfoRequest.participantID cannot be null"),
	/** Payload cannot be null validation; no args */
	BIP_PERSON_INFO_REQUEST_NOTNULL("NotNull.personInfoRequest",
			"PersonInfoRequest Payload cannot be null"),
	/** Payload cannot be null validation; no args */
	BIP_PERSON_DOCS_METADATA_NOTNULL("NotNull.personDocsMetadata", "PersonDocsMetadata Payload cannot be null"),
	/** Response has different PID than the request; no args */
	BIP_PERSON_INFO_REQUEST_PID_INCONSISTENT("bip.reference.person.info.request.pid.inconsistent",
			"Response returned an invalid Participant ID."),
	/** Response has different PID than the logged in user; no args */
	BIP_PERSON_INFO_REQUEST_PID_INVALID("bip.reference.person.info.request.pid.invalid",
			"Response has different PID than the logged in user."),
	/** Date value given in the request is not valid; no args */
	BIP_PERSON_INVALID_DATE("bip.reference.person.invalid.date", "Date value given in the request is not valid."),
	/** Pid value could not be found; no args */
	BIP_PERSON_INFO_REQUEST_PID_NOT_FOUND("bip.reference.person.pid.not.found", "Pid value could not be found.");

	/** The filename "name" part of the properties file to get from the classpath */
	private static final String PROPERTIES_FILE = "messages";
	/** The message source containing properties for this enum */
	private static ReloadableResourceBundleMessageSource messageSource;
	/* Populate the message source from the properties file */
	static {
		messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:" + PROPERTIES_FILE);
		messageSource.setDefaultEncoding("UTF-8");
	}

	/** The key - must be identical to the key in framework-messages.properties */
	private String key;
	/** A default message, in case the key is not found in framework-messages.properties */
	private String defaultMessage;

	/**
	 * Construct keys with their property file counterpart key and a default message.
	 *
	 * @param key - the key as declared in the properties file
	 * @param defaultMessage - in case the key cannot be found
	 */
	private PersonMessageKeys(final String key, final String defaultMessage) {
		this.key = key;
		this.defaultMessage = defaultMessage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.bip.framework.messages.MessageKey#getKey()
	 */
	@Override
	public String getKey() {
		return this.key;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gov.va.bip.framework.messages.MessageKey#getMessage(java.lang.Object[])
	 */
	@Override
	public String getMessage(final String... params) {
		return messageSource.getMessage(this.getKey(), params, this.defaultMessage, Locale.getDefault());
	}
}
