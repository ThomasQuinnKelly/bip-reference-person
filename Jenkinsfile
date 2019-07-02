mavenGitflowPipeline {

    //Sonar Github Credentials - Settings this value will configure the pipeline to use this credential
    //to connect to github during sonar PR scans, adding comments for any violations found
    sonarGithubCredentials = 'dsva-github'

    //Github credential ID to use for releases
    githubCredentials = 'epmo-github'

    dockerBuilds = [
        'blue-dev/bip-reference-person': 'bip-reference-person'
    ]

    // deploymentTemplates = ["template.yaml"]
    // deploymentParameters = [
    //     'APP_NAME': 'bip-reference-person',
    //     'IMAGE': 'bip-reference-person'
    // ]

    cucumberOpts = "--tags @DEV"

    //Credential for Nexus Deployment
    //nexusCredentialId = 'blue-dev-nexus-login'

    //Helm Chart information

    //Git Repository that contains your Helm chart
    chartRepository = "https://github.com/department-of-veterans-affairs/ocp-external-config"

    //Path to your chart directory within the above repository
    chartPath = "charts/bip-reference-person"

    //Jenkins credential ID to use when connecting to repository. This defaults to `github` if not specified
    chartCredentialId = "github"

    //Map to parameters to set when installing the chart
    // chartParameters = [
    //     "spring.profiles": "dev",
    //     "iamge.repository": "registry-docker-registry/blue-dev/bip-reference-person",
    //     "vault.enabled": "false",
    //     "vault.consul.enabled": "false",
    //     "vault.service.configMap": "null",
    //     "consul.config.enabled": "false",
    //     "consul.discovery.enabled": "false",
    //     "consul.discovery.register": "false",
    //     "consul.service.configMap": "null",
    //     "consul.service.secret": "null"
    // ]

    chartValueFile = "testing.yaml"

    //Release name to use
    chartReleaseName = "bip-reference-person"


    //Below config is for local pipeline testing
    skipSonar = true
    skipUndeploy = true
    mvnSettingsFile = 'settings.xml'
}