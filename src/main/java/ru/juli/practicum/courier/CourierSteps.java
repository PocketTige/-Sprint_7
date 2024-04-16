package ru.juli.practicum.courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.juli.practicum.client.Courier;
import ru.juli.practicum.client.ScooterServiceClientImpl;

import static ru.juli.practicum.client.Credentials.fromCourier;
import static ru.juli.practicum.constants.URL.REQUEST_SPECIFICATION;
import static ru.juli.practicum.constants.URL.RESPONSE_SPECIFICATION;

public class CourierSteps {
    static ScooterServiceClientImpl client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);
    @Step("Create courier with login password firstName}") // создание курьера со всеми полями
    public static ValidatableResponse createCourier(Courier courier) {
        return client.createCourier(courier);
    }

    @Step("Login courier with with login password") // логин курьера со всеми полями
    public static ValidatableResponse loginCourier(Courier courier) {
        return client.login(fromCourier(courier));
    }
}