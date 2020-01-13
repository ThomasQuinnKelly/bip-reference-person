# Secure Service Communications
All communication with our services or with external services will be secured end-to-end with mTLS. To this end, each service will need to be provided with its own public and private certificate pair. Applications will also need to be provided a trusted keystore of certificates that they should trust for secure communication.

# Certificate Generation

## Server Certificate
Each service will need to present a SSL certificate to all callers. The certificates needs to match the DNS name which the client is using to address the service. For internal services this would be `<service-name>.service.consul` and for externally available services, the common name would be something like `<service-name>.dev.bip.va.gov`

### Internal Services
Certificate common name needs to match `*.service.consul` as must service endpoints will be resolved via Consul.

_TODO - Decide on internal CA solution_

### External Facing Services
Certificate common name needs to match `<service-name>.dev.bip.va.gov`. These certificates need to valid for all external callers and therefore need to be signed by one of the VA certificate authorities. These will need to be manually requested.

## Client Certificates
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
  name: bip-reference-person-server-ssl
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
  name: bip-reference-person-client-ssl
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
          value: /secrets/server-ssl/truststore
        - name: server.ssl.key-store
          value: /secrets/server-ssl/keystore
        - name: server.ssl.trust-store-password
          valueFrom:
            secretKeyRef:
                name: bip-reference-person-server-ssl
                key: server.truststore-password
        - name: server.ssl.key-store-password
          valueFrom:
            secretKeyRef:
                name: bip-reference-person-server-ssl
                key: server.keystore-password
        
        - name: javax.net.ssl.trustStore
          value: /secrets/client-ssl/truststore
        - name: javax.net.ssl.keyStore
          value: /secrets/client-ssl/keystore
        - name: javax.net.ssl.trustStorePassword
          valueFrom:
            secretKeyRef:
                name: bip-reference-person-client-ssl
                key: client.truststore-password
        - name: javax.net.ssl.keyStorePassword
          valueFrom:
            secretKeyRef:
                name: bip-reference-person-client-ssl
                key: client.keystore-password
        volumeMounts:
        - name: server-ssl
          mountPath: "/secrets/server-ssl" 
          readOnly: true
        - name: client-ssl
          mountPath: "/secrets/client-ssl" 
          readOnly: true
          
    volumes:
    - name: server-ssl
      secret:
        secretName: bip-reference-person-server-ssl
        items:
        - key: server.keystore
          path: keystore
        - key: server.truststore
          path: truststore
    - name: client-ssl
      secret:
        secretName: bip-reference-person-client-ssl
        items:
        - key: client.keystore
          path: keystore
        - key: client.truststore
          path: truststore
```

# Service Mesh Solution

Currently, a Service Mesh implementation for handling mTLS encryption between services and ingress/egress points is being considered to handle BIP framework communications. When a decision is made on the future of this path forward, this document will be updated accordingly. See [Service Mesh Solutions Research](service-mesh-research.md) for more details.