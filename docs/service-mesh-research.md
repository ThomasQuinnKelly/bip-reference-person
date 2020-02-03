# Service Mesh Solutions Research

The BIP Framework can benefit from the addition of a Service Mesh paradigm for managing service-to-service communications, service discovery, service access control, and making a number of other additional features available for BIP platform participants (Increased Metrics, Tracing, and Logging, Circuit Breaking, Chaos Testing, Rate Limiting, etc).

The Service Mesh solutions defined below work on an _**[Envoy](https://www.envoyproxy.io/learn/service-mesh) Sidecar**_ approach, leveraging a proxy sidecar for each managed service which handles all incoming and outgoing communications from the service. Allowing a set of services to communicate using these proxies will allow the sidecar proxies and their associated Control Plane to manage many of the details automatically, such as mutual TLS authentication (mTLS) and service discovery.

![Kubernetes Sidecar Example](https://wecode.wepay.com/assets/2018-06-11-using-l5d-as-a-service-mesh-proxy-at-wepay/image_0.png "Kubernetes Sidecar Example")

Available Service Mesh products vary in terms of features, complexity, and performance. This document will present research on some of the viable options for implementation in the BIP architecture.

## [Istio](https://istio.io/)

Istio is one of the oldest and most widely used Service Mesh frameworks available, and is regarded as the industry standard choice in terms of available features and stability. Istio's downside of being one of the first commerically viable mesh frameworks includes increased complexity for implementation and management, as well as slightly lower performance and higher resource usage when compared to newer generation meshes.

Istio in Kubernetes consists of auto-injecting one sidecar per pod, and leveraging a Control Plane for handling sidecar interactions. The Pilot handles traffic management and service discovery, the Mixer handles policy enforcement and is the intermediary between sidecar proxies, while the Citadel is the certificate authority, and handles certificate generation and rotation policies for each sidecar.

![Istio Architecture Diagram](https://lh4.googleusercontent.com/lgf8864y3CWcXV-E1fROnEi553v1cbVoJTPLDGfnK0-HaMF_qcIp3Ff9M4qrMcYOs-Dp8nxYlWkzfLf6fPCb9gc2CaIMlCHTVLzY1oghfTYNHa5ukppkdo2qzLkdsHDYJjps1YDX "Istio Architecture Diagram")

Below are the features supported by the Istio architecture:

|     Features     | Support |
|:----------------:|:-------:|
| mTLS             |   Yes   |
| Cert Mgmt        |   Yes   |
| Circuit Breaking |   Yes   |
| Rate Limiting    |   Yes   |
| Chaos Testing    |   Yes   |
| Distrib. Tracing |   Yes   |
| Multicluster     |   Yes   |
| GUI Interface    | Add-on  |

#### Summary

Istio is the complex solution for the service mesh pattern, but also provides the best flexibility and additionally available features when compared to other service mesh solutions (i.e. Circuit Breaking, Chaos Testing, Rate Limiting, etc). Istio tends to be on the higher resource usage and lower performance side of the scale when compared to other service mesh options [[Performance Analysis of Istio and Linkerd](https://kinvolk.io/blog/2019/05/performance-benchmark-analysis-of-istio-and-linkerd/)].

## [Linkerd v2](https://linkerd.io/)

###### Note: Linkerd v2 is a complete rearchitecture of their v1 system, and the two are treated as separate entities.

Linkerd is unique by currently being the only service mesh solution which is a part of the Cloud Native Foundation, the organization responsible for Kubernetes. Linkerd provides a simpler implementation and setup of Istio, as the cost of some of the additional features that Istio can provide.

A detailed breakdown of the Linkerd v2 architecture can be seen [here](https://linkerd.io/2/reference/architecture/).

![Linkerd v2 Architecture Diagram](https://linkerd.io/images/architecture/control-plane.png "Linkerd v2 Architecture Diagram")

Below are the features supported by the Linderd v2 architecture:

|     Features     | Support |
|:----------------:|:-------:|
| mTLS             |   Yes   |
| Cert Mgmt        |   Yes   |
| Circuit Breaking |    No   |
| Rate Limiting    |    No   |
| Chaos Testing    |   Some  |
| Distrib. Tracing |   Some  |
| Multicluster     |    No   |
| GUI Interface    |   Yes   |

#### Summary

Linkerd is the simple solution for the service mesh pattern, but gains this simplicity at the cost of removing additional features and flexibility of the design pattern. Linkerd also has the slight edge over Istio in terms of performance and resource usage [[Performance Analysis of Istio and Linkerd](https://kinvolk.io/blog/2019/05/performance-benchmark-analysis-of-istio-and-linkerd/)].

## [Consul Connect](https://www.consul.io/docs/connect/index.html)

Consul Connect is a service mesh solution which is an extension of Consul. Consul Connect includes automatic mTLS on L4, and handles access control using "intentions". Since Consul Connect is an extension of Consul, it relies on the installation and use of Consul in the framework. The extension runs as a single control binary, with no additional systems needed (Optional external support using Vault for cert and secret management). Consul Connect also boasts the option of Native Integration, currently supporting a Go library, but the consul webpage states that "... without library support, it is still possible for any major language to integrate with Connect". This may also lead to additional libraries for native integration in other languages in the future.

![Consul Connect Architecture Diagram](https://containersonaws.com/images/consul-connect.png "Consul Connect Architecture Diagram")

Below are the features supported by the Consul Connect architecture:

|     Features     | Support |
|:----------------:|:-------:|
| mTLS             |   Yes   |
| Cert Mgmt        |   Yes   |
| Circuit Breaking |   Yes   |
| Rate Limiting    |   Yes   |
| Chaos Testing    |    No   |
| Distrib. Tracing |   Yes   |
| Multicluster     |   Yes   |
| GUI Interface    |    No   |

#### Summary

Consul Connect is the middle solution between Istio and Linkerd, providing a solid set of features and functionality, while trying to be more intuitive in terms of installation and management. Not as complex or fully featured as Istio, but not as simple and basic as Linkerd.

## [Kuma](https://kuma.io/)

Kuma is another open-source service mesh framework that is a part of the Kong family of products. Kuma can provide L4 mTLS encryption automatically for service-to-service communication, and has integration with Kubernetes as well as support to run on any other available platforms.

![Kuma Architecture Diagram](https://2tjosk2rxzc21medji3nfn1g-wpengine.netdna-ssl.com/wp-content/uploads/2019/09/main-diagram@2x.png "Kuma Architecture Diagram")

Kuma boasts to be the easiest and fastest way to implement a service mesh for your cluster (in minutes!), but is a recent addition to the Service Mesh landscape (September 2019), so there is significantly less documentation and research on the system and it's future available features.

## [AWS App Mesh](https://aws.amazon.com/app-mesh/)

App Mesh is the AWS solution for a service mesh option, however it's mTLS encryption features are still in their beta "App Mesh Preview Channel" program, and the AWS App Mesh itself is not currently available in either GovCloud region.

## Additional Readings
##### [Comparison of Istio, Linkerd, and Consul Connect (platform9.com)](https://platform9.com/blog/kubernetes-service-mesh-a-comparison-of-istio-linkerd-and-consul/)
##### [Comparison of Istio, Linkerd, and Consul Connect (cloudops.com)](https://www.cloudops.com/2019/03/comparing-service-meshes-istio-linkerd-and-consul-connect/)
##### [Comparison of Istio, Linkerd, and Consul Connect (Slide Deck)](https://events19.linuxfoundation.org/wp-content/uploads/2018/11/OSN-Days-PPT-Service-Mesh.pdf)
##### [Linkerd or Istio?](https://itnext.io/linkerd-or-istio-2e3ce781fa3a)
##### [Performance Analysis of Istio and Linkerd](https://kinvolk.io/blog/2019/05/performance-benchmark-analysis-of-istio-and-linkerd/)