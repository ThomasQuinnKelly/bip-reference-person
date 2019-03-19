FROM consul:1.4.3

COPY acl-wrapper.sh /usr/local/bin/acl-wrapper.sh
ENTRYPOINT ["acl-wrapper.sh"]
CMD ["agent", "-dev", "-client", "0.0.0.0"]