package ru.juli.practicum.constants;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class URL {
    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";
    public static final String CREATE_COURIER = "/api/v1/courier";
    public static final String DELETE_COURIER = "/api/v1/courier/";
    public static final String LOGIN_COURIER = "/api/v1/courier/login";
    public static final String ORDERS = "/api/v1/orders";
    public static final String CANCEL_ORDER = "/api/v1/orders/cancel";

    public static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .log(LogDetail.ALL)
                    .addHeader("Content-type", "application/json")
                    .setBaseUri(BASE_URI)
                    .build();
    public static final ResponseSpecification RESPONSE_SPECIFICATION =
            new ResponseSpecBuilder().log(LogDetail.ALL).build();
}
