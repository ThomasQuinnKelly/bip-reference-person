# Build and Git Properties 

## Capability and Features
- Access Build Properties in code via Spring Boot
- Access Git properties in code via Spring Boot

## Build Properties Configuration
- If you are using Spring Boot, your pom.xml should already contain spring-boot-maven-plugin. You just need to add the following          configuration.

	<plugin>
    		<groupId>org.springframework.boot</groupId>
    		<artifactId>spring-boot-maven-plugin</artifactId>
    		<executions>        
    			<execution>            
    				<id>build-info</id>            
    				<goals>                
    					<goal>build-info</goal>            
    				</goals>        
    			</execution>    
    		</executions>
    	 </plugin>

 - Above command instructs the plugin to execute also build-info goal, which is not run by default. This generates build meta-data about your application, which includes artifact version, build time and more.
 
## Accessing Build Properties
- After configuring your spring-boot-maven-plugin and building your application, you can access information about your application's build through BuildProperties object. Let the Spring inject it for you:

	@Autowired
	BuildProperties buildProperties;
	
Now you can access various information from this object.

	// Artifact's name from the pom.xml file
	buildProperties.getName();
	// Artifact version
	buildProperties.getVersion();
	// Date and Time of the build
	buildProperties.getTime();
	// Artifact ID from the pom file
	buildProperties.getArtifact();
	// Group ID from the pom file
	buildProperties.getGroup();
	
## Git Properties Configuration
- Actuator automatically detects git.properties file, which contains useful information about your git repository. To generate it, you'll need to add a specific plugin to your build config.

In Maven pom.xml:

        	<plugin>
            	<groupId>pl.project13.maven</groupId>
            	<artifactId>git-commit-id-plugin</artifactId>
       	 </plugin>

## Accessing Git Properties	
- After rebuilding and restarting the /info endpoint displays some git info.

{
  "git": {
    "commit": {
      "time": "2018-09-01T19:46:31Z",
      "id": "493f071"
    },
    "branch": "master"
  }
}