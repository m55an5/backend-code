Feature: Process Transactions and find fraudulent card hash

  @singleTrans
  Scenario Outline: Processing Single Transaction WITHIN 24 hour window
    Given I create a csv file with name "<csv>"
    And I add hash "<hash>" with time "<time>" and amount "<amount>"
    And I create the csv file
    When I run processTrans request with csv file and threshold "<threshold>"
    Then I "<expect>" expect "<hash>" in faudulent output

    Examples:
    | hash                         | csv          | time                 | amount  | threshold | expect |
    | 10d7ce2f43e35fa57d1bbf8b1e2  | testing.csv  | 2020-09-23T10:00:00  | 20.00   |  20.00    | dont   |
    | 10d7ce2f43e35fa57d1bbf8b1e2  | testing.csv  | 2020-09-23T10:00:00  | 20.00   |  19.00    | do     |

  @singleHash @within24hr @validTrans
  Scenario Outline: Single-Hash Valid Transaction : Whithin 24 hr window
    Given I create a csv file with name "testing.csv"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T20:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T04:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T09:59:59" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T10:00:00" and amount "80"
    And I create the csv file
    When I run processTrans request with csv file and threshold "80.00"
    Then I "dont" expect "10d7ce2f43e35fa57d1bbf8b1F2" in faudulent output


    @singleHash @within24hr @InvalidTrans
    Scenario: Single-Hash Invalid Transaction : Within 24hr Window
    Given I create a csv file with name "testing.csv"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T10:00:00" and amount "80"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T10:00:00" and amount "20"
    And I create the csv file
    When I run processTrans request with csv file and threshold "100.00"
    Then I "do" expect "10d7ce2f43e35fa57d1bbf8b1F2" in faudulent output

    @singleHash @InvalidTrans
    Scenario: Single-Hash Invalid Transaction : Over 24hr Window
    Given I create a csv file with name "testing.csv"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T20:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T04:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T09:59:59" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T10:00:00" and amount "101"
    And I create the csv file
    When I run processTrans request with csv file and threshold "100.00"
    Then I "do" expect "10d7ce2f43e35fa57d1bbf8b1F2" in faudulent output

        
  @MutliHash @within24hr @validTrans
  Scenario: Multiple-Hash Valid Transaction : Within 24 hr window 
    Given I create a csv file with name "testing.csv"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-23T20:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-24T04:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-24T09:59:59" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-24T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T20:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T04:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T09:59:59" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T10:00:00" and amount "20"
    And I create the csv file
    When I run processTrans request with csv file and threshold "80.00"
    Then I "dont" expect "10d7ce2f43e35fa57d1bbf8b1e2" in faudulent output
    And I "dont" expect "10d7ce2f43e35fa57d1bbf8b1F2" in faudulent output




  @MutliHash @within24hr @InvalidTrans
  Scenario: Multiple-Hash Invalid Transaction : Within 24 hr window 
    Given I create a csv file with name "testing.csv"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-23T20:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-24T04:00:00" and amount "90"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-24T09:59:59" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-24T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T20:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T04:00:00" and amount "90"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T09:59:59" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T10:00:00" and amount "20"
    And I create the csv file
    When I run processTrans request with csv file and threshold "100.00"
    Then I "do" expect "10d7ce2f43e35fa57d1bbf8b1e2" in faudulent output
    And I "do" expect "10d7ce2f43e35fa57d1bbf8b1F2" in faudulent output


  @MutliHash @InvalidTrans
  Scenario: Multiple-Hash Invalid Transaction : Over 24 hr window 
    Given I create a csv file with name "testing.csv"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-23T20:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-24T04:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-24T09:59:59" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2" with time "2020-09-24T10:00:00" and amount "101"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T10:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-23T20:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T04:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T09:59:59" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1F2" with time "2020-09-24T10:00:00" and amount "101"
    And I create the csv file
    When I run processTrans request with csv file and threshold "100.00"
    Then I "do" expect "10d7ce2f43e35fa57d1bbf8b1e2" in faudulent output
    And I "do" expect "10d7ce2f43e35fa57d1bbf8b1F2" in faudulent output

  
  @MutliHash @within24hr 
  Scenario: Multiple-Hash Records in CSV within 24hr Window 
    Given I create a csv file with name "testing.csv"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2A" with time "2020-09-23T10:00:00" and amount "110"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2B" with time "2020-09-23T11:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2B" with time "2020-09-23T12:00:00" and amount "80"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2C" with time "2020-09-23T13:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2D" with time "2020-09-23T14:00:00" and amount "20"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2E" with time "2020-09-23T15:00:00" and amount "0"
    And I add hash "10d7ce2f43e35fa57d1bbf8b1e2F" with time "2020-09-23T16:00:00" and amount "100.01"
    And I create the csv file
    When I run processTrans request with csv file and threshold "100.00"
    Then I "do" expect "10d7ce2f43e35fa57d1bbf8b1e2A" in faudulent output
    Then I "dont" expect "10d7ce2f43e35fa57d1bbf8b1e2B" in faudulent output
    Then I "dont" expect "10d7ce2f43e35fa57d1bbf8b1e2C" in faudulent output
    Then I "dont" expect "10d7ce2f43e35fa57d1bbf8b1e2D" in faudulent output
    Then I "dont" expect "10d7ce2f43e35fa57d1bbf8b1e2E" in faudulent output
    Then I "do" expect "10d7ce2f43e35fa57d1bbf8b1e2F" in faudulent output


    



    