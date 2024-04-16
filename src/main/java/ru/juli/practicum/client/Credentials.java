package ru.juli.practicum.client;

public class Credentials {
    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    private final String login;

    private final String password;

    public static Credentials fromCourier(Courier courier) {
        return new Credentials(courier.getLogin(), courier.getPassword());
    }

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
