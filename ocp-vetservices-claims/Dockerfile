FROM java:8

# Add a volume pointing to /tmp
VOLUME /tmp

# The application's jar file
ARG JAR_FILE=target/ocp-vetservices-claims.jar

# Add the application's jar to the container
ADD ${JAR_FILE} ocp-vetservices-claims.jar

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ocp-vetservices-claims.jar"]

ADD ./local-dev/wait-for-it.sh /wait-for-it.sh

