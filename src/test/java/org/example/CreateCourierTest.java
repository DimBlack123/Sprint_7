package org.example;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

public class CreateCourierTest extends BaseTest {

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
    @DisplayName("Create courier")
    public void createCourier() {
        Response createCourierResponse0 = sendPostRequestCourierCreate(credentials);

        checkStatusCode(createCourierResponse0, 201);

        checkOkResult(createCourierResponse0);

        Response loginCourierResponse = sendPostRequestCourierLogin(credentials);

        checkStatusCode(loginCourierResponse, 200);

        courierId = String.valueOf(getCourierIdAfterLogin(loginCourierResponse));
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

        courierId = String.valueOf(getCourierIdAfterLogin(loginCourierResponse));
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
