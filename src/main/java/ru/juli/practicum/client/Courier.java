package ru.juli.practicum.client;

public class Courier {

    public static String getLogin(Object o) { return null;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public static String getFirstName(Object o) {
        return null;
    }

    public String getFirstName() {
        return firstName;
    }

    private final String login;
    private final String password;
    private final String firstName;
    public static Courier create(String login, String password, String firstName) {
        return new Courier(login, password, firstName);
    }
     private Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
}
