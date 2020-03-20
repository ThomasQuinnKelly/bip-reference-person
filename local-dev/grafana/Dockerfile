ARG GRAFANA_VERSION="5.4.0"

FROM grafana/grafana:${GRAFANA_VERSION}

USER grafana

COPY ./resources/vonage-status-panel.tar.gz vonage-status-panel.tar.gz
RUN tar -xvf vonage-status-panel.tar.gz -C /var/lib/grafana/plugins

RUN mkdir -p /var/lib/grafana/dashboards
COPY dashboards /var/lib/grafana/dashboards
COPY provisioning /etc/grafana/provisioning