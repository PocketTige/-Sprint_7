package ru.juli.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import ru.juli.practicum.client.Courier;
import ru.juli.practicum.client.ScooterServiceClientImpl;
import static org.hamcrest.Matchers.equalTo;
import static ru.juli.practicum.client.CourierSteps.createCourier;
import static ru.juli.practicum.client.CourierSteps.loginCourier;
import static ru.juli.practicum.constants.URL.*;

public class CreateCourierTest {
    Courier courier;
    ScooterServiceClientImpl client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);
    private int id;

    @Test
    @DisplayName("create a courier") // имя теста успешное создание курьера со всеми полями
    @Description("you can create a courier") // описание теста  курьера можно создать
    public void CreateNewCourierTest() {
        Courier courier = Courier.create("Tandjirotest007", "1234", "Komado");
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201).and().assertThat().body("ok", equalTo(true));
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt("id");

    }

    @Test
    @DisplayName("create a courier repeat login") // имя теста создание курьера с тем же логином
    @Description("you cannot create two identical couriers") // описание теста нельзя создать двух одинаковых курьеров
    public void CreateNewCourierRepeatTest() {
        Courier courier = Courier.create("Tandjirotest007", "1234", "Komado");
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201); // Первое создание курьера
        ValidatableResponse response2 = createCourier(courier);
        response2.assertThat().statusCode(409);; // Попытка создать того же курьера снова
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Create New Courier Required Test") // имя теста создание курьера обязательные поля
    @Description("to create a courier, you need to transfer all required fields to the handle") // описание теста чтобы создать курьера, нужно передать в ручку все обязательные поля;
    public void CreateNewCourierRequiredTest() {
        Courier courier = Courier.create("Tandjirotest007", "1234", null);
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201); // Первое создание курьера
        Courier courier2 = Courier.create("Tandjirotest008", null, null);
        ValidatableResponse response2 = createCourier(courier2);
        response2.assertThat().statusCode(400); // Попытка создать курьера с пустым паролем
        Courier courier3 = Courier.create(null, "1234", null);
        ValidatableResponse response3 = createCourier(courier3);
        response3.assertThat().statusCode(400); // Попытка создать курьера с пустым логином
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Create New Courier Response") // имя теста создание курьера
    @Description("the request returns the correct response code") // описание теста запрос возвращает правильный код ответа
    public void CreateNewCourierCodeTest() {
        Courier courier = Courier.create("Tandjirotest007", "1234", "Komado");
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt(String.valueOf("id"));
    }

    @Test
    @DisplayName("Create New Courier Response ok: true") // имя теста создание курьера
    @Description("the request returns Response ok: true") // описание теста проверка что успешный запрос возвращает ok: true
    public void CreateNewCourierCodeOkTest() {
        Courier courier = Courier.create("Tandjirotest007", "1234", "Komado"); // создаем курьера
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201).and().assertThat().body("ok", equalTo(true));
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt(String.valueOf("id"));
    }

    @Test
    @DisplayName("Create New Courier Response error") // имя теста создание курьера
    @Description("if one of the fields is missing, the request returns an error") // описание теста если одного из полей нет, запрос возвращает ошибку
    public void CreateNewCourierErrorTest() {
        Courier courier = Courier.create("Tandjirotest007", null, "Komado"); // создаем курьера
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Create New Courier Login Repeat Response error") // имя теста создание курьера
    @Description("if you create a user with a username that already exists, an error is returned") // описание теста если создать пользователя с логином, который уже есть, возвращается ошибка
    public void CreateNewCourierLoginRepeat() {
        courier = Courier.create("Tandjirotest007", "1234", "Komado"); // создаем курьера
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201).and().assertThat().body("ok", equalTo(true));
        Courier courier2 = Courier.create("Tandjirotest007", "4444", "Test"); // создаем курьера с тем же логином
        ValidatableResponse response2 = createCourier(courier2);
        response2.assertThat().statusCode(409).and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt(String.valueOf("id"));
    }
    @After
    public void cleanUp() {
        if (id != 0) {
            client.deleteCourierById(String.valueOf(id)); // удаляем созданного курьера
        }
    }
}

