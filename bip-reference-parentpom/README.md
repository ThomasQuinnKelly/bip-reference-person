## What is this project for? ##

Parent POM for service applications built on BIP Framework. It provides common Maven configuration and dependencies for the suite of projects, and common profiles used for build environments and other common build capabilities.

### POM notes

The POM must use bip-framework-parentpom as its parent to enable core features and capabilities of the BIP Framework.
```xml
<!-- ./bip-[application-name]-parentpom POM file -->
      <parent>
        <groupId>gov.va.bip.framework</groupId>
        <artifactId>bip-framework-parentpom</artifactId>
        <version>VERSION</version>
        <relativePath />
      </parent>
```

See [pom.xml](https://github.com/department-of-veterans-affairs/bip-reference-person/blob/master/bip-reference-parentpom/pom.xml) for the detailed configuration and dependency management.
