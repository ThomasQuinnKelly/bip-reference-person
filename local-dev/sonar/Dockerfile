FROM sonarqube:6.7

ENV LANGUAGE java
ENV PROFILE_NAME Blue

ADD run-wrapper.sh /opt/sonarqube/run-wrapper.sh

ADD provision /opt/sonarqube/provision
RUN chmod 755 /opt/sonarqube/provision/*

ENTRYPOINT ["/opt/sonarqube/run-wrapper.sh"]
