Feature: PID based Person Info from Person Partner Service.

  @personinfo
  Scenario Outline: PID based Person Info from Person Partner Service.
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When client request person info "<ServiceURL>" with PID data "<RequestFile>"
    Then the service returns status code = 200
		## And the response should be same as "<ResponseFile>"
   
    @DEV
    Examples: 
      | Veteran     | tokenrequestfile  | ServiceURL               | RequestFile            | ResponseFile        |
      | dev-janedoe | dev/token.request | /service-1/v1/person/pid | dev/personinfo.request | personinfo.response |

    @VA
    Examples: 
      | Veteran    | tokenrequestfile | ServiceURL               | RequestFile           | ResponseFile        |
      | va-janedoe | va/token.request | /service-1/v1/person/pid | va/personinfo.request | personinfo.response |
