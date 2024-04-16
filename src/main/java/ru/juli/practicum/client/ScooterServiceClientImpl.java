package ru.juli.practicum.client;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static ru.juli.practicum.constants.URL.*;

public class ScooterServiceClientImpl implements ScooterServiceClient {
    private RequestSpecification requestSpecification;
    private ResponseSpecification responseSpecification;

    public ScooterServiceClientImpl(RequestSpecification requestSpecification, ResponseSpecification responseSpecification) {
        this.requestSpecification = requestSpecification;
        this.responseSpecification = responseSpecification;
    }

    @Override
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .spec(requestSpecification)
                .body(courier)
                .post(CREATE_COURIER)
                .then()
                .spec(responseSpecification);
    }

    @Override
    public ValidatableResponse login(Credentials credentials) {
        return given()
                .spec(requestSpecification)
                .body(credentials)
                .post(LOGIN_COURIER)
                .then()
                .spec(responseSpecification);
    }

    @Override
    public ValidatableResponse deleteCourierById(String id) {
        return given()
                .spec(requestSpecification)
                .delete(DELETE_COURIER + id)
                .then()
                .spec(responseSpecification);
    }

        @Override
        public ValidatableResponse createOrder(Order order) {
            return given()
                    .spec(requestSpecification)
                    .post(ORDERS)
                    .then()
                    .spec(responseSpecification);
        }

    @Override
    public ValidatableResponse cancelOrder(String trackCredentials) {
        return given()
                .spec(requestSpecification)
                .queryParam("track", trackCredentials)
                .put(CANCEL_ORDER)
                .then()
                .spec(responseSpecification);
    }
    @Override
    public ValidatableResponse getListOrders() {
        return given()
                .spec(requestSpecification)
                .get(ORDERS)
                .then()
                .spec(responseSpecification);
    }
}
