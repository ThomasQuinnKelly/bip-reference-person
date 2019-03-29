# Stops the entire platform, including all log aggregation services

docker-compose -f docker-compose-claims.yml \
	down -v
