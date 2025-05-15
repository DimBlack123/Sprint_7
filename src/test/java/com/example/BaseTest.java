package com.example;

import io.restassured.RestAssured;
import org.junit.Before;

public abstract class BaseTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1";
    }

    public static final String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2Nzk0YjNkMjJmNjAzNjAwM2Q5ZjIxNjciLCJpYXQiOjE3NDcxNTUyMDEsImV4cCI6MTc0Nzc2MDAwMX0.4BxlLAHvG3DD9Z4JKdFTo4qB35YO4Ib_EeoBqxrZI_Y";

    public static final String credentials = "{\"login\":\"noop\",\"password\":\"1234\"}";

}
