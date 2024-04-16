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
import static ru.juli.practicum.constants.URL.*;
import static ru.juli.practicum.courier.CourierSteps.createCourier;
import static ru.juli.practicum.courier.CourierSteps.loginCourier;

public class CreateCourierTest {
    private Courier courier;
    private ScooterServiceClientImpl client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);
    protected final CourierGenerator courierGenerator = new CourierGenerator();
    private int id;

    @Before
    @Step("Создание тестовых данных курьера") // Создание тестовых данных курьера
    public void setUp() {
        CourierSteps courierSteps = new CourierSteps();
        courier = courierGenerator.createNewUnicCourier();
    }

    @Test
    @DisplayName("create a courier") // имя теста успешное создание курьера со всеми полями
    @Description("you can create a courier") // описание теста  курьера можно создать
    public void CreateNewCourierTest() {
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201).and().assertThat().body("ok", equalTo(true));
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt("id");

    }

    @Test
    @DisplayName("create a courier repeat courier") // имя теста создание курьера с теми же самыми данными
    @Description("you cannot create two identical couriers") // описание теста нельзя создать двух одинаковых курьеров
    public void CreateNewCourierRepeatTest() {
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
        courier.setFirstName(null);
        ValidatableResponse response = createCourier(courier); // передаем только обязательные поля, а firstName = null
        response.assertThat().statusCode(201); // Первое создание курьера
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt("id");
        courier.setPassword(null);
        ValidatableResponse response2 = createCourier(courier); // передаем password = null firstName = null
        response2.assertThat().statusCode(400); // Попытка создать курьера с пустым паролем
    }

    @Test
    @DisplayName("Create New Courier Response") // имя теста создание курьера
    @Description("the request returns the correct response code") // описание теста запрос возвращает правильный код ответа
    public void CreateNewCourierCodeTest() {
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201);
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt(String.valueOf("id"));
    }

    @Test
    @DisplayName("Create New Courier Response ok: true") // имя теста создание курьера
    @Description("the request returns Response ok: true") // описание теста проверка что успешный запрос возвращает ok: true
    public void CreateNewCourierCodeOkTest() {
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(201).and().assertThat().body("ok", equalTo(true));
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt(String.valueOf("id"));
    }

    @Test
    @DisplayName("Create New Courier Response error") // имя теста создание курьера
    @Description("if one of the fields is missing, the request returns an error") // описание теста если одного из полей нет (логина), запрос возвращает ошибку с определенным текстом
    public void CreateNewCourierErrorTest() {
        courier.setLogin(null);
        ValidatableResponse response = createCourier(courier);
        response.assertThat().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Create New Courier Login Repeat") // имя теста создание курьера
    @Description("if you create a user with a username that already exists, an error is returned") // описание теста если создать пользователя с логином, который уже есть, возвращается ошибка
    public void CreateNewCourierLoginRepeat() {
        ValidatableResponse response = createCourier(courier); // Создание первого курьера
        response.assertThat().statusCode(201).and().assertThat().body("ok", equalTo(true));
        ValidatableResponse loginResponse = loginCourier(courier);
        id = loginResponse.extract().body().jsonPath().getInt("id");
        courier.getLogin();
        courier.setPassword("newpassword");
        courier.setFirstName("newfirstName");
        ValidatableResponse repeatCourier = createCourier(courier); // Создание курьера с тем же логином но другим паролем и именем
        repeatCourier.assertThat().statusCode(409).body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    @After
    @Step("удаление данных курьера") // удаление данных курьера
    public void cleanUp() {
        if (id != 0) {
            client.deleteCourierById(String.valueOf(id)); // удаляем созданного курьера
        }
    }
}

