FROM sonarqube:7.6-community

ENV LANGUAGE java
ENV PROFILE_NAME Blue

ADD run-wrapper.sh /opt/sonarqube/run-wrapper.sh

USER root
ADD provision /opt/sonarqube/provision
RUN chmod 755 /opt/sonarqube/provision/*

USER sonarqube

ENTRYPOINT ["/opt/sonarqube/run-wrapper.sh"]
