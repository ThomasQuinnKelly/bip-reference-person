Feature: Uploads a valid document for a person

  @persondocsupload @happypath
  Scenario Outline: Submit a text document
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    And upload a document "<Document>"
    And submit a payload "<Payload>"
    When uploads a valid document for a person using "<ServiceURL>"
    Then the service returns status code = 200

    @DEV
    Examples: 
      | Veteran           | tokenrequestfile               | ServiceURL          			   | Document               		   | Payload |
      | dev-janedoe       | dev/janedoetoken.request       | /api/v1/persons/6666345/documents | persondocs_sample_file.txt        | person_docs_metadata.json |
  
    @VA
    Examples: 
      | Veteran          | tokenrequestfile              | ServiceURL          				 | Document                         | Payload |
      | va-janedoe       | va/janedoetoken.request       | /api/v1/persons/6666345/documents | persondocs_sample_file.txt       | person_docs_metadata.json |
