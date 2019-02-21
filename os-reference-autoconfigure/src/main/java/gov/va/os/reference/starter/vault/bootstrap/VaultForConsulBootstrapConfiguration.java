package gov.va.os.reference.starter.vault.bootstrap;

/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Collections;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.vault.config.consul.VaultConsulProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.env.VaultPropertySource;

/**
 * @author Mark Paluch
 */
@Configuration
@AutoConfigureOrder(1)
@ConditionalOnProperty(prefix = "spring.cloud.vault", name = "enabled", matchIfMissing = true)
public class VaultForConsulBootstrapConfiguration implements ApplicationContextAware,
		InitializingBean {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() {

		ConfigurableEnvironment ce = (ConfigurableEnvironment) applicationContext
				.getEnvironment();

		if (ce.getPropertySources().contains("consul-token")) {
			return;
		}

		VaultOperations vaultOperations = applicationContext
				.getBean(VaultOperations.class);
		VaultConsulProperties consulProperties = applicationContext
				.getBean(VaultConsulProperties.class);

		VaultPropertySource vaultPropertySource = new VaultPropertySource(
				vaultOperations, String.format("%s/creds/%s",
						consulProperties.getBackend(), consulProperties.getRole()));

		MapPropertySource mps = new MapPropertySource("consul-token",
				Collections.singletonMap("spring.cloud.consul.token",
						vaultPropertySource.getProperty("token")));

		ce.getPropertySources().addFirst(mps);
	}
}
