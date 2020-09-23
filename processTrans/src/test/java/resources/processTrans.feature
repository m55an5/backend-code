Feature: Process Transactions and find fraudulent card hash

  Scenario: Processing Valid Transaction
    Given The CSV file exists
    And I read the CSV file
    When I run processTrans request
    Then I get output of invalid hash cards