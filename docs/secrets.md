# Secrets Management
On the BIP Platform, [Vault](https://www.vaultproject.io) is used to store and manage application secrets. Any sensitive data that your application needs such as username/password, private keys, authentication token, etc. should be stored in Vault.

The reference application uses the [Spring Cloud Vault](https://cloud.spring.io/spring-cloud-vault/single/spring-cloud-vault.html) library to integrate with Vault. 

## Population Application Secrets in Vault
The management of secrets in Vault is handled by the BIP Platform team. Work with them to get your applications secrets installed in Vault.

## Vault Authentication
The BIP Platform team recommends using [Vault's Kubernetes Authenication](https://www.vaultproject.io/docs/auth/kubernetes.html) method. This amounts to setting up a [Kubernetes service account](https://kubernetes.io/docs/tasks/configure-pod-container/configure-service-account/) for you application and then configuring Vault to associate an access policy with that service account. 

You application then configures the Spring Cloud Vault library to use [Kubernetes authentication](https://cloud.spring.io/spring-cloud-vault/single/spring-cloud-vault.html#vault.config.authentication.kubernetes) referencing the filepath to the service account token. Kuberenetes will handle mounting the token file into your pod container.
```yaml
spring.cloud.vault:
    authentication: KUBERNETES
    kubernetes:
        role: my-dev-role
        kubernetes-path: kubernetes
        service-account-token-file: /var/run/secrets/kubernetes.io/serviceaccount/token
```

## Retreiving Secrets
By Default the following secret paths in Vault are looked at and all secrets found at those paths are loaded as Properties. Spring Cloud Vault allows using the Application name and a default context name (application) in combination with active profiles.
* /secret/{application}/{profile}
* /secret/{application}
* /secret/{default-context}/{profile}
* /secret/{default-context}

The application name is determined by the properties:
```
spring.cloud.vault.generic.application-name
spring.cloud.vault.application-name
spring.application.name
```
Secrets can be obtained from other contexts within the generic backend by adding their paths to the application name, separated by commas. For example, given the application name usefulapp,mysql1,projectx/aws, each of these folders will be used:

* /secret/usefulapp
* /secret/mysql1
* /secret/projectx/aws