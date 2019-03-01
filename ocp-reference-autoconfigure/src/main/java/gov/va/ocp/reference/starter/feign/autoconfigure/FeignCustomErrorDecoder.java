package gov.va.ocp.reference.starter.feign.autoconfigure;

import java.io.IOException;
import java.io.Reader;
import feign.Response;
import feign.codec.ErrorDecoder;
import gov.va.ocp.reference.framework.exception.OcpFeignRuntimeException;

public class FeignCustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499) {
        	
        	StringBuffer strBuffer = new StringBuffer();
        	try {
        		
				Reader inputReader = response.body().asReader();
				int data = inputReader.read();
				while(data != -1) {
				   strBuffer.append((char)data);
				   data = inputReader.read();
			    }
				
			} catch (IOException e) {
				 return defaultErrorDecoder.decode(methodKey, response);
			}
            return new OcpFeignRuntimeException(strBuffer.toString());
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }

}