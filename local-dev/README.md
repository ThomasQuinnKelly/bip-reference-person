# Local Development Environment
This local development environment is strickly for demonstration and local testing of the Openshift Container Platform.

## Starting the Environment
To start the environment first build the OCP reference service by running `mvn clean package`. The platform can then be started by running `docker-compose up -d --build`. 

## Service Links
* [OCP Reference App](http://localhost:8080)
* [Consul](http://localhost:8500) - Master ACL token is `7652ba4c-0f6e-8e75-5724-5e083d72cfe4`
* [Vault](http://localhost:8200) - Root token is `vaultroot`
* [Prometheus](http://localhos:9090)
* [Grafana](http://localhost:3000) - Username/Password is `admin/admin` by default