

###############################################################################
#   VARIABLES
###############################################################################

########## useful variables
cwd=`pwd`
thisScript="$0"
thisFileName=$(basename -- "$0" | cut -d'.' -f1)
args="$@"
returnStatus=0

########## script variables derived from swa-prep.properties

### Output directory for prep files
outputDir="~/Documents/Projects/bah/SwA_code_review/"
### clone URL for project to be submitted
cloneUrl="https://github.com/department-of-veterans-affairs/bip-framework.git"
### extract project name from clone URL
projectName=$cloneUrl | cut -d ### TODO

################################################################################
#########################                              #########################
#########################   SCRIPT UTILITY FUNCTIONS   #########################
#########################                              #########################
################################################################################

## function to exit the script immediately ##
## arg1 (optional): exit code to use        ##
## scope: private (internal calls only)    ##
function exit_now() {
	#  1 = error from a bash command
	#  5 = invalid command line argument
	#  6 = property not allocated a value
	# 10 = project directory already exists

	exit_code=$1
	if [ -z $exit_code ]; then
		exit_code="0"
	elif [ "$exit_code" -eq "0" ]; then
		echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" 2>&1 | tee -a "$genLog"
		echo " PREP ACTIVITIES COMPLETE" 2>&1 | tee -a "$genLog"
		echo "" 2>&1 | tee -a "$genLog"
		echo " ##################################################################################" 2>&1 | tee -a "$genLog"
		echo " ## Add completion steps here" 2>&1 | tee -a "$genLog"
		echo " ## 1. " 2>&1 | tee -a "$genLog"
		echo " ## 2. " 2>&1 | tee -a "$genLog"
		echo " ## 3. " 2>&1 | tee -a "$genLog"
		echo " ##################################################################################" 2>&1 | tee -a "$genLog"
		echo "" 2>&1 | tee -a "$genLog"
	else
		echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" 2>&1 | tee -a "$genLog"
		echo " ***   BUILD FAILED (exit code $exit_code)   ***" 2>&1 | tee -a "$genLog"
		echo "" 2>&1 | tee -a "$genLog"
		# check exit codes
		if [ "$exit_code" -eq "1" ]; then
			echo "Command error. See output at end of $genLog"
		elif [ "$exit_code" -eq "5" ]; then
			# Invalie command line argument
			echo " ERROR: Invalid command-line argument \"-$OPTARG\" (use \"$thisScript -h\" for help) ... aborting immediately" 2>&1 | tee -a "$genLog"
		elif [ "$exit_code" -eq "6" ]; then
			# One or more properties not set
			echo " ERROR: \"$propertiesFile\" does not provide values for the following properties:" 2>&1 | tee -a "$genLog"
			echo "        $missingProperties" 2>&1 | tee -a "$genLog"
		else
			# some unexpected error
			echo " Unexpected error code: $exit_code ... aborting immediately" 2>&1 | tee -a "$genLog"
		fi
	fi
	echo "" 2>&1 | tee -a "$genLog"
	echo " Help: \"$thisScript -h\"" 2>&1 | tee -a "$genLog"
	echo " Logs: \"$genLog\"" 2>&1 | tee -a "$genLog"
	echo "       search: \"+>> \" (script); \"sed: \" (sed); \"FAIL\" (mvn & cmd)" 2>&1 | tee -a "$genLog"
	echo "------------------------------------------------------------------------" 2>&1 | tee -a "$genLog"
	# exit
	exit $exit_code
}


## function to display help             ##
## scope: private (internal calls only) ##
function show_help() {
	echo "" 2>&1 | tee -a "$genLog"
	echo "$thisScript : Generate a new skeleton project from the origin project." 2>&1 | tee -a "$genLog"
	echo "  To generate your new project skeleton:" 2>&1 | tee -a "$genLog"
	echo "  1. Update gen.properties with values for your new project." 2>&1 | tee -a "$genLog"
	echo "  2. Run ./gen.sh (with relevant options) to create the new project." 2>&1 | tee -a "$genLog"
	echo "  3. Move the project folder to your git directory and git initialize it." 2>&1 | tee -a "$genLog"
	echo "Examples:" 2>&1 | tee -a "$genLog"
	echo "  $thisScript -h  show this help" 2>&1 | tee -a "$genLog"
	echo "  $thisScript     generate project using $thisFileName.properties file" 2>&1 | tee -a "$genLog"
	echo "  $thisScript -s  skip (re)building the Origin source project" 2>&1 | tee -a "$genLog"
	echo "  $thisScript -o  over-write new project if it already exists" 2>&1 | tee -a "$genLog"
	echo "  $thisScript -d  build docker image (docker must be running)" 2>&1 | tee -a "$genLog"
	echo "  $thisScript -so both skip build, and overwrite" 2>&1 | tee -a "$genLog"
	echo "" 2>&1 | tee -a "$genLog"
	echo "Notes:" 2>&1 | tee -a "$genLog"
	echo "* Full instructions available in development branch at:" 2>&1 | tee -a "$genLog"
	echo "  https://github.com/department-of-veterans-affairs/bip-archetype-service/" 2>&1 | tee -a "$genLog"
	echo "* A valid \"$thisFileName.properties\" file must exist in the same directory" 2>&1 | tee -a "$genLog"
	echo "  as this script." 2>&1 | tee -a "$genLog"
	echo "* It is recommended that a git credential helper be utilized to" 2>&1 | tee -a "$genLog"
	echo "  eliminate authentication requests while executing. For more info see" 2>&1 | tee -a "$genLog"
	echo "  https://help.github.com/articles/caching-your-github-password-in-git/" 2>&1 | tee -a "$genLog"
	echo "" 2>&1 | tee -a "$genLog"
	echo "" 2>&1 | tee -a "$genLog"
	# if we are showing this, force exit
	exit_now
}

## get argument options off of the command line        ##
## required parameter: array of command-line arguments ##
## scope: private (internal calls only)                ##
function get_args() {
	echo "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++" 2>&1 | tee -a "$genLog"
	echo "+>> Processing command-line arguments" 2>&1 | tee -a "$genLog"

	# echo "args: \"$@\""
	#if [ "$@" -eq "" ]; then
	if [[ "$@" == "" ]]; then
		echo "+>> Using properties file \"$propertiesFile\"" 2>&1 | tee -a "$genLog"
	fi
	while getopts ":h" opt; do
		case "$opt" in
			h)
				show_help
				;;
				# TODO process arg for project name below
			\?)
				exit_now 5
				;;
		esac
		previous_opt="$opt"
	done
	# shift $((OPTIND -1))
}


################################################################################
########################                                ########################
########################   BUSINESS UTILITY FUNCTIONS   ########################
########################                                ########################
################################################################################

# TODO

################################################################################
############################                        ############################
############################   BUSINESS FUNCTIONS   ############################
############################                        ############################
################################################################################

# TODO

### create working folder if necessary
### git clone the project
### build install the project
### run fortify (steal from fortify-sca.sh)
### clean up the project (remove target, etc)
### assemble the prep folder
### print next steps, and note for location of fresh submission forms ??? can we curl the most recent version ???
# Look for most recent version of "VA Secure Code Review Validation Request Form.pdf"
# at https://wiki.mobilehealth.va.gov/display/OISSWA/Public+Document+Library
