# Feign and REST Clients Exception Management

## Exception Management

- OcpRestGlobalExceptionHandler in framework is used to alter HTTP response codes from our REST service calls. It handles different Exception scenarios as listed below and they are converted to ERROR messages with the corresponding Http Status codes shown below:
  
  a. IllegalArgumentException  --- HttpStatus.BAD_REQUEST
  b. MethodArgumentNotValidException -- HttpStatus.BAD_REQUEST
  c. HttpClientErrorException -- HttpStatus.BAD_REQUEST
  d. MethodArgumentTypeMismatchException -- HttpStatus.BAD_REQUEST
  e. ConstraintViolationException -- HttpStatus.BAD_REQUEST
  f. HttpMessageNotReadableException -- HttpStatus.BAD_REQUEST
  g. HttpRequestMethodNotSupportedException -- HttpStatus.METHOD_NOT_ALLOWED
  h. MediaTypeNotSupportedStatusException -- HttpStatus.UNSUPPORTED_MEDIA_TYPE
  i. OcpRuntimeException -- No translation but original exception values are used
  j. Exception -- HttpStatus.INTERNAL_SERVER_ERROR
  
- Invalid input is handled at the Resource end point using JSR 303 Validations and validation exceptions are propagated to  OcpRestGlobalExceptionHandler to be converted as 400(HttpStatus.BAD_REQUEST) Http Status Code in the Response Object.

- ExceptionHandlingUtils can be used to transform the run time exceptions into appropriate OcpRuntimeException class to be handled via OcpRestGlobalExceptionHandler

## Feign Client

- Feign Client interrogates the Response object to convert the HttpStatus code for the Response. Please see PersonRestClientTester for reference implementation
		
	@ApiOperation(value = "An endpoint which uses a REST client using Feign to call the remote person by pid operation.")
	@RequestMapping(value = URL_PREFIX + "/callPersonByPidUsingFeignClient", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonInfoResponse>
			callPersonByPidUsingFeignClient(@RequestBody final PersonInfoRequest personInfoRequest) {

		// use this in case of feign hystrix to test fallback handler invocation
		// NOSONAR ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.circuitBreaker.forceOpen", "true");
		PersonInfoResponse personInfoResponse = null;

		personInfoResponse = feignPersonClient.personByPid(personInfoRequest); // NOSONAR cannot immediately return
		
		if (personInfoResponse != null) {
			if (personInfoResponse.hasErrors()) {
				return new ResponseEntity<>(personInfoResponse, HttpStatus.BAD_REQUEST);
			} else if (personInfoResponse.hasFatals()) {
				return new ResponseEntity<>(personInfoResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(personInfoResponse, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(personInfoResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

- If Hystrix is enabled on Feign Client then any exception other than HystrixBadRequestException or its subclasses invoke the FallBack methods. So HystrixBadRequestException or its subclasses being propagated need to be handled on the client side as shown above that are using the Hystrix enabled Feign Client.
	
## Rest Client

- No specific exception handling is required on the client for Rest Client as OcpRestGlobalExceptionHandler does the transformation of exception messages to appropriate error messages. Please refer sample code at PersonRestClientTester class.

	@ApiOperation(value = "An endpoint which uses a REST client using RestTemplate to call the remote echo operation.")
	@RequestMapping(value = URL_PREFIX + "/callPersonByPidUsingRestTemplate", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PersonByPidDomainResponse>
			callPersonByPidUsingRestTemplate(@RequestBody final PersonByPidDomainRequest personByPidDomainRequest) {
		// invoke the service using classic REST Template from Spring, but load balanced through Consul
		HttpEntity<PersonByPidDomainRequest> requestEntity = new HttpEntity<>(personByPidDomainRequest);
		ResponseEntity<PersonByPidDomainResponse> exchange = null;
			exchange =
					personUsageRestTemplate.executeURL("http://localhost:8080" + PersonResource.URL_PREFIX + "/pid",
							HttpMethod.POST, requestEntity, new ParameterizedTypeReference<PersonByPidDomainResponse>() {
							});
		LOGGER.info("Invoked os-reference-person service using REST template: " + exchange);
		return exchange;
	}

