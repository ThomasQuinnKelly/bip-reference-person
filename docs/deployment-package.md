# Application Deployment Package
Developers need a way to tell the platform how to deploy their application as well as any services that their application depends on. This method should allow for the development team to specify all all aspects of their service deployment in a common way across all projects.

The platform team has selected [Openshift Templates](https://docs.openshift.com/container-platform/3.6/dev_guide/templates.html) as the deployment tool they would like to use. Development teams will need to build a deliver a set of templates as their deployment artifact. 

This document will give you the basics on creating an Openshift Template for your application, but your should definately read up on the Openshift documentation to get a full understanding on how to build a template.

## Building an Openshift Template
Openshift templates are a collection of Object defintions. To deploy your application you will need the following objects at a mimimum:
* [DeploymentConfig](https://docs.openshift.com/container-platform/3.6/architecture/core_concepts/deployments.html) - Defintion of your container configuration to deploy
* [Service](https://docs.openshift.com/container-platform/3.6/architecture/core_concepts/pods_and_services.html#services) - Load Balancer in front of your container instances
* [ConfigMap](https://docs.openshift.com/container-platform/3.6/dev_guide/configmaps.html) - Stores the bootstrap configuration value for your container
* [Route](https://docs.openshift.com/container-platform/3.6/dev_guide/routes.html) - (Optional) This exposed your service externally to Openshift.

For an example template see the [BIP Reference Deployment Template](../template.yaml)

### Pipeline Provided Parameters
The Jenkins pipeline will supply some parameters to your template during deployment. These are documented in the [BIP Jenkins Library documentation](https://github.com/department-of-veterans-affairs/os-svc-jenkins-lib/blob/master/docs/common/deployment.md#pipeline-provided-parameters)

### Platform Provided ConfigMaps
The platform team will provide the following ConfigMaps and properties for use in bootstrapping your application container.
* ConfigMap Name `consul-config`
    * `CONSUL_PORT` - Consul service port number
    * `CONSUL_SCHEME` - Use HTTP or HTTPS when connecting to Consul

* ConfigMap Name `vault-config`
    * `VAULT_HOST` - Vault service hostname
    * `VAULT_PORT` - Vault service port
    * `VAULT_SCHEME` - Use HTTP or HTTPS when connecting to Vault
    * `VAULT_KUBERNETES_ROLE` - Role to use for Vault Kubernetes authentication backend
    * `vault.auth.token.file` - Path to file containing the token value for authenticating to Vault

### Platform Provided Secrets
* Secret Name `consul-acl-token`
    * `CONSUL_ACL_TOKEN` - ACL token to use when connecting to Consul

* Secret Name `va-cacerts`
    * `va-cacerts` - JKS truststore with VA CA certificates


## Configuring the pipeline with your deployment information
For information on writing your Jenkinsfile to support application deployment see the [BIP Jenkins Library documentation](https://github.com/department-of-veterans-affairs/os-svc-jenkins-lib/blob/master/docs/common/deployment.md#deployment-pipeline-configuration)