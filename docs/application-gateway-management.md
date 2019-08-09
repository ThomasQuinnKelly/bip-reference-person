# Application Gateway Management

## What do we need from an API gateway?
- Highly available
- Secure
- Authoritative
- Scalable
- Authentication Mechanism
- Load Balancing
- Routing
- Rate Limiting
- Logging
- Internal communications between APIs

## API Gateway
An API gateway allows consumers to call operations on an API. In reality, however, they are only calling a façade which acts a reverse proxy on steroids in front of upstream APIs. The gateway is in charge of forwarding all the traffic to the physical API, also known as upstream API, and return the responses to the consumers. However, the gateway provides more functionality than just traffic routing - It also allows you to handle service authentication, traffic control such as rate limiting, request/response transformation and more.

The beauty of API gateways is that it allows you to align all your APIs, without having to change anything on the physical API. For example, you can expose your APIs to your consumers by using key authentication, while the API gateway uses mutual authentication with the upstream system.

## Kong
Kong, previously known as Kong Community (CE), is an open-source scalable API gateway technology that is initiated by Kong Inc and has a growing community.

The gateway is built on top of NGINX and can be extended further by using the open-source plug-ins, either by Kong themselves or the community or write your own plug-ins via Lua. Kong can be configured in front of any RESTful API and let the developers concentrate more on implementing business logic without worrying about functionalities like authentication mechanism, rate limiting, logging,  internal communications between APIs, carrying out communication with public entities and other organizations. It’s like a security layer  which sits in front of your application and enhances it’s performance. Kong provides full control over architecture and it’s currently used by many organizations including small and large ones.

Once the gateway is running, it will expose two endpoints:

- A proxy endpoint where consumers can send all their traffic to
- A management endpoint, also known as Kong Management API, to configure the gateway itself.

Administrators can use the Admin REST API and CLI to configure the gateway which allows them to fully script repetitive tasks. However, there is no out-of-the-box support for infrastructure-as-code to manage your gateway.

Every gateway is backed by a data store that contains all the relevant configuration which can be stored in Apache Cassandra or PostgreSQL. This is also where Kong shines as it is a cluster-based technology that allows you to easily scale out by adding more nodes, but it still only requires one data store making it easier to manage. The client applications talks to Kong and then Kong acts as a reverse proxy and routes the requests to the applications on the basis of managed plugins in Kong.

Note: Kong, by default, listens to API Requests on port 8000 and it’s RESTful admin interface runs on port 8001.

## Out-of-the-box features
Kong is an API gateway and we need to run it ourselves but what does it bring out-of-the-box?

- Simple Deployments - Container based and easy to deploy wherever we want, if we also have a data store collocated.

- Flexible gateway routing - You can fully configure how the gateway should route your traffic by using `Routes`. These define if traffic should be by Host-header, Method, URI path or a combination of those. ([docs](https://docs.konghq.com/0.14.x/proxy/) | [example](https://docs.konghq.com/0.14.x/proxy/#routes-and-matching-capabilities))

- Open-Source Plug-in Ecosystem - A rich collection of plug-ins which provide the capability to use built-in features from Kong Hub in a variety of areas: Authentication, Traffic Control, Analytics, Transformations, Logging, Serverless and many more.

- Load Balancing - Besides traditional DNS-based balancing, Kong provides a Ring-based balancer where you can either use round-robin or a customized hash-based balancer following the Ketama principle. In the case of hash-based balancing, it is up to the users to define if they want to balance based on a hash of `consumer`, `ip`, `header`, or `cookie`. [docs](https://docs.konghq.com/0.14.x/loadbalancing/#ring-balancer)
     - Ring-based load balancing also performs health checks and uses circuit breakers.

- Community Support
Pretty impressive what Kong delivers for a free product - Super easy to deploy, an entire ecosystem of plugins and some flexible routing!

However, things that are lacking:

- A developer portal where customers can browse the APIs that we expose. This is the biggest downside.

- An administration portal where we can monitor and configure everything. APIs are great for automation, but it always helps if you have a UI for operations and troubleshooting.
     - Luckily the community provides this with tools like Konga & Dashy.

- Analytics of how our APIs are being used as this is crucial to gain insights in your system. This can help you optimize your platform by tweaking your throttling, diagnosing issues, etc.

And this is where Kong Enterprise comes in!

## An ecosystem of plugins
One of the core components of Kong is the ecosystem of plugins - They provide the extensibility to install additional components that enrich your APIs with additional capabilities and extends your API landscape.

These plugins are either built and maintained by Kong Inc or by the community providing you with an easy way to extend your APIs by installing scoped units of features. For example, not everybody needs rate limiting, but if you do you can easily install a plugin for that.

Next, it provides you with the capability to easily integrate Kong with a 3rd party service so that you can for example trigger an Azure Function, expose metrics for Prometheus or log requests to loggly.com.

A plugin can also be installed on different levels in the Kong object structure such as only for a specific route, consumer or even globally so that it applies for everything and everybody.

Kong Hub, the Kong Plugin store if you will, consolidates official and open-source plugins and are grouped in the following categories:

- **Authentication** - Protect your services with an authentication layer
- **Security** - Secure your API traffic against malicious actors
- **Traffic Control** - Manage, throttle, and restrict inbound and outbound API traffic
- **Serverless** - Invoke serverless functions via APIs
- **Analytics & Monitoring** - Visualize, inspect, and monitor APIs and microservice traffic
- **Transformations** - Transform requests and responses on the fly
- **Logging** - Stream request and response data to logging solutions
- **Deployment** - Run Kong on a platform of choice which is not officially supported by Kong

However, not all OSS plugins are in Kong Hub as developers can apply to add them but it's not a must. You can find more on LuaRocks.org or GitHub. 

You can also write your own Kong plugins in Lua that will run on NGINX. Interested in learning more? They have a [plugin development guide](https://docs.konghq.com/0.14.x/plugin-development/).

The beauty of this ecosystem is that it's open and everybody can contribute to them. Next to that, all Kong plugins are OSS where you can have a look or even contribute to it.

It's good to keep in mind that the more advanced official plugins are only available for Kong Enterprise. However, it makes sense given they are only applicable for big enterprises and also because there is no free lunch. 

They provide a lightweight gateway for you and you add the features that you need; either provided by Kong, the community or self-written. It's all possible!

## Kong JWT Plugin
Verify requests containing HS256 or RS256 signed JSON Web Tokens (as specified in RFC 7519). Each of your Consumers will have JWT credentials (public and secret keys) which must be used to sign their JWTs. A token can then be passed through:

- a query string parameter,
- a cookie,
- or the Authorization header.

Kong will either proxy the request to your upstream services if the token’s signature is verified, or discard the request if not. Kong can also perform verifications on some of the registered claims of RFC 7519 (exp and nbf). Review the docker compose file to see how this plugin is configured for BIP reference person end points.

## Kong JWT Signer

The Kong JWT Signer plugin makes it possible to verify and (re-)sign one or two tokens in a request, that the plugin refers as access token and channel token. The plugin supports both opaque tokens (via introspection) and signed JWT tokens (JWS tokens via signature verification). access_token and channel_token are names of the tokens and they can be any valid verifiable tokens. E.g. two access tokens (one given to end user and one given to client application).