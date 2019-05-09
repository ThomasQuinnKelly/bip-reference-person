mavenGitflowPipeline {

    //Sonar Github Credentials - Settings this value will configure the pipeline to use this credential
    //to connect to github during sonar PR scans, adding comments for any violations found
    sonarGithubCredentials = 'dsva-github'

    dockerBuilds = [
        'bip-reference-person': 'bip-reference-person'
    ]

    deploymentTemplates = ["template.yaml"]
    deploymentParameters = [
        'APP_NAME': 'bip-reference-person',
        'IMAGE': 'bip-reference-person',
        'IMAGE_TAG': 'master'
    ]

    cucumberOpts = "--tags @DEV"
}