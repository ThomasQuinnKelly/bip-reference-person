# Starts the openshift reference spring boot service, including consul, vault and redis

TMPDIR=/private$TMPDIR docker-compose -f docker-compose.yml \
	up --build -d
