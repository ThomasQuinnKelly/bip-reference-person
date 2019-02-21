FROM java:8

# Add a volume pointing to /tmp
VOLUME /tmp

# The application's jar file
ARG JAR_FILE=target/os-reference-service-1.jar

# Add the application's jar to the container
ADD ${JAR_FILE} os-reference-service-1.jar

# Run the jar file 
ENTRYPOINT ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000","-Djava.security.egd=file:/dev/./urandom","-jar","/os-reference-service-1.jar"]
