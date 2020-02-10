#! /bin/sh

echo "Configuring Localstack..."

#aws configure set region us-east-1
#aws configure set aws_access_key_id test_key
#aws configure set aws_secret_access_key test_secret

echo "###Creating Topic"
aws --endpoint-url=http://localhost:4575 sns create-topic --name test_my_topic

echo "###Creating Queue"
aws --endpoint-url=http://localhost:4576 sqs create-queue --queue-name sub_new_queue

echo "###Creating Dead Letter Queue"
aws --endpoint-url=http://localhost:4576 sqs create-queue --queue-name sub_new_queue_dead

echo "###Creating Subscription"
aws --endpoint-url=http://localhost:4575 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:test_my_topic --protocol sqs --notification-endpoint arn:aws:sns:us-east-1:000000000000:sub_new_queue

# Retrieve the logs for debugging if neccesary
rm ./local-dev/localstack/localstack.log 2> /dev/null
docker logs bip-reference-person_localstack_1 >> ./local-dev/localstack/localstack.log 2>&1 >> ./local-dev/localstack/localstack.log &
