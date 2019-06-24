mavenGitflowPipeline {

    //Sonar Github Credentials - Settings this value will configure the pipeline to use this credential
    //to connect to github during sonar PR scans, adding comments for any violations found
    sonarGithubCredentials = 'dsva-github'

    //Github credential ID to use for releases
    githubCredentials = 'epmo-github'

    dockerBuilds = [
        'bip-reference-person': 'bip-reference-person'
    ]

    deploymentTemplates = ["template.yaml"]
    deploymentParameters = [
        'APP_NAME': 'bip-reference-person',
        'IMAGE': 'bip-reference-person'
    ]

    cucumberOpts = "--tags @DEV"

    //Credential for Nexus Deployment
    nexusCredentialId = 'blue-dev-nexus-login'

    //Below config is for local pipeline testing
    skipSonar = true
    skipMavenDeploy = true
    mvnSettingsFile = 'settings.xml'
}