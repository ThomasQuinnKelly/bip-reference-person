#!/bin/bash

./bin/run.sh &

# Wait for sonar to be up
./provision/wait_for_sonar.sh
./provision/set_main_profile.sh

echo "Done!"
wait
