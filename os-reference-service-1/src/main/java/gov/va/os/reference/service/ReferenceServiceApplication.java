package gov.va.os.reference.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.DispatcherServlet;

import brave.sampler.Sampler;
import gov.va.os.reference.service.config.ReferenceServiceConfig;

/**
 * An <tt>Ascent Demo Service Application</tt> enabled for Spring Boot Application, 
 * Spring Cloud Netflix Feign Clients, Hystrix circuit breakers, Swagger and 
 * AspectJ's @Aspect annotation.
 *
 */
@SpringBootApplication
@EnableDiscoveryClient //needed to reach out to spring cloud config, eureka
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableFeignClients
@EnableHystrix
@EnableCaching
@EnableAsync
@Import(ReferenceServiceConfig.class)
public class ReferenceServiceApplication extends SpringBootServletInitializer {

    @Autowired
    private WebMvcProperties webMvcProperties;
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ReferenceServiceApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ReferenceServiceApplication.class, args);
    } 
    
    @Bean
    Sampler alwaysSampler() {
    	return Sampler.ALWAYS_SAMPLE;
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setDispatchOptionsRequest(
                this.webMvcProperties.isDispatchOptionsRequest());
        dispatcherServlet.setDispatchTraceRequest(
                this.webMvcProperties.isDispatchTraceRequest());
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(
                this.webMvcProperties.isThrowExceptionIfNoHandlerFound());
        dispatcherServlet.setThreadContextInheritable(true);
        return dispatcherServlet;
    }
}
