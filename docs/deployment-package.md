# Application Deployment Package
Developers need a way to tell the platform how to deploy their application as well as any services that their application depends on. This method should allow for the development team to specify all all aspects of their service deployment in a common way across all projects.

The platform team has selected [Helm](https://helm.sh/docs/) as the deployment tool they would like to use. Development teams will need to build a deliver a Helm package as their deployment artifact. 

This document will give you the basics on creating a Helm deployment package for your application, but your should definately read up on the Helm documentation to get a full understanding on how to build a package.

## Building a Helm Package
First step to building a Helm Chart is to [install the Helm client](https://helm.sh/docs/using_helm/#installing-the-helm-client). Once installed you can create a new Chart by running
```bash
helm create my-chart-name
```

This will give you a basic Helm package that will the following important files:
* Chart.yaml
* values.yaml

### Chart.yaml
This file describes your deployment package. Modify this to set your service name, description and version.

### values.yaml
This is the configuration for your Chart. The information in this file is used in the Chart Templates found in the generated `templates/` folder. Modify this file to specify the following:
* `replicaCount` - Number of container instances to start
* `image.repository` - The image definition in repository/project/image format. Ex. 172.30.1.1:5000/reference-spring-boot/ocp-reference-person
* `image.tag` - image tag. Ex. latest
* `service.port` - Port number your service runs on
* `ingress.enabled` - If true, configures an ingress route to all external requests to your service

## Configuring the pipeline with your Helm package information
*This section is a work-in-progreess and will be completed once the pipeline stages for deployment have been completed.*

Inside your Jenkinsfile add a line `helmPackage = 'path/to/helm/package'` with the value being the path to your helm package within your source code repository.
```groovy
mavenGitflowPipeline {
    ...
    helmPackage = 'helm/ocp-reference-person'
    ...
}
```