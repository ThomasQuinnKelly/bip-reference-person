#! /bin/sh

#echo "Starting Localstack..."
#
#DEBUG=1 localstack start --host >> localstack.log 2>&1 >> localstack.log &
#
#secs=600                           # 10 minutes
#start=$(date +'%s')
#endTime=$(( $(date +%s) + secs ))  # Calculate end time.
#
#while [ $(date +%s) -lt $endTime ]; do  # Loop until interval has elapsed.
#  sleep 1
#  echo $(tail -1 "localstack.log")
#  if grep -q "Ready." "localstack.log";then
#    echo "Localstack reports it is ready."
#    break
#  fi
#  sleep 8
#  echo "Starting Localstack has taken $(($(date +'%s') - $start)) seconds so far"
#done

echo "Waiting for all LocalStack services to be ready..."

rm ./local-dev/localstack/localstack.log 2> /dev/null

secs=120                           # 2 minutes
start=$(date +'%s')
endTime=$(( $(date +%s) + secs ))  # Calculate end time.

while [ $(date +%s) -lt $endTime ]; do  # Loop until interval has elapsed.
  sleep 1
  docker logs bip-reference-person_localstack_1 >> ./local-dev/localstack/localstack.log 2>&1 >> ./local-dev/localstack/localstack.log &
  #echo $(tail -1 "./local-dev/localstack/localstack.log")
  if grep -q "Ready." "./local-dev/localstack/localstack.log";then
    echo "Localstack reports it is ready."
    break
  fi
  sleep 2
  echo "Starting Localstack has taken $(($(date +'%s') - $start)) seconds so far"
done