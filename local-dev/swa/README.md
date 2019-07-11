# What is this for?
VA OIS Software Assurance (SwA) performs reviews for design and code reviews, and quality reviews. For code reviews, SwA is interested in reviewing release versions of the software.

This README and related `swa-prep.sh` script are specific to [Secure Code Review > How do I register applications, request secure code review tools and validations?](https://wiki.mobilehealth.va.gov/display/OISSWA/Frequently+Asked+Questions).

Some targeted URLs to instructions for the SwA submission requirements and process:
* Document Library (PDFs, etc): https://wiki.mobilehealth.va.gov/display/OISSWA/Public+Document+Library
* Application Registration: https://wiki.mobilehealth.va.gov/display/OISSWA/How+to+open+an+NSD+ticket+to+register+a+VA+application
* Code Review Submission: https://wiki.mobilehealth.va.gov/pages/viewpage.action?pageId=26774489
* FAQ:  https://wiki.mobilehealth.va.gov/display/OISSWA/Frequently+Asked+Questions

## What does the script do?

The script is intended for use with applications based on the BIP Framework (and for the framework itself). It prompts the user to perform any manual steps, and automates those steps that can be automated.

The script offers:
1. Repeatability and consistency for each released artifact
2. Local traceability for the preparation of packages that will be sent to SwA

#### swa-prep.sh & swa-prep.properties

This script creates a folder with:
	* A copy of the `VA Secure Code Review Validation Request Form.pdf` to be filled out
	* A zip of the code taken from a git tag (a release version)
	* A fresh Fortify FPR derived from the tag
	* A zip of the local Fortify rulepack that used to scan the code

## When and how do I run the script?

SwA submissions are typically done only on release candidates. A release version will have a git TAG version that does not have "-SNAPSHOT" on the end of it.

To make the preparation process go faster and easier, it is worth taking the time to update default files for your project.
* Open `[project-reactor]/local-dev/swa/defaults/VA Secure Code Review Validation Request Form.pdf` in a PDF editor (Acrobat, Preview, etc). Fill out the form and save it in place.
* Open `[project-reactor]/local-dev/swa/defaults/swa-prep.properties` in an editor. Update property values to sensible defaults if desired, and save in place.

Steps:
1. [Register with SwA](https://wiki.mobilehealth.va.gov/display/OISSWA/How+to+open+an+NSD+ticket+to+register+a+VA+application) if you have not already registered the application.
2. Create a release on the Jenkins server.
3. Open a bash terminal
	* Change to the local-dev/swa directory in your project
		```bash
		$ cd ~/git/my-project/local-dev/swa
		```
	* Run the script and follow prompts (you may need to set the script to be executable: `chmod +x swa-prep.sh`)
		```bash
		$ ./swa-prep.sh
		```
	* Follow the prompts, and supply information or perform actions as directed. Note the messages output when the script finishes.
4. Use the created submission files to [submit the code review request](https://wiki.mobilehealth.va.gov/pages/viewpage.action?pageId=26774489) to SwA

## Maintenance

The "VA Secure Code Review Validation Request Form.pdf" may change from time to time. You can check for recent versions at the Swa [Public Document Library](https://wiki.mobilehealth.va.gov/display/OISSWA/Public+Document+Library). Download it, and copy it into `[project-reactor]/local-dev/swa/defaults/`.  If the file name changes, you can either rename the new file to `VA Secure Code Review Validation Request Form.pdf`, or change the value of the `pdfFileName` variable near the top of `swa-prep.sh`.
