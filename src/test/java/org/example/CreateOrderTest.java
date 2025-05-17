package org.example;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BaseTest {

    @Step("Send POST request /orders")
    public Response sendPostRequestOrderCreate(String body) {
        return given()
                .header("Content-type", "application/json")
                .auth()
                .oauth2(bearerToken)
                .and()
                .body(body)
                .when()
                .post("/orders");
    }

    @Step("Check field track exists")
    public void checkFieldTrackExists(Response response) {
        assertTrue(response.body().asString().contains("track"));
    }

    @Parameters
    public static Object[] data() {
        return new Object[]{
                // color BLACK and GRAY
                "{\n" +
                        "  \"firstName\": \"Naruto\",\n" +
                        "  \"lastName\": \"Uchiha\",\n" +
                        "  \"address\": \"Konoha, 142 apt.\",\n" +
                        "  \"metroStation\": 4,\n" +
                        "  \"phone\": \"+7 800 355 35 35\",\n" +
                        "  \"rentTime\": 5,\n" +
                        "  \"deliveryDate\": \"2020-06-06\",\n" +
                        "  \"comment\": \"Saske, come back to Konoha\",\n" +
                        "  \"color\": [\n" +
                        "    \"BLACK\", \"GRAY\"]\n" +
                        "}"
                ,
                // color BLACK
                "{\n" +
                        "  \"firstName\": \"Naruto\",\n" +
                        "  \"lastName\": \"Uchiha\",\n" +
                        "  \"address\": \"Konoha, 142 apt.\",\n" +
                        "  \"metroStation\": 4,\n" +
                        "  \"phone\": \"+7 800 355 35 35\",\n" +
                        "  \"rentTime\": 5,\n" +
                        "  \"deliveryDate\": \"2020-06-06\",\n" +
                        "  \"comment\": \"Saske, come back to Konoha\",\n" +
                        "  \"color\": [\n" +
                        "    \"BLACK\"]\n" +
                        "}"
                ,
                // no color
                "{\n" +
                        "  \"firstName\": \"Naruto\",\n" +
                        "  \"lastName\": \"Uchiha\",\n" +
                        "  \"address\": \"Konoha, 142 apt.\",\n" +
                        "  \"metroStation\": 4,\n" +
                        "  \"phone\": \"+7 800 355 35 35\",\n" +
                        "  \"rentTime\": 5,\n" +
                        "  \"deliveryDate\": \"2020-06-06\",\n" +
                        "  \"comment\": \"Saske, come back to Konoha\"\n" +
                        "}"
        };
    }

    private final String json;

    public CreateOrderTest(String json) {
        this.json = json;
    }

    @Test
    @DisplayName("Create order")
    public void createOrder() {
        Response createOrderResponse = sendPostRequestOrderCreate(json);

        checkStatusCode(createOrderResponse, 201);

        checkFieldTrackExists(createOrderResponse);
    }

}
