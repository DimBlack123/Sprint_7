package org.example;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;

import static io.restassured.RestAssured.given;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class BaseTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1";
    }

    public static final String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2Nzk0YjNkMjJmNjAzNjAwM2Q5ZjIxNjciLCJpYXQiOjE3NDcxNTUyMDEsImV4cCI6MTc0Nzc2MDAwMX0.4BxlLAHvG3DD9Z4JKdFTo4qB35YO4Ib_EeoBqxrZI_Y";

    public static final String credentials = "{\"login\":\"noop\",\"password\":\"1234\"}";

    @Step("Send POST request /courier")
    public Response sendPostRequestCourierCreate(String body) {
        return given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(bearerToken)
                .and()
                .body(body)
                .when()
                .post("/courier");
    }

    @Step("Send POST request /courier/login")
    public Response sendPostRequestCourierLogin(String body) {
        return given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(bearerToken)
                .and()
                .body(body)
                .when()
                .post("/courier/login");
    }

    @Step("Send DELETE request /courier/:id")
    public Response sendDeleteRequestCourier(int id) {
        return given()
                .auth()
                .oauth2(bearerToken)
                .when()
                .delete("/courier/{id}", id);
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int statusCode) {
        response.then().assertThat().statusCode(statusCode);
    }

    @Step("Check ok result")
    public void checkOkResult(Response response) {
        assertTrue(response.body().as(OkResult.class).isOk());
    }

    @Step("Check status code in answer")
    public void checkStatusCodeInAnswer(Response response, int statusCode) {
        assertEquals(response.body().as(ErrorResult.class).getCode(), statusCode);
    }

    @Step("Check error message")
    public void checkErrorMessage(Response response, String message) {
        byte[] bytes = response.body().as(ErrorResult.class).getMessage().getBytes(UTF_8);
        String value = new String(bytes, UTF_8);
        assertEquals(value, message);
    }

    @Step("Get courier id after login")
    public int getCourierIdAfterLogin(Response response) {
        return response.body().as(LoginResult.class).getId();
    }

}
