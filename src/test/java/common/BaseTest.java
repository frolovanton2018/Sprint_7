package common;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class BaseTest {

    protected static final String BASE_URL = "https://qa-scooter.education-services.ru";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    protected void deleteCourier(String id) {
        if (id == null) {
            return;
        }
        try {
            given()
                    .contentType(ContentType.JSON)
                    .body("{\"id\": \"" + id + "\"}")
                    .when()
                    .delete(BASE_URL + "/api/v1/courier/" + id)
                    .then()
                    .statusCode(200);
        } catch (Exception e) {
            // Курьер мог быть уже удалён или не существовать
        }
    }

    protected String createCourier(String login, String password) {
        CourierRequest createRequest = new CourierRequest(login, password, "Anton");
        return given()
                .header("Content-Type", "application/json")
                .body(createRequest)
                .when()
                .post(BASE_URL + "/api/v1/courier")
                .then()
                .extract().path("id");
    }

    protected void loginCourier(String login, String password) {
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, password);
        given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post(BASE_URL + "/api/v1/courier/login");
    }

}
