# BIP Framework Capabilities Summary

## Configuration
Base config for logger properties, yaml file support, spring profiles.

## Exceptions
Base BipExceptionExtender allows exception types to conform to BIP messaging requirements.
See ServiceMessage, and BipException / BipRuntimeException.

## Logging
BipLoggerFactory creates BipLoggers which are fully compatible with slf4j and logback. The logger adds BipBanner (an ASCII-text banner) and Levels. The logger also splits and manages exceptions so they can cross the docker 16KB comm channel limitation (https://github.com/kubernetes/kubernetes/issues/52444).

## Audit Logging
AuditLogger uses AuditEventData and implementers of AuditableData to asynchronously log audit events through the AuditLogSerializer.

Audit events may be triggered from an aspect or interceptor.  Audit can manually be invoked on a class or method with the @Auditable annotation.

## Model Transformation
Simple transformer pattern to support transformation between the Provider (REST) model, Domain (service) model, and external Partner models. See the framework.transfer.transform package.

