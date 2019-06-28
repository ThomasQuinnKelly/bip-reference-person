# What is this for?
VA SwA (Software Assurance) performs code review on release versions of software.

The `swa-prep.sh` script automates the steps of preparing the package of documentation and code for submission to the SwA, specifically for the [Secure Code Review](https://wiki.mobilehealth.va.gov/display/OISSWA/Frequently+Asked+Questions).

## What does the script do?

The script is intended for use with applications based on the BIP Framework (and for the framework itself).
The script offers

1. Repeatability and consistency for each released artifact

2. Local traceability for the preparation of packages that will be sent to SwA

3. After execution, a folder with:
	* A copy of the VA Secure Code Review Validation Request Form.pdf to be filled out
	* A valid zip of the code from a git tag (a release version)
	* A fresh FPR derived from the tag
	* A zip of the Fortify rulepack used to scan the code

## How do I run it?

1. Projects using the script for the first time:
	* Create a branch of reactor project for [`bip-reference-person`](https://github.com/department-of-veterans-affairs/bip-reference-person)
	* In a browser, go to the GitHub project page to acquire the name of the project (from the end of the repo URL, or end of the git clone URL)
 	* Run the setup script - it will set up a project directory under the `local-dev/swa/projects/` folder for your project
		```bash
		# replace "your-project-name" below with YOUR project name as it appears in the GitHub URL
		$ ./swa-setup.sh your-project-name
		```
	* Set the values in `local-dev/swa/projects/your-project-name/swa-prep.properties` to values that are meaningful for the artifact to be reviewed, and for your local computer
	* Optionally, fill out the request PDF form with a PDF editor, to be used as the default for future review submissions
	* Commit and push your changes to the branch, and create a PR for them - set reviewer to Abhijit Kulkarni (username `abhijitvk`)
2. To create the prep folder:
	* Set the values in `local-dev/swa/projects/your-project-name/swa-prep.properties` to values that are meaningful for the artifact to be reviewed, and for your local computer
	* Run `swa-prep.sh your-project-name` from a bash command line
		```bash
		# replace "your-project-name" below with YOUR project name as it appears in the GitHub URL
		$ ./swa-setup.sh your-project-name
		```
	* The `your-project-name_tag/` prep folder will be created under the location specified in your properties file

3. With the prep folder created, you will need to manually update the PDF that was copied to it. Adjust the PDF with information relevant to the artifact being sent for code review.
