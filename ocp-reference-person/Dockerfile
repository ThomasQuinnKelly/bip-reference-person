FROM java:8

# Add a volume pointing to /tmp
VOLUME /tmp

# The application's jar file
ARG JAR_FILE=target/ocp-reference-person.jar

# Add the application's jar to the container
ADD ${JAR_FILE} ocp-reference-person.jar

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ocp-reference-person.jar"]
