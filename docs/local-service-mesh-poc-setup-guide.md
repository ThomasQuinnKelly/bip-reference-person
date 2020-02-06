# Local Service Mesh Proof of Concept Setup Guide

This document will provide details and setup instructions for generating and successfully running a local proof of concept environment for the Consul Connect Service Mesh. After completing this guide, you will have two services running locally on a [Minikube](https://kubernetes.io/docs/setup/learning-environment/minikube/) environment with a [Consul Connect Service Mesh](https://www.consul.io/docs/connect/index.html) facilitating the request/response transactions and routing between the services.

## Overview

This POC sets up a bip-reference-person service to field requests, and a bip-archetype-client service to send requests for information to the bip-reference-person service on a regular basis.

The environment is configured with a Consul Agent, with Consul Connect automatic sidecar injection and automatic mTLS encryption enabled. See `service-mesh-helm-consul-values.yaml` for more details.

A description of the logical path:
   * The client service generates a request in the `ArchetypeTestServiceImpl.java` class on a recurring basis (See the `pingTest()` function)
   * That request is routed to the local loopback address (127.0.0.1) on port 1234
   * Consul Connect has been configured to have a Connect Sidecar listen to the loopback address on port 1234, and forward requests from that port to the bip-reference-person service registered in Consul (See `service-mesh-deployments.yaml` for this configuration)
        * `"consul.hashicorp.com/connect-service-upstreams": "bip-reference-person:1234"`
   * Consul Connect routes this traffic through the Connect Sidecars using mTLS encryption automatically (See `service-mesh-helm-consul-values.yaml`)
   * The bip-reference-person service receives the request and responds with mock data
   * The bip-archetype-client service receives the mock data response and logs this to the pod logs (View the pod logs in the K8s Dashboard to see this)

## Required Local Installations

To being, ensure you have downloaded and installed each of the technologies below, and have them available via command line interface (CLI).

* Docker    [https://docs.docker.com/install/]
* Minikube 	[https://kubernetes.io/docs/tasks/tools/install-minikube/]
* Kubectl 	[https://kubernetes.io/docs/tasks/tools/install-kubectl/]
* Helm		[https://helm.sh/docs/intro/install/]

## Provision and Create a Local K8s Environment

Utilizing Minikube, you will create a local K8s cluster on your machine to use for applying the services and mesh.

#### Start Docker

Start your local Docker Daemon for building and orchestrating your images later.

#### Configure Minikube

Execute the following in your CLI:

* Configure CPUs allowed for minikube
    * `$ minikube config set cpus 4`
* Configure memory allowed for minikube
    * `$ minikube config set memory 8192`
* Configure disk space allowed for minikube
    * `$ minikube config set disk-size 50g`
* If you have multiple VM drivers, set VM Driver to use, otherwise skip this step
    * `$ minikube config set vm-driver {YOUR_VM_DRIVER_OF_CHOICE}`
* Enable Ingress in minikube
    * `$ minikube addons enable ingress`
* Start a minikube cluster (This will take some time)
    * `$ minikube start`
* Set your docker commands to point to the docker i inside Minikube
    * `$ eval $(minikube docker-env)`
    
## Pull and Build Service Container Images

Pull the bip-reference-person project:

`$ git clone https://github.ec.va.gov/EPMO/bip-reference-person.git`

Build the image:

`$ cd bip-reference-person`

`$ mvn clean install -U`

`$ docker build bip-reference-person -t bipdev/bip-reference-person:latest`

Pull the bip-archetypetest project:

`$ git clone https://github.ec.va.gov/EPMO/bip-archetypetest.git`

Checkout the service-mesh-poc branch and build the image:

`$ cd bip-archetypetest`

`$ git checkout origin service-mesh-poc`

`$ mvn clean install -U`

`$ docker build bip-archetypetest -t bipdev/bip-archetype-client:latest`

Confirm both images exist in your Docker registry. The below call will list your container images, and they should include `bipdev/bip-reference-person` and `bip-archetype-client`:

`$ docker images`

## Install Consul to your Minikube Cluster

Pull down Consul Demo Helm chart

`$ git clone https://github.com/hashicorp/consul-helm.git`

Install the helm chart with the custom helm-consul-values in bip-archetypetest

`$ helm install -f service-mesh-helm-consul-values.yaml consul-test {PATH_TO_CLONED_CONSUL_DEMO_REPO}`

Confirm Consul pods are created and running:

`$ kubectl get pods --all-namespaces`

## Install Services to your Minikube Cluster

Apply the deployment configuration to your cluster from the bip-archetypetest repository:

`$ kubectl apply -f minikube-deployments.yaml`

# Using the Environment

After following the above installation guide, you should now have a local K8s setup on Minikube, with a bip-reference-person service deployed, and a bip-archetype-client service deployed and making periodic requests to the bip-reference-person service. Both of these services live in the `consul-test` namespace.

## Helpful Commands

Open K8s Dashboard:

`$ minikube dashboard`

Open Consul UI:

`$ minikube service consul-test-consul-ui`

Enter Consul CLI:

`$ kubectl exec -it consul-test-consul-server-0 /bin/sh`

View bip-reference-person SwaggerUI:

`$ kubectl expose deployment bip-reference-person-deploy --type=NodePort -n consul-test`

`$ minikube service bip-reference-person-deploy -n consul-test`

Scale deployment pods:

`$ kubectl scale --replicas=1 deployment/bip-reference-person-deploy -n consul-test`

`$ kubectl scale --replicas=1 deployment/bip-archetype-client-deploy -n consul-test`

Stop Minikube:

`$ minikube stop`

Clean and reset Minikube:

`$ minikube delete`
	
`Navigate to ~/.minikube and delete the folder`