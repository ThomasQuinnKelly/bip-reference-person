mavenGitflowPipeline {

    //Sonar Github Credentials - Settings this value will configure the pipeline to use this credential
    //to connect to github during sonar PR scans, adding comments for any violations found
    sonarGithubCredentials = 'dsva-github'

    //Github credential ID to use for releases
    githubCredentials = 'epmo-github'

    //Specify to use the fortify maven plugin, instead of the Ant task to execute the fortify scan
    useFortifyMavenPlugin = true

    /*************************************************************************
    * Docker Build Configuration
    *************************************************************************/

    // Map of Image Names to sub-directory in the repository. If this is value is non-empty, 
    // the build pipeline will build all images specified in the map. The example below will build an image tagged as 
    // `bip-reference-person:latest` using the Docker context of `./bip-reference-person`.
    dockerBuilds = [
        'bip-reference-person': 'bip-reference-person'
    ]

    /*************************************************************************
    * Functional Testing Configuration
    *************************************************************************/

    //Directory that contains the cucumber reports
    cucumberReportDirectory = "bip-reference-inttest/target/site"

    //Additional Mavn options to use when running functional test cases
    cucumberOpts = "--tags @DEV"

    /*************************************************************************
    * OpenShift Deployment Configuration
    *
    * This section only applied to builds running on the OpenShift platform.
    * This section should be omitted if you are using Helm for deployments on
    * Kubernetes.
    *************************************************************************/
    //Path to your applications Openshift deployment template
    deploymentTemplates = ["template.yaml"]
    
    //Default Deployment parameters used to configure your Openshift deployment template
    defaultDeploymentParameters = [
        'APP_NAME': 'bip-reference-person',
        'IMAGE': 'bip-reference-person',
        'SPRING_PROFILES': 'dev'
    ]

    //Functional Testing Deployment parameters used to configure your Openshift deployment template
    functionalTestDeploymentParameters = [
        'APP_NAME': 'bip-reference-person',
        'IMAGE': 'bip-reference-person',
        'SPRING_PROFILES': 'dev'
    ]

    //Performance Testing Deployment parameters used to configure your Openshift deployment template
    performanceTestDeploymentParameters = [
         'APP_NAME': 'bip-reference-person',
         'IMAGE': 'bip-reference-person',
         'SPRING_PROFILES': 'dev'
    ]

    /*************************************************************************
    * Helm Deployment Configuration
    *
    * This section only applied to builds running on the Kubernetes platform.
    * This section should be omitted if you are using Openshift templates for
    * deployment on Openshift.
    *************************************************************************/

    //Git Repository that contains your Helm chart
    chartRepository = "https://github.ec.va.gov/EPMO/bip-external-config"

    //Path to your chart directory within the above repository
    chartPath = "charts/bip-reference-person"

    //Jenkins credential ID to use when connecting to repository. This defaults to `github` if not specified
    chartCredentialId = "github"

    //Value YAML file used to configure the Helm deployments used for functional and performance testing.
    defaultChartValueFile = "testing.yaml"
    functionalTestDeploymentParameters = "testing.yaml"
    performanceTestDeploymentParameters = "testing.yaml"

    //Release name to use
    chartReleaseName = "bip-reference-person"
}