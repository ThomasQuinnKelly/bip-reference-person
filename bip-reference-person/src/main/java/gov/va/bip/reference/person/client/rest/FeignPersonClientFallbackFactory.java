package gov.va.bip.reference.person.client.rest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixTimeoutException;

import feign.hystrix.FallbackFactory;
import gov.va.bip.framework.exception.BipFeignRuntimeException;
import gov.va.bip.framework.log.BipLogger;
import gov.va.bip.framework.log.BipLoggerFactory;
import gov.va.bip.framework.messages.MessageSeverity;
import gov.va.bip.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.bip.reference.person.api.model.v1.PersonInfoResponse;

/**
 * This class provides the Hystrix fallback implementation for Feign Client calls to the service
 *
 * @author vvanapalli
 *
 */
@Component
public class FeignPersonClientFallbackFactory implements FallbackFactory<FeignPersonClient> {

	private static final BipLogger LOGGER = BipLoggerFactory.getLogger(FeignPersonClientFallbackFactory.class);

	@Override
	public FeignPersonClient create(final Throwable cause) {

		return (@RequestBody final PersonInfoRequest personInfoRequest) -> {
			LOGGER.info("FeignPersonClient fallback invoked");
			String message = cause.getMessage();

			LOGGER.error("FeignPersonClient fallback Throwable: {}", cause);

			if (cause instanceof HystrixRuntimeException) {
				message = "HystrixRuntimeException: " + message;

			} else if (cause instanceof HystrixTimeoutException) {
				message = "HystrixTimeoutException: " + message;

			} else if (cause instanceof BipFeignRuntimeException) {
				BipFeignRuntimeException exception = (BipFeignRuntimeException) cause;
				PersonInfoResponse response1 = new PersonInfoResponse();
				response1.addMessage(MessageSeverity.ERROR,
						exception.getExceptionData().getKey(),
						exception.getMessage(),
						exception.getExceptionData().getStatus());
				return response1;
			}

			/*
			 * HystrixBadRequestException does not trigger fallback and
			 * hence not handled here
			 * else_if_(_cause_instanceof_HystrixBadRequestException_)_{_
			 * _message_=_"HystrixBadRequestException: "_+_message_;_
			 * _}_
			 */

			PersonInfoResponse response2 = new PersonInfoResponse();
			response2.addMessage(MessageSeverity.FATAL,
					HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
					message,
					HttpStatus.SERVICE_UNAVAILABLE);
			return response2;

		};
	}

}