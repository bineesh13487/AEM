# I expect that element "$string" (has|does not have) the class "$string"

Feature: Test if a given element has a certain CSS class
    As a developer
    I want to be able to test if a element has a certain CSS class

    Background:
        Given I open the site "/index.html"

    Scenario: Element <body> should have the class "babel"
        Then  I expect that element "body" has the class "babel"

    Scenario: Element <body> should not have the class "class3"
        Then  I expect that element "body" does not have the class "class3"
