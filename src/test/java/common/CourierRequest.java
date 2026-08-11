package common;

public class CourierRequest {
    public final String login;
    public final String password;
    public final String firstName;

    public CourierRequest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "CourierRequest{login='" + login + "', password='" + password + "', firstName='" + firstName + "'}";
    }
}
