FROM consul:1.4.2

COPY acl.json /consul/config/acl.json

RUN /bin/sed -i 's/^\(.*set -- su-exec consul:consul "$@"\)/ if [ "$(id -u)" = "0" ]; then\n \1\n fi/' /usr/local/bin/docker-entrypoint.sh