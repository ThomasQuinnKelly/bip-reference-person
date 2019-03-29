mavenGitflowPipeline {
    dockerBuilds = [
        'ocp-reference-person': 'ocp-reference-person'
    ]

    deploymentTemplates = ["template.yaml"]
    deploymentParameters = [
        'APP_NAME': 'ocp-reference-person',
        'IMAGE': 'docker-registry.default.svc:5000/blue-dev/ocp-reference-person',
        'IMAGE_TAG': 'master'
    ]
}