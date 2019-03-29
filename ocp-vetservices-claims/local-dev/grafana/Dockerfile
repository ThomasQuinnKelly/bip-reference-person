ARG GRAFANA_VERSION="5.4.0"

FROM grafana/grafana:${GRAFANA_VERSION}

USER grafana

ARG GF_INSTALL_PLUGINS="vonage-status-panel"

RUN if [ ! -z "${GF_INSTALL_PLUGINS}" ]; then \
    OLDIFS=$IFS; \
        IFS=','; \
    for plugin in ${GF_INSTALL_PLUGINS}; do \
        IFS=$OLDIFS; \
        grafana-cli --pluginsDir "$GF_PATHS_PLUGINS" plugins install ${plugin}; \
    done; \
fi

RUN mkdir -p /var/lib/grafana/dashboards
COPY dashboards /var/lib/grafana/dashboards
COPY provisioning /etc/grafana/provisioning