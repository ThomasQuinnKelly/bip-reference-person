# Build and Git Properties 

## Capability and Features
- Access Build Properties in code via Spring Boot
- Access Git properties in code via Spring Boot

## Build Properties Configuration
- If you have included `bip-framework-parentpom` as the parent dependency, you must already have plugin spring-boot-maven-plugin with the following configuration.

```xml
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
```

 - Above command instructs the plugin to execute also `build-info` goal, which isn't run by default. This generates build meta-data for the application, which includes artifact version, build time and additional information.
 
## Accessing Build Properties
- After building your application, you can access information about your application's build through BuildProperties object. Let the Spring inject it for you:

```java
	    @Autowired
	    BuildProperties buildProperties;
```

   Now you can access various information from this object.

```java
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
```

## Git Properties Configuration
- Actuator detects git.properties file, which contains useful information about your git repository. If you have included `bip-framework-parentpom` as the parent dependency, you have access to the plugin with following configuration .

   Maven pom.xml:

```xml
		<plugin>
		    <groupId>pl.project13.maven</groupId>
	            <artifactId>git-commit-id-plugin</artifactId>
		    <configuration>
		       <verbose>false</verbose>
			<excludeProperties>
			   <excludeProperty>git.build.user.*</excludeProperty>
			   <excludeProperty>git.commit.user.*</excludeProperty>
			</excludeProperties>
			<failOnNoGitDirectory>false</failOnNoGitDirectory>
		    </configuration>
		</plugin>
```

## Accessing Git Properties	
- After rebuilding and restarting the /info endpoint displays Git via `GitInfoContributor` class info as shown below

```json
		{
		  "git": {
		    "commit": {
		      "time": "2018-09-01T19:46:31Z",
		      "id": "493f071"
		    },
		    "branch": "master"
		  }
		}
```
