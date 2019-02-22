package gov.va.os.reference.service.rest.client.feign;

import org.springframework.stereotype.Component;

@Component
/**
 * This class provides the Hystrix fallback implementation for Feign Client calls to the service
 *
 * @author
 *
 */
public class FeignEchoClientFallback implements FeignEchoClient {

//	TODO
//	@Override
//	public ResponseEntity<EchoHostServiceResponse> echo() {
//		EchoHostServiceResponse response = new EchoHostServiceResponse();
//		response.addMessage(MessageSeverity.FATAL, "SERVICE_NOT_AVAILABLE",
//				"This is feign fallback handler, the service wasn't available");
//		return new ResponseEntity<EchoHostServiceResponse>(response, HttpStatus.SERVICE_UNAVAILABLE);
//	}

}
