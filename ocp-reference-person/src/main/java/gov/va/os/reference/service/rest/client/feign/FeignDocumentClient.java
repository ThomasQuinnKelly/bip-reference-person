package gov.va.os.reference.service.rest.client.feign;

import org.springframework.cloud.openfeign.FeignClient;

import gov.va.os.reference.service.config.ReferenceDocumentServiceFeignConfig;

@FeignClient(value = "reference-document-service",
		fallback = FeignDocumentClientFallback.class,
		configuration = ReferenceDocumentServiceFeignConfig.class)
public interface FeignDocumentClient { // NOSONAR not a functional interface

//	@RequestMapping(value = "/document/v1/documentTypes", method = RequestMethod.GET)
//	ResponseEntity<GetDocumentTypesResponse> getDocumentTypes();

}
