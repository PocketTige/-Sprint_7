package ru.juli.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import ru.juli.practicum.client.ScooterServiceClientImpl;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.juli.practicum.constants.URL.REQUEST_SPECIFICATION;
import static ru.juli.practicum.constants.URL.RESPONSE_SPECIFICATION;

public class GetListOrdersTest{
    ScooterServiceClientImpl client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);
    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверяем получение списка заказов, ожидаем список заказов и код ответа 200")
    public void getListOrders() {
        ValidatableResponse response = client.getListOrders();
        response.assertThat().statusCode(200).and().assertThat().body("orders", notNullValue());
    }
}
