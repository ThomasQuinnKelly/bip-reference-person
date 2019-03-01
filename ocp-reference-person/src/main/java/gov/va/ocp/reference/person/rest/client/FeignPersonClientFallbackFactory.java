package gov.va.ocp.reference.person.rest.client;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixTimeoutException;

import feign.hystrix.FallbackFactory;
import gov.va.ocp.reference.framework.messages.MessageSeverity;
import gov.va.ocp.reference.person.model.PersonByPidDomainRequest;
import gov.va.ocp.reference.person.model.PersonByPidDomainResponse;


@Component
/**
 * This class provides the Hystrix fallback implementation for Feign Client calls to the service
 *
 * @author vvanapalli
 *
 */
public class FeignPersonClientFallbackFactory implements FallbackFactory<FeignPersonClient> {


	@Override
	public FeignPersonClient create(Throwable cause) {
		
		return new FeignPersonClient() {
			@Override
			public PersonByPidDomainResponse personByPid(@RequestBody final PersonByPidDomainRequest personInfoRequest) {
				
				String message = cause.getMessage();
				
				if (cause instanceof HystrixRuntimeException ) {
		            message = "HystrixRuntimeException: " + message;
		            
		        }else if (cause instanceof HystrixTimeoutException ) {
		            message = "HystrixTimeoutException: " + message;
		            
		        }
				
				
                /* 
                 * HystrixBadRequestException does not trigger fallback and 
                 * hence not handled here		        
                 * else if (cause instanceof HystrixBadRequestException ) {
		            message = "HystrixBadRequestException: " + message;
		            
		        }*/
				PersonByPidDomainResponse response = new PersonByPidDomainResponse();
				response.setDoNotCacheResponse(true);
				response.addMessage(MessageSeverity.FATAL, HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
					message, HttpStatus.SERVICE_UNAVAILABLE);
				return response;
			}


			
		};
	}
	

}