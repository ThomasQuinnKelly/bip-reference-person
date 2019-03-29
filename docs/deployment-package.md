# Application Deployment Package
Developers need a way to tell the platform how to deploy their application as well as any services that their application depends on. This method should allow for the development team to specify all all aspects of their service deployment in a common way across all projects.

The platform team has selected [Helm](https://helm.sh/docs/) as the deployment tool they would like to use. Development teams will need to build a deliver a Helm package as their deployment artifact. 

This document will give you the basics on creating a Helm deployment package for your application, but your should definately read up on the Helm documentation to get a full understanding on how to build a package.

## Building an Openshift Template


### Pipeline Provided Parameters
The Jenkins pipeline will supply some parameters to your template during deployment. These are documented in the [BIP Jenkins Library documentation](https://github.com/department-of-veterans-affairs/os-svc-jenkins-lib/blob/master/docs/common/deployment.md#pipeline-provided-parameters)

### Platform Provided ConfigMaps
The platform team will provide the following ConfigMaps and properties for use in bootstrapping your application container.
* ConfigMap Name ????
    * `consul.host` - Consul service hostname
    * `consul.port` - Consul service port number
    * `vault.host` - Vault service hostname
    * `vault.port` - Vault service port
    * `vault.appRole` - Vault application role
    * `vault.auth.token.file` - Path to file containing the token value for authenticating to Vault


## Configuring the pipeline with your deployment information
For information on writing your Jenkinsfile to support application deployment see the [BIP Jenkins Library documentation](https://github.com/department-of-veterans-affairs/os-svc-jenkins-lib/blob/master/docs/common/deployment.md#deployment-pipeline-configuration)