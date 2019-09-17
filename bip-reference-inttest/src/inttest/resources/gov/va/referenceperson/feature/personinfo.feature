Feature: PID Based Person Information from Person Partner Service.

  @personinfo @happypath
  Scenario Outline: PID based Person Info from Person Partner Service for valid PID.
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
  Scenario Outline: PID based Person Info from Person Partner Service for valid PID.
    Given the claimant is a "<Veteran>"
    When client request person info "<ServiceURL>" with PID data "<RequestFile>"
    Then the service returns status code = 401
    And the service returns content type "<Type>"

    @DEV
    Examples: 
      | Veteran     | tokenrequestfile         | ServiceURL          | RequestFile         | participantID | Type                     |
      | dev-janedoe | dev/janedoetoken.request | /api/v1/persons/pid | dev/janedoe.request |       6666345 | application/problem+json |

    @VA
    Examples: 
      | Veteran    | tokenrequestfile        | ServiceURL          | RequestFile        | participantID | Type                     |
      | va-janedoe | va/janedoetoken.request | /api/v1/persons/pid | va/janedoe.request |       6666345 | application/problem+json |

  @personinfo
  Scenario Outline: PID based Person Info from Person Partner Service for incorrect PID.
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When client request person info "<ServiceURL>" with PID data "<RequestFile>"
    Then the service returns status code = 400
    And the service returns message "<Text>"
    And the service returns content type "<Type>"

    @DEV
    Examples: 
      | Veteran           | tokenrequestfile               | ServiceURL          | RequestFile         | Text                                            | Type                     |
      | dev-janedoe       | dev/janedoetoken.request       | /api/v1/persons/pid | dev/invalid.request | Participant ID must be greater than zero.       | application/problem+json |
      | dev-russellwatson | dev/russellwatsontoken.request | /api/v1/persons/pid | dev/null.request    | PersonInfoRequest.participantID cannot be null. | application/problem+json |

    @VA
    Examples: 
      | Veteran          | tokenrequestfile              | ServiceURL          | RequestFile        | Text                                            | Type                     |
      | va-janedoe       | va/janedoetoken.request       | /api/v1/persons/pid | va/invalid.request | Participant ID must be greater than zero.       | application/problem+json |
      | va-russellwatson | va/russellwatsontoken.request | /api/v1/persons/pid | va/null.request    | PersonInfoRequest.participantID cannot be null. | application/problem+json |

  @personinfo
  Scenario Outline: PID based Person Info from Person Partner Service for no record found.
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When client request person info "<ServiceURL>" with PID data "<RequestFile>"
    Then the service returns status code = 200
    And the service returns message "<Severity>" and "<Text>"

    @DEV
    Examples: 
      | Veteran     | tokenrequestfile         | ServiceURL          | RequestFile               | Severity | Text                                                                                                                                                                                                             |
      | dev-janedoe | dev/janedoetoken.request | /api/v1/persons/pid | dev/norecordfound.request | WARN     | Could not read mock XML file test/mocks/person.getPersonInfoByPtcpntId.6666355.xml using key person.getPersonInfoByPtcpntId.6666355. Please make sure this response file exists in the main/resources directory. |

    @VA
    Examples: 
      | Veteran    | tokenrequestfile        | ServiceURL          | RequestFile              | Severity | Text                                                                                                                                                                                                             |
      | va-janedoe | va/janedoetoken.request | /api/v1/persons/pid | va/norecordfound.request | WARN     | Could not read mock XML file test/mocks/person.getPersonInfoByPtcpntId.6666355.xml using key person.getPersonInfoByPtcpntId.6666355. Please make sure this response file exists in the main/resources directory. |
