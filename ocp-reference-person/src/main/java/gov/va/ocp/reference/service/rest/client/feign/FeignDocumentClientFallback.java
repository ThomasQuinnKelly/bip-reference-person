package gov.va.ocp.reference.service.rest.client.feign;

import org.springframework.stereotype.Component;

@Component
public class FeignDocumentClientFallback implements FeignDocumentClient {

//	@Override
//	public ResponseEntity<GetDocumentTypesResponse> getDocumentTypes() {
//		final GetDocumentTypesResponse response = new GetDocumentTypesResponse();
//		response.addMessage(MessageSeverity.FATAL, "DOCUMENT_SERVICE_NOT_AVAILABLE",
//				"This is feign fallback handler, the document service was not available");
//		return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
//	}

}
