# Starts the openshift reference spring boot service, including consul, vault and redis

docker-compose -f docker-compose-claims.yml \
	up --build -d
