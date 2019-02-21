package gov.va.os.reference.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import gov.va.os.reference.service.ReferenceServiceProperties;
import gov.va.os.reference.service.api.DemoService;
import gov.va.os.reference.service.api.v1.transfer.Demo;
import gov.va.os.reference.service.api.v1.transfer.DemoServiceResponse;
import gov.va.os.reference.service.utils.HystrixCommandConstants;

@Component
@Qualifier("IMPL")
@RefreshScope
@DefaultProperties(groupKey = HystrixCommandConstants.REFERENCE_DEMO_SERVICE_GROUP_KEY)
public class DemoServiceImpl implements DemoService {

	private static final String THROWN_ON_PURPOSE = "Thrown on purpose!";

	@Autowired
	private ReferenceServiceProperties properties;

	@Value("${service-1.sampleProperty}")
	private String sampleProperty;

	@Override
	@HystrixCommand(fallbackMethod = "getFallbackDemoResponse", commandKey = "DemoServiceReadCommand")
	public DemoServiceResponse read(final String name) {
		if ("error".equals(name)) {
			throw new RuntimeException(THROWN_ON_PURPOSE); // NOSONAR intentional generic exception
		}
		final DemoServiceResponse response = new DemoServiceResponse();
		final Demo demo = new Demo();
		demo.setName(name);
		demo.setDescription("description for demo with name: " + name + ".  " +
				"FYI Sample property is as followed from 2 different sources " +
				"[ReferenceServiceConfig] and [Autowired]: [" +
				properties.getSampleProperty() + "][" + sampleProperty + "]");
		response.setDemo(demo);
		return response;
	}

	public DemoServiceResponse getFallbackDemoResponse(final String name) {
		final DemoServiceResponse response = new DemoServiceResponse();
		final Demo demo = new Demo();
		demo.setName(name);
		demo.setDescription("Fallback Response to the client");
		response.setDemo(demo);
		return response;
	}
}
