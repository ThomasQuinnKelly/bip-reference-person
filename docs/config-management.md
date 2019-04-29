# Configuration Management
Developers and Operations staff need the ability to configure their applications at runtime without the need to create a new build of the software. This application configuration should be stored in source control. The Configuration Managment service's role is to provide this runtime configuration to the application during bootstrap and to notify application if there is a configuration change made, instructing them to reload their configuration. Consul Key/Value store is the chosen Configuration Management solution of the BIP platform.

The reference application uses the [Spring Cloud Consul](https://cloud.spring.io/spring-cloud-consul/single/spring-cloud-consul.html) library to integrate with the Consul K/V store.

## Loading Configuration into Consul
Solution is TBD

## Authenticating to Consul
See [Vault Consul Token Generation](https://www.vaultproject.io/docs/secrets/consul/index.html)</br>
See [Spring Cloud Vault Consul](https://cloud.spring.io/spring-cloud-vault/single/spring-cloud-vault.html#vault.config.backends.consul)

## Retreiving Key/Values
By default the following paths in Consul are looked at and all k/v pairs found at those paths are loaded as Properties. Spring Cloud Consul allows using the Application name and a default context name (application) in combination with active profiles.
* `/config/{application}/{profile}`
* `/config/{application}`
* `/config/{default-context}/{profile}`
* `/config/{default-context}`

The application name is determined by the properties:
```
	spring.application.name
```

K/V pairs are loaded as Properties by the key name in Consul. For example if your key/value pair was `database.username=bob` then you would access that value in your application by refering to the property matching the key name `${database.username}`

## Watching for Config Changes
The Spring Cloud Consul library will monitor Consul for changes to the afore mentioned paths. If there is a change detected, a Refresh Event is published; the equivalent of calling the `/refresh` endpoint. Refer to the [Spring Cloud Consul](https://cloud.spring.io/spring-cloud-consul/single/spring-cloud-consul.html#spring-cloud-consul-config-watch) documentation on this for more details.
