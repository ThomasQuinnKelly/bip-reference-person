package gov.va.os.reference.service.rest.client.feign;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import gov.va.os.reference.document.service.api.transfer.GetDocumentTypesResponse;
import gov.va.os.reference.framework.messages.MessageSeverity;

@Component
public class FeignDocumentClientFallback implements FeignDocumentClient {

	@Override
	public ResponseEntity<GetDocumentTypesResponse> getDocumentTypes() {
		final GetDocumentTypesResponse response = new GetDocumentTypesResponse();
		response.addMessage(MessageSeverity.FATAL, "DOCUMENT_SERVICE_NOT_AVAILABLE",
				"This is feign fallback handler, the document service was not available");
		return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
	}

}
