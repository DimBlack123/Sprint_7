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

public class LoginCourierTest extends BaseTest {

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
    @DisplayName("Login courier")
    public void loginCourier() {
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
    @DisplayName("Can not login unknown courier")
    public void canNotLoginUnknownCourier() {
        Response loginCourierResponse = sendPostRequestCourierLogin(credentials);

        checkStatusCode(loginCourierResponse, 404);

        checkStatusCodeInAnswer(loginCourierResponse, 404);

        checkErrorMessage(loginCourierResponse, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Checking required fields when login courier")
    public void checkingRequiredFieldsWhenLoginCourier() {
        Response loginCourierResponse = sendPostRequestCourierLogin("{\"password\":\"1234\"}");

        checkStatusCode(loginCourierResponse, 400);

        checkStatusCodeInAnswer(loginCourierResponse, 400);

        checkErrorMessage(loginCourierResponse, "Недостаточно данных для входа");
    }

}
