package common;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BaseTest {

    protected static final String BASE_URL = "https://qa-scooter.education-services.ru";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Step("Удаление курьера по переданному id")
    protected void deleteCourier(String login, String password) {
        if (login == null) {
            return;
        }
        try {
            CourierLoginResponse response = loginCourier(login, password);
            String id = response.getId();
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

    @Step("Создание нового курьера")
    protected String createCourier(String login, String password) {
        CourierRequest createRequest = new CourierRequest(login, password, "Anton");
         given()
                .header("Content-Type", "application/json")
                .body(createRequest)
                .when()
                .post(BASE_URL + "/api/v2/courier");
                return null;
    }

    @Step("Авторизация курьера")
    protected CourierLoginResponse loginCourier(String login, String password) {
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, password);
        return given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post(BASE_URL + "/api/v1/courier/login")
                .then()
                .extract().response().as(CourierLoginResponse.class);
    }

    @Step("Удаление курьера по id")
    protected String deleteCourierById(String id) {
        DeleteCourierRequest request = new DeleteCourierRequest();
        request.id = id;
            given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .delete(BASE_URL + "/api/v1/courier/" + id);
        return null;
    }

}
