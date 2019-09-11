Feature: Upload And Download of Documents For a Person

  @persondocsupload @happypath
  Scenario Outline: Uploads a valid text document for a person with PID
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    And upload a document "<Document>"
    And submit a payload "<Payload>"
    When uploads a valid document for a person with PID using "<ServiceURL>"
    Then the service returns status code = 200

    @DEV
    Examples: 
      | Veteran           | tokenrequestfile               | ServiceURL          			   | Document               		   | Payload |
      | dev-janedoe       | dev/janedoetoken.request       | /api/v1/persons/6666345/documents | persondocs_sample_file.txt        | person_docs_metadata.json |
  
    @VA
    Examples: 
      | Veteran          | tokenrequestfile              | ServiceURL          				 | Document                         | Payload |
      | va-janedoe       | va/janedoetoken.request       | /api/v1/persons/6666345/documents | persondocs_sample_file.txt       | person_docs_metadata.json |
      
  @persondocsmetadata @happypath
  Scenario Outline: Get Metadata for a person with PID
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When gets a valid document metadata for a person with PID using "<ServiceURL>"
    Then the service returns status code = 200

    @DEV
    Examples: 
      | Veteran           | tokenrequestfile               | ServiceURL         | 			   
      | dev-janedoe       | dev/janedoetoken.request       | /api/v1/persons/6666345/documents/metadata |
  
    @VA
    Examples: 
      | Veteran          | tokenrequestfile              | ServiceURL      |    				 
      | va-janedoe       | va/janedoetoken.request       | /api/v1/persons/6666345/documents/metadata |
      
   @persondocsdownload @happypath
  Scenario Outline: Get Metadata for a person with PID
    Given the claimant is a "<Veteran>"
    And invoke token API by passing header from "<tokenrequestfile>" and sets the authorization in the header
    When download a sample document using "<ServiceURL>"
    Then the service returns status code = 200
    And validate sample document download returned message "<Text>"

    @DEV
    Examples: 
      | Veteran           | tokenrequestfile               | ServiceURL         | 	Text |		   
      | dev-janedoe-download       | dev/janedoetoken.request       | /api/v1/persons/documents/sample | Sample Reference data |
  
    @VA
    Examples: 
      | Veteran          		| tokenrequestfile              | ServiceURL      |   Text | 				 
      | va-janedoe-download     | va/janedoetoken.request       | /api/v1/persons/documents/sample | Sample Reference data |

