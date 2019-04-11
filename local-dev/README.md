# Local Development Environment
This local development environment is strickly for demonstration and local testing of the Openshift Container Platform.

## Starting the Environment
To start the environment first build the BIP reference service by running `mvn clean package`. The platform can then be started by running `docker-compose up -d --build`. 

## Service Links
* [BIP Reference App](http://localhost:8080)
* [Consul](http://localhost:8500) - Master ACL token is `7652ba4c-0f6e-8e75-5724-5e083d72cfe4`
* [Vault](http://localhost:8200) - Root token is `vaultroot`
* [Prometheus](http://localhos:9090)
* [Grafana](http://localhost:3000) - Username/Password is `admin/admin` by default

## Redis Cache:

## 1. Default Profile:

If you run the app in default profile, it uses emebedded redis server for caching. To clear cache, follow the below steps.

* Download Redis from https://redis.io/download
* Go to src folder and run redis-cli
* To see the cache entries - KEYS *
* To clear cache entries - FLUSHALL

## 1. local-int profile:

If you run the app in local-int profile, follow the below steps.

* Find the container id of the redis using "docker ps -a"*
* Log into the container "docker exec -i -t <container-id> sh"
* Enter redis-cli
* To see the cache entries - KEYS *
* To clear cache entries - FLUSHALL
