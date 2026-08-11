package common;

public class CourierLoginRequest {
    public final String login;
    public final String password;

    public CourierLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
