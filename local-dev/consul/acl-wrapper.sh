#!/bin/sh
set -e

# Wrapper script for the offical consul docker container. Configures ACLs based on provided environment variables.
# THIS IS FOR LOCAL DEVELOPMENT ONLY AS SUPPLYING ACL TOKEN VIA ENVIRONMENT VARIABLES IS NOT SECURE

if [ -n "$MASTER_ACL_TOKEN" ]; then
    if [ -z "$AGENT_ACL_TOKEN" ]; then
        AGENT_ACL_TOKEN="$MASTER_ACL_TOKEN"
    fi

    cat > /consul/config/acl.json <<EOF
{
    "acl": {
        "enabled": true,
        "default_policy": "deny",
        "down_policy": "extend-cache",
        "tokens": {
            "master": "$MASTER_ACL_TOKEN",
            "agent": "$AGENT_ACL_TOKEN"
        }
    }
}
EOF
fi

# Proceed with Consul entrypoint script
docker-entrypoint.sh "$@"