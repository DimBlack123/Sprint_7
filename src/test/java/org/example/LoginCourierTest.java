package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

public class LoginCourierTest extends BaseTest {

    private String courierId;

    @After
    public void clean() {
        if (courierId != null) {
            Response deleteCourierResponse = sendDeleteRequestCourier(Integer.parseInt(courierId));
            checkStatusCode(deleteCourierResponse, 200);
            courierId = null;
        }
    }

    @Test
    @DisplayName("Login courier")
    public void loginCourier() {
        Response createCourierResponse0 = sendPostRequestCourierCreate(credentials);

        checkStatusCode(createCourierResponse0, 201);

        checkOkResult(createCourierResponse0);

        Response loginCourierResponse = sendPostRequestCourierLogin(credentials);

        checkStatusCode(loginCourierResponse, 200);

        courierId = String.valueOf(getCourierIdAfterLogin(loginCourierResponse));
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
