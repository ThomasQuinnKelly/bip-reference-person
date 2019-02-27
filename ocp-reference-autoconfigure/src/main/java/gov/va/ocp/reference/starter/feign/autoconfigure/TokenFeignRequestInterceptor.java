package gov.va.ocp.reference.starter.feign.autoconfigure;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import gov.va.ocp.reference.framework.log.ReferenceLogger;
import gov.va.ocp.reference.framework.log.ReferenceLoggerFactory;
import gov.va.ocp.reference.security.jwt.JwtTokenService;

/**
 * An implementation of {@link RequestInterceptor} that adds the JWT token
 * from the originating request, and adds it to the outgoing request. No changes are
 * made to the response.
 * <p>
 * Use this class when making feign assisted (e.g. {@code @EnableFeignClients}) inter-=service REST calls that require PersonTraits.
 */
public class TokenFeignRequestInterceptor implements RequestInterceptor {

	private static final ReferenceLogger LOGGER = ReferenceLoggerFactory.getLogger(TokenFeignRequestInterceptor.class);

	@Autowired
	private JwtTokenService tokenService;

	/**
	 * Add token header from the originating request to the outgoing request.
	 * No changes made to the response.
	 *
	 * @see feign.RequestInterceptor#apply(feign.RequestTemplate)
	 */
	@Override
	public void apply(RequestTemplate template) {
		Map<String, String> tokenMap = tokenService.getTokenFromRequest();
		for (Map.Entry<String, String> token : tokenMap.entrySet()) {
			LOGGER.info("Adding Token Header {} {}", token.getKey(), token.getValue());
			template.header(token.getKey(), token.getValue());
		}
	}
}