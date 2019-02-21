package gov.va.os.reference.service.api;


import gov.va.os.reference.service.api.v1.transfer.DemoServiceResponse;

public interface DemoService {

  /**
   * Reads the DemoServiceResponse.
   *
   * @param name Read Name
   * @return the demo service response
   */
	DemoServiceResponse read(String name);
}
