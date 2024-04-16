package ru.juli.practicum.client;

import io.restassured.response.ValidatableResponse;

public interface ScooterServiceClient {
       ValidatableResponse createCourier(Courier courier);
       ValidatableResponse login(Credentials credentials);
       ValidatableResponse deleteCourierById(String id);
       ValidatableResponse createOrder(Order order);
       ValidatableResponse cancelOrder(String trackCredentials);
       ValidatableResponse getListOrders();
}
