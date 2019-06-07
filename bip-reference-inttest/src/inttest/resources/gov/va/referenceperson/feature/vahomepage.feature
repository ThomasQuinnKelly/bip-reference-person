Feature: Validate VA home page to check the header, banner, containers and links inside the containers.

  @vahomepage
  Scenario Outline: Validate VA home page header
    Given the veteran navigates to VA homepage URL "<ServiceURL>"
    When the user is in the VA homepage
    Then verify the header in the VA home page
    And verify the official USA website notice banner
    And verify the crisis line banner

    @DEVUI
    Examples: 
      | ServiceURL          |
      | https://www.va.gov/ |

    @VAUI
    Examples: 
      | ServiceURL          |
      | https://www.va.gov/ |

  @vahomepage
  Scenario Outline: Validate VA health care and disablity container links
    Given the veteran navigates to VA homepage URL "<ServiceURL>"
    When the user is in the VA homepage
    Then verify the title in the home page hub container
    And verify the other links inside the health care container
    And verify the other links inside the disablity container

    @DEVUI
    Examples: 
      | ServiceURL          |
      | https://www.va.gov/ |

    @VAUI
    Examples: 
      | ServiceURL          |
      | https://www.va.gov/ |
