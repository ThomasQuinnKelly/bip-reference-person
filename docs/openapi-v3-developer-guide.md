# BIP OpenAPI v3 Developer Guide

OpenAPI is the open source successor to the old Swagger specification. The framework team has spent time investigating the two main approaches to using OpenAPI, and any issues that arise. There is value in being aware of [the research and its findings](https://github.com/department-of-veterans-affairs/bip-reference-person/tree/master/docs/openapi-v3-api-code-generation-journey.md).

BIP Framework began with support for the legacy Code First approach. With the move to OpenAPI v3, the framework is changing its API model to the Design First approach. In many ways this makes it easier for new projects, but imposes an upgrade requirement on existing projects.

This document describes how to use OpenAPI v3 in service applications built on BIP Framework, specifically:

- How framework prepares OpenAPI for you at build time
- Application configuration
- How to specify an API for BIP consumption
- What code gets generated, and best practices for integrating that code into your application
- Steps to migrate Code First applications to the new API paradigm

## Framework build responsibilities

## Application Configuration

### Maven POM

## How to specify the API

### Application configuration

OpenAPI.yml

### messages.properties and MessageKeys

### .openapi-generator.ignore

## Generated code

### The interface

### Integrating the application code

# Migrating from Code First to Design First

## Get the current API definition

## Modify the current definition

## Update maven configuration

## Update classes and config

REST Resource classes Model objects and dependents messages.properties / MessageKeys and dependents

## Clean up

Remove old annotations ?
