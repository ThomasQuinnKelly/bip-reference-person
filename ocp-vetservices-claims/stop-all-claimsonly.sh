# Stops the entire platform, including all log aggregation services

docker-compose -f docker-compose-claimsonly.yml \
	down -v
