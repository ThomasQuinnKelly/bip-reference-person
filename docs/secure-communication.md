# Secure Service Communications
All communication with our services or with external services will be secured end-to-end with MTLS. To this end, each service will need to be provided with its own public and private certificate pair. Applications will also need to be provided a trusted keystore of certificates that they should trust for secure communication.

# Certificate Generation

# Server Certificate
Each service will need to present a SSL certificate to all callers. The certificates needs to match the DNS name which the client is using to address the service. For internal services this would be `<service-name>.service.consul` and for externally available services, the common name would be something like `<service-name>.dev.bip.va.gov`

### Internal Services
Certificate common name needs to match `*.service.consul` as must service endpoints will be resolved via Consul.

_TODO - Decide on internal CA solution_

### External Facing Services
Certificate common name needs to match `<service-name>.dev.bip.va.gov`. These certificates need to valid for all external callers and therefore need to be signed by one of the VA certificate authorities. These will need to be manually requested.

# Client Certificates
Each service will need a client SSL certificate signed by the Internal Service CA in order to call other internal services. Any other client certificates needed to call external services would need to be provided by that services CA.

# Delivery of certificates to the application container

Server and Client SSL private/public keypairs in PKCS12 format will be provided to the container as a Kubernetes secret. The secret should include the PKCS12 file and the password for that keypair, the trusted keystore file and its associated password.

## Server SSL Secret
```yaml
apiVersion: v1
data:
  server.keystore: <secret value>
  server.keystore-password: <secret value>
  server.truststore: <secret value>
  server.truststore-password: <secret value>
kind: Secret
metadata:
  creationTimestamp: 2019-04-01T13:55:59Z
  name: ocp-reference-person-server-ssl
  namespace: default
type: Opaque
```

## Client SSL Secret
```yaml
apiVersion: v1
data:
  client.keystore: <secret value>
  client.keystore-password: <secret value>
  client.truststore: <secret value>
  client.truststore-password: <secret value>
kind: Secret
metadata:
  creationTimestamp: 2019-04-01T13:55:59Z
  name: ocp-reference-person-client-ssl
  namespace: default
type: Opaque
```

## Configuration Mapping to Pod
Inside your application deployment template, map the secret data to environment variables and volumes in your container.

```yaml
spec:
    containers:
        env:
        - name: server.ssl.trust-store
          value: /secrets/server-ssl/serverTruststore
        - name: server.ssl.key-store
          value: /secrets/server-ssl/serverKeystore
        - name: server.ssl.trust-store-password
          valueFrom:
            secretKeyRef:
                name: ocp-reference-person-server-ssl
                key: server.truststore-password
        - name: server.ssl.key-store-password
          valueFrom:
            secretKeyRef:
                name: ocp-reference-person-server-ssl
                key: server.keystore-password
        volumeMounts:
        - name: server-ssl
          mountPath: "/secrets/server-ssl" 
          readOnly: true
    volumes:
    - name: server-ssl
      secret:
        secretName: ocp-reference-person-server-ssl
        items:
        - key: server.keystore
          path: serverKeystore
        - key: server.truststore
          path: serverTruststore
    - name: client-ssl
      secret:
        secretName: ocp-reference-person-client-ssl
        items:
        - key: client.keystore
          path: clientKeystore
        - key: client.truststore
          path: clientTruststore
       
```

-Djavax.net.ssl.keyStore=$CLIENT_KEYSTORE 
-Djavax.net.ssl.keyStorePassword=$CLIENT_KEYSTORE_PASS 
-Djavax.net.ssl.trustStore=$CLIENT_TRUSTSTORE 
-Djavax.net.ssl.trustStorePassword=$CLIENT_TRUSTSTORE_PASS