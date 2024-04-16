package ru.juli.practicum.courier;

import io.qameta.allure.Step;
import com.github.javafaker.Faker;
import ru.juli.practicum.client.Courier;

public class CourierGenerator {
    static Faker faker = new Faker();

    @Step("Создание нового курьера с рандомными данными") // Создание нового курьера с рандомными данными
    public Courier createNewUnicCourier() {
        String login = faker.name().username(); // Генерация логина курьера
        String password = faker.internet().password(4, 8, true, true); // Генерация пароля
        String firstName = faker.name().firstName(); // Генерация имени курьера
        // Возвращаем новый объект Courier
        return new Courier(login, password, firstName);
    }
}


//public class CourierGenerator {
//
//    public static void main(String[] args) {
//        Faker faker = new Faker();
//
//        String login = faker.name().username(); // Генерация логина курьера
//        String password = faker.internet().password(4, 8, true, true); // Генерация пароля
//        String firstName = faker.name().firstName(); // Генерация имени курьера
//
//        System.out.println("Login: " + login);
//        System.out.println("Password: " + password);
//        System.out.println("FirstName: " + firstName);
//    }
//}
