package gov.va.os.reference.service.api;

import java.util.concurrent.Future;

import gov.va.os.reference.service.api.v1.transfer.DemoServiceResponse;

public interface DemoService {

  /**
   * Reads the DemoServiceResponse.
   *
   * @param name Read Name
   * @return the demo service response
   */
	DemoServiceResponse read(String name);
	
	/**
	 * Reads the DemoServiceResponse.
	 *
	 * @param id Read Name
	 * @return the future
	 */
	Future<DemoServiceResponse> readAsync(String id);
}
