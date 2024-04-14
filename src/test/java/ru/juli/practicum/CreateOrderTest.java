package ru.juli.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.juli.practicum.client.Order;
import ru.juli.practicum.client.ScooterServiceClientImpl;
import ru.juli.practicum.client.TrackCredentials;
import java.util.List;
import static ru.juli.practicum.constants.URL.REQUEST_SPECIFICATION;
import static ru.juli.practicum.constants.URL.RESPONSE_SPECIFICATION;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    Order order;
    TrackCredentials trackCredentials;
    private int trackNew;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final List<String> color;

    ScooterServiceClientImpl client = new ScooterServiceClientImpl(REQUEST_SPECIFICATION, RESPONSE_SPECIFICATION);
    public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {8}")
    public static Object[] data() {
        return new Object[][]{
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2024-04-06", "Saske, come back to Konoha", null},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2024-04-06", "Saske, come back to Konoha", List.of("BLACK")},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2024-04-06", "Saske, come back to Konoha", List.of("GREY")},
                {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2024-04-06", "Saske, come back to Konoha", List.of("BLACK", "GREY")}

        };
    }
    @After
    public void cleanUp() {
        if (trackNew != 0) {
            client.cancelOrder(String.valueOf(trackNew));
        }
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа со всеми цветми самока (параметризация)")
    public void createOrder() {
        order = Order.createOrder(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        ValidatableResponse orderResponse = client.createOrder(order);
        orderResponse.assertThat().statusCode(201);
        trackNew = orderResponse.extract().body().jsonPath().getInt(String.valueOf("track"));
    }
}
