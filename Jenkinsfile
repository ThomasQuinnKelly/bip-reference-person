mavenGitflowPipeline {
    dockerBuilds = [
        'ocp-reference-person': 'ocp-reference-person',
        'consul': 'local-dev/consul',
        'vault-config': 'local-dev/vault-config'
    ]

    deploymentTemplates = ["template.yaml"]
    deploymentParameters = [
        'APP_NAME': 'ocp-reference-person'
        'IMAGE': '172.30.1.1:5000/blue-dev/ocp-reference-person'
        'IMAGE_TAG': 'master'
    ]
}