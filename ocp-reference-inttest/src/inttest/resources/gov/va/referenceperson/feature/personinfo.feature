Feature: PID based Person Info from Person Partner Service.

  @personinfo @happypath
  Scenario Outline: PID based Person Info from Person Partner Service.
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When client request person info "<ServiceURL>" with PID data "<RequestFile>"
    Then the service returns status code = 200
    And the service returns ParticipantID PID based on participantId <participantID>

    @DEV
    Examples: 
      | Veteran           | tokenrequestfile               | ServiceURL          | RequestFile               | participantID |
      | dev-janedoe       | dev/janedoetoken.request       | /api/v1/persons/pid | dev/janedoe.request       |       6666345 |
      | dev-russellwatson | dev/russellwatsontoken.request | /api/v1/persons/pid | dev/russellwatson.request |      13364995 |

    @VA
    Examples: 
      | Veteran          | tokenrequestfile              | ServiceURL          | RequestFile              | participantID |
      | va-janedoe       | va/janedoetoken.request       | /api/v1/persons/pid | va/janedoe.request       |       6666345 |
      | va-russellwatson | va/russellwatsontoken.request | /api/v1/persons/pid | va/russellwatson.request |      13364995 |

  @personinfo
  Scenario Outline: PID based Person Info from Person Partner Service with different PID .
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When client request person info "<ServiceURL>" with PID data "<RequestFile>"
    Then the service returns status code = 200
    And the service returns ParticipantID PID based on participantId <participantID>
    And the service returns message "<Severity>" and "<Text>"

    @DEV
    Examples: 
      | Veteran     | tokenrequestfile               | ServiceURL          | RequestFile               | participantID | Severity | Text                                                                                                                                                                                                              |
      | dev-janedoe | dev/russellwatsontoken.request | /api/v1/persons/pid | dev/russellwatson.request |      13364995 | WARN     | A different Participant ID was retrieved than that of the logged in user. In a real service, this condition should throw a service exception (in this case, PersonServiceException) with INVOKE_FALLBACK_MESSAGE. |

    @VA
    Examples: 
      | Veteran    | tokenrequestfile              | ServiceURL          | RequestFile              | participantID | Severity | Text                                                                                                                                                                                                              |
      | va-janedoe | va/russellwatsontoken.request | /api/v1/persons/pid | va/russellwatson.request |      13364995 | WARN     | A different Participant ID was retrieved than that of the logged in user. In a real service, this condition should throw a service exception (in this case, PersonServiceException) with INVOKE_FALLBACK_MESSAGE. |

  @personinfo
  Scenario Outline: PID based Person Info from Person Partner Service for invalid PID.
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When client request person info "<ServiceURL>" with PID data "<RequestFile>"
    Then the service returns status code = 200
    And the service returns message "<Text>"

    @DEV
    Examples: 
      | Veteran     | tokenrequestfile         | ServiceURL          | RequestFile         | Text                                                                                                                                                                                                       |
      | dev-janedoe | dev/janedoetoken.request | /api/v1/persons/pid | dev/invalid.request | Could not read mock XML file 'test/mocks/person.getPersonInfoByPtcpntId.12.xml' using key 'person.getPersonInfoByPtcpntId.12'. Please make sure this response file exists in the main/resources directory. |

    @VA
    Examples: 
      | Veteran    | tokenrequestfile        | ServiceURL          | RequestFile        | Text                                                                                                                                                                                                       |
      | va-janedoe | va/janedoetoken.request | /api/v1/persons/pid | va/invalid.request | Could not read mock XML file 'test/mocks/person.getPersonInfoByPtcpntId.12.xml' using key 'person.getPersonInfoByPtcpntId.12'. Please make sure this response file exists in the main/resources directory. |

  @personinfo
  Scenario Outline: PID based Person Info from Person Partner Service for null PID.
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When client request person info "<ServiceURL>" with PID data "<RequestFile>"
    Then the service returns status code = 400
    And the service returns message "<Text>"

    @DEV
    Examples: 
      | Veteran           | tokenrequestfile               | ServiceURL          | RequestFile      | Text                                           |
      | dev-russellwatson | dev/russellwatsontoken.request | /api/v1/persons/pid | dev/null.request | PersonInfoRequest.participantID cannot be null |

    @VA
    Examples: 
      | Veteran          | tokenrequestfile              | ServiceURL          | RequestFile     | Text                                           |
      | va-russellwatson | va/russellwatsontoken.request | /api/v1/persons/pid | va/null.request | PersonInfoRequest.participantID cannot be null |
