package ru.juli.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.juli.practicum.client.Courier;
import ru.juli.practicum.client.ScooterServiceClientImpl;
import ru.juli.practicum.courier.CourierGenerator;
import ru.juli.practicum.courier.CourierSteps;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.juli.practicum.constants.URL.REQUEST_SPECIFICATION;
import static ru.juli.practicum.constants.URL.RESPONSE_SPECIFICATION;
import static ru.juli.practicum.courier.CourierSteps.createCourier;
import static ru.juli.practicum.courier.CourierSteps.loginCourier;


public class LoginCourierTest {
    private int id;
    Courier courier;
    ScooterServiceClientImpl client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);
    protected final CourierGenerator courierGenerator = new CourierGenerator();
    private CourierSteps courierSteps;
    @Before
    @Step("Создание тестовых данных курьера") // Создание тестовых данных курьера
    public void setUp() {
        courierSteps = new CourierSteps();
        courier = courierGenerator.createNewUnicCourier();
    }

    @Test
    @DisplayName("courier log in") // имя теста логин курьера
    @Description("The courier can log in") // описание теста курьер может авторизоваться
    public void LoginCourierTest() {
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
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse loginResponse = loginCourier(courier);
        loginResponse.assertThat().statusCode(200);
        id = loginResponse.extract().body().jsonPath().getInt(String.valueOf("id"));
        courier.setLogin(null); // заполнение login = null
        ValidatableResponse loginResponse2 = loginCourier(courier); // Попытка авторизации c login = null
        loginResponse2.assertThat().statusCode(400);
    }

    @Test
    @DisplayName("courier password error") // имя теста
    @Description("the system will return an error if the username or password is incorrect. password") // описание теста система вернёт ошибку, если неправильно указать пароль
    public void LoginCourierRequiredErrorTest() {
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse currentResponse = loginCourier(courier); // логинимся с правильным паролем чтобы потом получить id для удаления
        id = currentResponse.extract().body().jsonPath().getInt(String.valueOf("id"));
        String currentPassword = courier.getPassword(); // Получаем текущий пароль и изменяем его
        courier.setPassword(currentPassword + "1");  // Добавляем символ к пароль, делая его невалидным
        ValidatableResponse loginResponse = loginCourier(courier);
        loginResponse.assertThat().statusCode(404);
    }

    @Test
    @DisplayName("courier None existent") // имя теста
    @Description("if you log in under a non-existent user, the request returns an error") // описание теста если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
    public void LoginCourierNonexistentTest() {
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse currentResponse = loginCourier(courier); // логинимся с правильным логином чтобы потом получить id для удаления
        id = currentResponse.extract().body().jsonPath().getInt(String.valueOf("id"));
        String currentLogin = courier.getLogin(); // Получаем текущий логин и изменяем его
        courier.setLogin(currentLogin + "nonexistent");  // Добавляем символы к логину, делая его несуществующим невалидным
        ValidatableResponse loginResponse = loginCourier(courier);
        loginResponse.assertThat().statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("courier log in Respose id") // имя теста
    @Description("a successful request returns the id") // описание теста успешный запрос возвращает id
    public void CreateNewCourierCodeOkTest() {
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse loginResponse = loginCourier(courier);
        loginResponse.assertThat().statusCode(200).and().assertThat().body("id", notNullValue());
        id = loginResponse.extract().body().jsonPath().getInt(String.valueOf("id"));

    }
    @After
    @Step("удаление данных курьера") // удаление данных курьера
    public void cleanUp() {
        if (id != 0) {
            client.deleteCourierById(String.valueOf(id)); // удаляем созданного курьера
        }
    }
}
