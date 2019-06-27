mavenGitflowPipeline {

    //Sonar Github Credentials - Settings this value will configure the pipeline to use this credential
    //to connect to github during sonar PR scans, adding comments for any violations found
    sonarGithubCredentials = 'dsva-github'

    //Github credential ID to use for releases
    githubCredentials = 'epmo-github'

    dockerBuilds = [
        'bip-reference-person': 'bip-reference-person'
    ]

    // deploymentTemplates = ["template.yaml"]
    // deploymentParameters = [
    //     'APP_NAME': 'bip-reference-person',
    //     'IMAGE': 'bip-reference-person'
    // ]

    // cucumberOpts = "--tags @DEV"

    //Credential for Nexus Deployment
    //nexusCredentialId = 'blue-dev-nexus-login'

    //Helm Chart information
    chart {
        //Git Repository that contains your Helm chart
        repository = "https://github.com/department-of-veterans-affairs/ocp-external-config"

        //Path to your chart directory within the above repository
        path = "charts/bip-reference-person"

        //Jenkins credential ID to use when connecting to repository. This defaults to `github` if not specified
        credentialId = "github"

        //Map to parameters to set when installing the chart
        parameters = [
            "spring.profiles": "dev"
        ]

        releaseName = "bip-reference-person"
    }

    //Below config is for local pipeline testing
    skipSonar = true
    //skipMavenDeploy = true
    mvnSettingsFile = 'settings.xml'
}