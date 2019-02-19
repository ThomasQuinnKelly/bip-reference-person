package gov.va.os.reference.service.rest.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.va.os.reference.document.service.api.transfer.GetDocumentTypesResponse;
import gov.va.os.reference.service.config.ReferenceDocumentServiceFeignConfig;

@FeignClient(value = "ascent-document-service",
		fallback = FeignDocumentClientFallback.class,
		configuration = ReferenceDocumentServiceFeignConfig.class)
public interface FeignDocumentClient { // NOSONAR not a functional interface

	@RequestMapping(value = "/document/v1/documentTypes", method = RequestMethod.GET)
	ResponseEntity<GetDocumentTypesResponse> getDocumentTypes();

}
