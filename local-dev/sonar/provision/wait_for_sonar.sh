#!/bin/bash
function isUp {
    curl -s -u admin:admin -f "$BASE_URL/api/system/info"
}

# Wait for server to be up
BASE_URL='http://localhost:9000'
PING=`isUp`
while [ -z "$PING" ]
do
    sleep 5
    PING=`isUp`
done

