Feature: Task management API

  Scenario: Create a feature task
    When I create a FEATURE task titled "Realtime updates"
    Then the response status should be 201
    And the returned task status should be "TODO"
