mavenGitflowPipeline {

    //Specify to use the fortify maven plugin, instead of the Ant task to execute the fortify scan
    useFortifyMavenPlugin = true

    skipFortify = true
    // Temporary flag to skip sonar scans
    skipSonar = true
    // Temporary flag to test the pipeline, MUST be removed once functional tests issues are resolved
    skipFunctionalTests = false
    // Temporary flag to test the pipeline, MUST be removed once performance tests issues are resolved
    skipPerformanceTests = true

    /*************************************************************************
    * Docker Build Configuration
    *************************************************************************/

    // Map of Image Names to sub-directory in the repository. If this is value is non-empty, 
    // the build pipeline will build all images specified in the map. The example below will build an image tagged as 
    // `blue/bip-reference-person:latest` using the Docker context of `./bip-reference-person`.
    dockerBuilds = [
        'blue/bip-reference-person': 'bip-reference-person'
    ]

    /*************************************************************************
    * Functional Testing Configuration
    *************************************************************************/

    //Directory that contains the cucumber reports
    cucumberReportDirectory = "bip-reference-inttest/target/site"

    //Additional Mavn options to use when running functional test cases
    cucumberOpts = "--tags @DEV"

    /* Postman Testing Configuration */
   
   //Set of Postman test collections to execute. Required for Postman Testing stage to run.
   //Url of the service is passed to the collection as an environment variable named BASE_URL
   postmanTestCollections = [
     'bip-reference-inttest/src/inttest/resources/bip.postman_collection.json'
   ]

    /*************************************************************************
    * OpenShift Deployment Configuration
    *
    * This section only applied to builds running on the OpenShift platform.
    * This section should be omitted if you are using Helm for deployments on
    * Kubernetes.
    *************************************************************************/
    //Path to your applications Openshift deployment template
    deploymentTemplates = ["template.yaml"]

    //Deployment parameters for review instances and dev instance
    deploymentParameters = [
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

    //TODO Remove before delivery
    chartBranch = "dev-opa-helm"

    //Jenkins credential ID to use when connecting to repository. This defaults to `github` if not specified
    chartCredentialId = "github"

    //Value YAML file used to configure the Helm deployments used for functional and performance testing.
    chartValueFunctionalTestFile = "testing.yaml"
    chartValuePerformanceTestFile = "testing.yaml"

    //Value YAML file used to configure the Helm deployments used for the Deploy Review Instance stage
    chartValueReviewInstanceFile = "reviewInstance.yaml"

    //Release name to use
    chartReleaseName = "bip-reference-person"
}
