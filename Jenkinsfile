mavenGitflowPipeline {

    //Sonar Github Credentials - Settings this value will configure the pipeline to use this credential
    //to connect to github during sonar PR scans, adding comments for any violations found
    sonarGithubCredentials = 'dsva-github'

    //Credential ID to use when connecting to GitHub repository. Used during the release process to commit POM changes back to GitHub.
    githubCredentials = 'epmo-github'

    // Map of Image Names to sub-directory in the repository. If this is value is non-empty, the build pipepline will build
    // all images specified in the map. The config below will build an image tagged as 
    // blue-dev/bip-reference-person:latest using the Docker context of ./bip-reference-person.
    dockerBuilds = [
        'blue-dev/bip-reference-person': 'bip-reference-person'
    ]

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
    //     "image.repository": "registry-docker-registry/blue-dev/bip-reference-person",
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
}