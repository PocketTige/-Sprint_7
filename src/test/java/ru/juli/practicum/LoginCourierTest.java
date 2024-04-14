package ru.juli.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Test;
import ru.juli.practicum.client.Courier;
import ru.juli.practicum.client.Credentials;
import ru.juli.practicum.client.ScooterServiceClientImpl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.juli.practicum.client.CourierSteps.createCourier;
import static ru.juli.practicum.client.CourierSteps.loginCourier;
import static ru.juli.practicum.constants.URL.REQUEST_SPECIFICATION;
import static ru.juli.practicum.constants.URL.RESPONSE_SPECIFICATION;


public class LoginCourierTest {
    private int id;
    Courier courier;
    ScooterServiceClientImpl client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);

    @Test
    @DisplayName("courier log in") // имя теста логин курьера
    @Description("The courier can log in") // описание теста курьер может авторизоваться
    public void LoginCourierTest() {
        Courier courier = Courier.create("Tandjirotest999", "1234", "Komado"); // создаем курьера
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse loginResponse = loginCourier(courier);
        loginResponse.assertThat().statusCode(200);
        id = loginResponse.extract().body().jsonPath().getInt(String.valueOf("id"));
    }

    @Test
    @DisplayName("courier log in Required") // имя теста логин курьера обязательные поля
    @Description("to authorize, you need to pass all the required fields") // описание теста для авторизации нужно передать все обязательные поля
    public void LoginCourierRequiredTest() {
        courier = Courier.create("Tandjirotest999", "1234", "Komado"); // создаем курьера
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse loginResponse = client.login(new Credentials("", "1234"));
        loginResponse.assertThat().statusCode(400);
        ValidatableResponse loginResponse2 = loginCourier(courier);
        loginResponse2.assertThat().statusCode(200);
        id = loginResponse2.extract().body().jsonPath().getInt(String.valueOf("id"));
    }


    @Test
    @DisplayName("courier login error") // имя теста
    @Description("the system will return an error if the username or password is incorrect. login") // описание теста система вернёт ошибку, если неправильно указать логин
    public void LoginCourierIncorrectTest() {
        courier = Courier.create("Tandjirotest999", "1234", "Komado"); // создаем курьера
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse loginResponse = client.login(new Credentials("Tadjiro12345", "0000"));
        loginResponse.assertThat().statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
        ValidatableResponse loginResponse2 = loginCourier(courier);
        id = loginResponse2.extract().body().jsonPath().getInt(String.valueOf("id"));
    }

    @Test
    @DisplayName("courier password error") // имя теста
    @Description("the system will return an error if the username or password is incorrect. password") // описание теста система вернёт ошибку, если неправильно указать пароль
    public void LoginCourierRequiredErrorTest() {
        courier = Courier.create("Tandjirotest999", "1234", "Komado"); // создаем курьера
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse loginResponse = client.login(new Credentials("Tadjiro33033", ""));
        loginResponse.assertThat().statusCode(400);
        ValidatableResponse loginResponse2 = loginCourier(courier);
        id = loginResponse2.extract().body().jsonPath().getInt(String.valueOf("id"));

    }

    @Test
    @DisplayName("courier None existent") // имя теста
    @Description("if you log in under a non-existent user, the request returns an error") // описание теста если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
    public void LoginCourierNonexistentTest() {
        ValidatableResponse loginResponse = client.login(new Credentials("Nonexistenttest", "1234"));
        loginResponse.assertThat().statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("courier log in Respose id") // имя теста
    @Description("a successful request returns the id") // описание теста успешный запрос возвращает id
    public void CreateNewCourierCodeOkTest() {
        courier = Courier.create("Tandjirotest999", "1234", "Komado"); // создаем курьера
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse loginResponse = loginCourier(courier);
        loginResponse.assertThat().statusCode(200).and().assertThat().body("id", notNullValue());
        id = loginResponse.extract().body().jsonPath().getInt(String.valueOf("id"));

    }
    @After
    public void cleanUp() {
        if (id != 0) {
            client.deleteCourierById(String.valueOf(id)); // удаляем созданного курьера
        }
    }
}
