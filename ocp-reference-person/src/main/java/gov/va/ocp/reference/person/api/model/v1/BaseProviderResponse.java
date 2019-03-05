package gov.va.ocp.reference.person.api.model.v1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import gov.va.ocp.framework.messages.MessageSeverity;

public class BaseProviderResponse {

	/** The messages. */
	private List<Message> messages = new ArrayList<>();

	/**
	 * Messages accumulated in the service (domain) layer.
	 *
	 * @return the messages
	 */
	public List<Message> getMessages() {
		return messages;
	}

	/**
	 * Add a {@link Message} to the list of messages.
	 *
	 * @param severity the severity
	 * @param key the message key
	 * @param text the message
	 * @param httpStatus the associated http status code
	 */
	public void add(MessageSeverity severity, String key, String text, HttpStatus httpStatus) {
		Message message = new Message(
				severity == null ? null : severity.toString(),
				key,
				text,
				httpStatus == null ? null : httpStatus.value());
		this.messages.add(message);
	}
}
