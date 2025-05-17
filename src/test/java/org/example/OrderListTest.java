package org.example;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class OrderListTest extends BaseTest {

    @Step("Send Get request /orders")
    public Response sendGetRequestOrderCreate() {
        return given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(bearerToken)
                .when()
                .get("/orders");
    }

    @Step("Check array exists")
    public void checkArrayExists(Response response) {
        assertTrue(response.body().asString().contains("\"orders\":["));
    }

    @Test
    @DisplayName("Get order list")
    public void getOrderList() {
        Response createOrderResponse = sendGetRequestOrderCreate();

        checkStatusCode(createOrderResponse, 200);

        checkArrayExists(createOrderResponse);
    }

}
