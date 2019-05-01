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
Edit your bash profile:
```bash
   vi ~/.bash_profile
```

Add exports:
```bash
	export JAVA_HOME="`/usr/libexec/java_home -v '1.8*'`"
	export M2_HOME=/[PATH_TO_YOUR_APACHE_INSTALLS]/apache-maven-3.3.9
	export PATH=$PATH:$M2_HOME/bin:/usr/local/bin/svn
	export MAVEN_OPTS="-Xmx1024m -Dmaven.multiModuleProjectDirectory=$M2_HOME"
	export JAVA_OPTS="-Xmx2048m -Djava.net.preferIPv4Stack=true"
	export SONAR_RUNNER_HOME=/[PATH_TO_YOUR_SONAR_INSTALL]/sonar-runner-2.1
	
	export PATH="$PATH:/Applications/HP_Fortify/HP_Fortify_SCA_and_Apps_4.40/bin:$SONAR_RUNNER_HOME/bin"
	
	export PATH="/Applications/HPE_Security/Fortify_SCA_and_Apps_17.10/bin:$PATH"
```

## Installing Git on a Mac

Open a terminal window.

**Step 1**: Install Homebrew

Homebrew simplifies the installation of software on the Mac OS X operating system.

Copy & paste the following into the terminal window and hit Return.
```bash
		ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
		
		brew doctor
```

You will be offered to install the Command Line Developer Tools from Apple. Confirm by clicking Install. After the installation finished, continue installing Homebrew by hitting Return again.

**Step 2**:  Install Git

Copy & paste the following into the terminal window and hit Return.
```bash
	brew install git
```

You can use Git now.

## How to connect Maven with Nexus using HTTPS

Maven can be set up to use Nexus repository. When the repository runs on https maven isn’t able to connect to it automatically. The solution to this is to add the server’s certificate to the default Java keystore. When connecting to your https-repository fails, Maven will show you an exception like

`[WARNING] Could not transfer metadata gov.va.bip.framework:bip-framework-parentpom:0.0.1-SNAPSHOT/maven-metadata.xml from/to nexus3 (https://nexus.dev.bip.va.gov/repository/maven-public): sun.security.validator
.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target`

To resolve this, download the server’s certificate and add it to the default Java keystore. The easiest way to download the certificate is with the Java provided keytool. The following command is an example to download the certificate to a `.pem` file

```bash
keytool -J-Djava.net.useSystemProxies=true -printcert -rfc --sslserver \nexus.dev.bip.va.gov\:443 > cert.pem
```

The proxy-part is optional. Now you downloaded the certificate, you can add it to the keystore with the following command

```bash
keytool -importcert -file cert.pem -alias nexus.dev.bip.va.gov -storepass changeit -keystore $JAVA_HOME/jre/lib/security/cacerts`
```

Note that $JAVA_HOME is the path to the JDK that is known by Maven
