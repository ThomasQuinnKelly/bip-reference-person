package gov.va.ocp.reference.framework.rest.provider;

import org.springframework.http.HttpStatus;

import gov.va.ocp.reference.framework.messages.Message;

import java.util.Set;

/**
 * The Interface MessagesToHttpStatusRule is the rule interface used in the MessagesToHttpStatusRulesEngine.
 *
 * @author jshrader
 */
@FunctionalInterface
public interface MessagesToHttpStatusRule {

	/**
	 * Eval.
	 *
	 * @param messagesToEval the messages to eval
	 * @return the HttpStatus
	 */
	HttpStatus eval(Set<Message> messagesToEval);
}
