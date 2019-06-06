Feature: Validate VA home page to check the header, banner, containers and links inside the containers.

  @vahomepae
  Scenario Outline: Validate VA home page header
    Given the veteran navigates to VA homepage URL "<ServiceURL>"
    When the user is in the VA homepage
    Then verify the header in the VA home page
    And verify the official USA website notice banner
    And verify the crisis line banner 
    And verify the other links in the navigation menu

    @vahomepae @DEV
    Examples: 
      | ServiceURL          |
      | https://www.va.gov/ |

    @vahomepae
    Examples: 
      | ServiceURL          |
      | https://www.va.gov/ |
