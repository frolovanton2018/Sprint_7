package common;

public class CourierRequestBuilder {

    public static CourierRequest createWithRandomLogin() {
        return new CourierRequest(
                "AF_TEST_" + new java.util.Random().nextInt(1000000),
                "Secret123!",
                "Anton"
        );
    }

    public static CourierRequest create(String login) {
        return new CourierRequest(login, "Secret123!", "Anton");
    }
}
