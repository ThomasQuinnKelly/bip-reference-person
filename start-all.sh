# Starts the openshift reference spring boot service, including consul, vault and redis

TMPDIR=/private$TMPDIR docker-compose -f docker-compose.yml \
	up --build -d

sh ./local-dev/localstack/localstackStart.sh
sh ./local-dev/localstack/localstackConfig.sh
