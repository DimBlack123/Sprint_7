package com.example;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.ErrorResult;
import org.example.LoginResult;
import org.example.OkResult;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateCourierTest extends BaseTest {

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

    @Test
    @DisplayName("Create courier")
    public void createCourier() {
        Response createCourierResponse0 = sendPostRequestCourierCreate(credentials);

        checkStatusCode(createCourierResponse0, 201);

        checkOkResult(createCourierResponse0);

        Response loginCourierResponse = sendPostRequestCourierLogin(credentials);

        checkStatusCode(loginCourierResponse, 200);

        int id = getCourierIdAfterLogin(loginCourierResponse);

        Response deleteCourierResponse = sendDeleteRequestCourier(id);

        checkStatusCode(deleteCourierResponse, 200);

        checkOkResult(deleteCourierResponse);
    }

    @Test
    @DisplayName("Can not create same courier twice")
    public void canNotCreateSameCourierTwice() {
        Response createCourierResponse0 = sendPostRequestCourierCreate(credentials);

        checkStatusCode(createCourierResponse0, 201);

        checkOkResult(createCourierResponse0);

        Response createCourierResponse1 = sendPostRequestCourierCreate(credentials);

        checkStatusCode(createCourierResponse1, 409);

        checkStatusCodeInAnswer(createCourierResponse1, 409);

        checkErrorMessage(createCourierResponse1, "Этот логин уже используется. Попробуйте другой.");

        Response loginCourierResponse = sendPostRequestCourierLogin(credentials);

        int id = getCourierIdAfterLogin(loginCourierResponse);

        Response deleteCourierResponse = sendDeleteRequestCourier(id);

        checkStatusCode(deleteCourierResponse, 200);

        checkOkResult(deleteCourierResponse);
    }

    @Test
    @DisplayName("Checking required fields when creating courier")
    public void checkingRequiredFieldsWhenCreatingCourier() {
        Response createCourierResponse0 = sendPostRequestCourierCreate("{\"login\":\"noop\"}");

        checkStatusCode(createCourierResponse0, 400);

        checkStatusCodeInAnswer(createCourierResponse0, 400);

        checkErrorMessage(createCourierResponse0, "Недостаточно данных для создания учетной записи");

        Response createCourierResponse1 = sendPostRequestCourierCreate("{\"password\":\"1234\"}");

        checkStatusCode(createCourierResponse1, 400);

        checkStatusCodeInAnswer(createCourierResponse1, 400);

        checkErrorMessage(createCourierResponse1, "Недостаточно данных для создания учетной записи");
    }

}
