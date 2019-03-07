package gov.va.ocp.reference.person.rest.client;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixTimeoutException;

import feign.hystrix.FallbackFactory;
import gov.va.ocp.framework.exception.OcpFeignRuntimeException;
import gov.va.ocp.framework.log.OcpLogger;
import gov.va.ocp.framework.log.OcpLoggerFactory;
import gov.va.ocp.framework.messages.MessageSeverity;
import gov.va.ocp.reference.person.api.model.v1.PersonInfoRequest;
import gov.va.ocp.reference.person.api.model.v1.PersonInfoResponse;


@Component
/**
 * This class provides the Hystrix fallback implementation for Feign Client calls to the service
 *
 * @author vvanapalli
 *
 */
public class FeignPersonClientFallbackFactory implements FallbackFactory<FeignPersonClient> {

	private static final OcpLogger LOGGER = OcpLoggerFactory.getLogger(FeignPersonClientFallbackFactory.class);

	@Override
	public FeignPersonClient create(Throwable cause) {
		
		return new FeignPersonClient() {
			@Override
			public PersonInfoResponse personByPid(@RequestBody final PersonInfoRequest personInfoRequest) {
				LOGGER.info("FeignPersonClient fallback invoked");
				String message = cause.getMessage();
				
				LOGGER.error("FeignPersonClient fallback Throwable: {}", cause);
				
				if (cause instanceof HystrixRuntimeException ) {
		            message = "HystrixRuntimeException: " + message;
		            
		        }else if (cause instanceof HystrixTimeoutException ) {
		            message = "HystrixTimeoutException: " + message;
		            
		        }else if (cause instanceof OcpFeignRuntimeException ) {
		        	OcpFeignRuntimeException exception = ((OcpFeignRuntimeException)cause);
		        	PersonInfoResponse response = new PersonInfoResponse();
					response.addMessage(MessageSeverity.ERROR, 
							exception.getKey(),
							exception.getText(), 
							HttpStatus.valueOf(Integer.valueOf(exception.getStatus())));
		            return response;
		        }
				
				
                /* 
                 * HystrixBadRequestException does not trigger fallback and 
                 * hence not handled here		        
                 * else if (cause instanceof HystrixBadRequestException ) {
		            message = "HystrixBadRequestException: " + message;
		            
		        }*/
               
				PersonInfoResponse response = new PersonInfoResponse();
				response.addMessage(MessageSeverity.FATAL, 
									HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
									message, 
									HttpStatus.SERVICE_UNAVAILABLE);
				return response;
				
			}
		};
	}
	

}