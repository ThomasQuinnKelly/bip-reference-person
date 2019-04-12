# Installation Help Guide

## Install JDK 8 on a Mac

To install the JDK package, you only have to install the binary files provided by Oracle.

* Download JDK 8 from [Oracle WebSite](https://www.oracle.com/technetwork/java/javase/downloads/index.html): [jdk-8u201-macosx-x64.dmg for Mac or newer] - you will probably have to search for "jdk-8u".
* Double click on jdk-8u201-macosx-x64.dmg and follow the screen instructions.
* JDK package will have been installed in /Library/Java/JavaVirtualMachines. You may have multiple versions of JDK package. To make sure it picks 1.8 version, update bash_profile as shown below

	vi ~/.bash_profile 
	export JAVA_HOME="`/usr/libexec/java_home -v '1.8*'`"
	export JAVA_OPTS="-Xmx2048m -Djava.net.preferIPv4Stack=true"

## Sample bash_profile on a Mac
   vi ~/.bash_profile
   
	export JAVA_HOME="`/usr/libexec/java_home -v '1.8*'`"
	export M2_HOME=/[PATH_TO_YOUR_APACHE_INSTALLS]/apache-maven-3.3.9
	export PATH=$PATH:$M2_HOME/bin:/usr/local/bin/svn
	export MAVEN_OPTS="-Xmx1024m -Dmaven.multiModuleProjectDirectory=$M2_HOME"
	export JAVA_OPTS="-Xmx2048m -Djava.net.preferIPv4Stack=true"
	export SONAR_RUNNER_HOME=/[PATH_TO_YOUR_SONAR_INSTALL]/sonar-runner-2.1
	
	export PATH="$PATH:/Applications/HP_Fortify/HP_Fortify_SCA_and_Apps_4.40/bin:$SONAR_RUNNER_HOME/bin"
	
	export PATH="/Applications/HPE_Security/Fortify_SCA_and_Apps_17.10/bin:$PATH"

## Installing Git on a Mac

Open a terminal window.

**Step 1**: Install Homebrew

Homebrew simplifies the installation of software on the Mac OS X operating system.

Copy & paste the following into the terminal window and hit Return.
		
		ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
		
		brew doctor

You will be offered to install the Command Line Developer Tools from Apple. Confirm by clicking Install. After the installation finished, continue installing Homebrew by hitting Return again.

**Step 2**:  Install Git

	Copy & paste the following into the terminal window and hit Return.
	
	brew install git
	
You can use Git now.
