#!/bin/sh

## turn on to assist debugging ##
# export PS4='[$LINENO] '
# set -x
##

args="$@"
previous_opt=""

maven_phases="initialize"
maven_args="-Pfortify-sca"

## get argument options off of the command line        ##
## required parameter: array of command-line arguments ##
## scope: private (internal calls only)                ##
function get_args() {
	# echo "+>>    args: \"$@\""
	while getopts ":hb" opt; do
		# echo "+>>    opt=$opt OPTARG=$OPTARG"
		previous_opt="$opt"
		case "$opt" in
			h )
				echo ""
				echo "Usage: $thisScript [-h|-b]"
				echo "Runs fortify on the project, and merges into the root FPR."
				echo "Options:"
				echo "  -h   show this help."
				echo "  []   (no arg) do not build the project before running Fortify."
				echo "  -b   build the project before running Fortify."
				echo ""
				exit 0
				;;
			b )
				maven_phases="clean install"
				maven_args="$maven_args  -Dfortify.bind.phase=package"
				;;
			\? )
				echo "+>> ERROR: unknown argument \"$opt\""
				exit 1
				;;
		esac
	done
	# shift $((OPTIND -1))
	# echo "+>>    OPTIND=$OPTIND"
}

echo ""
echo "========================================================================="
echo "Run Fortify"
echo ""

get_args $args

# echo "maven_phases=$maven_phases"
# echo "maven_args=$maven_args"
# echo "previous_opt=$previous_opt"
if [ "$previous_opt" == "" ]; then
	echo "* Project will NOT be built before running Fortify"
	echo "  Run './fortify -h' to see options."
	read -p "  Press Enter to continue, Ctrl+C to abort: "
else
	echo "* Project will be built before running Fortify"
	echo "  Run './fortify -h' to see options."
	read -p "  Press Enter to continue, Ctrl+C to abort: "
fi

echo "+>> mvn $maven_phases $maven_args"
mvn $maven_phases $maven_args
if [ "$?" -ne "0" ]; then echo "Error: processing error."; exit 2; fi;

echo "+>> mvn antrun:run@fortify-merge -Pfortify-merge"
mvn antrun:run@fortify-merge -Pfortify-merge
if [ "$?" -ne "0" ]; then echo "Error: processing error."; exit 2; fi;

echo "==== Done ===="
echo ""
