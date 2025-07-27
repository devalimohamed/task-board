package com.taskboard.bdd;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskStepDefinitions {

    @LocalServerPort
    private int port;

    private Response response;

    @When("I create a FEATURE task titled {string}")
    public void iCreateAFeatureTaskTitled(String title) {
        response = RestAssured.given()
                .port(port)
                .contentType(ContentType.JSON)
                .body("{\"taskType\":\"FEATURE\",\"title\":\"" + title + "\",\"priority\":\"MEDIUM\"}")
                .when()
                .post("/api/tasks");
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @And("the returned task status should be {string}")
    public void theReturnedTaskStatusShouldBe(String expectedTaskStatus) {
        assertEquals(expectedTaskStatus, response.jsonPath().getString("status"));
    }
}
