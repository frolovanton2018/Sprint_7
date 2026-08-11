import common.*;

import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLogInTests extends BaseTest {

    private String createdCourierId = null;

    @AfterEach
    void cleanup() {
        deleteCourier(createdCourierId);
    }

    @Test
    @Description("Курьер может авторизоваться и успешный запрос возвращает id")
    void courierCanLogin() {
        String login = "AF_TEST_" + new java.util.Random().nextInt(1000);
        String password = "Secret123!";

        // Создаём курьера
        createdCourierId = createCourier(login, password);
        System.out.println("Created login: " + login);

        // Проверяем успешный логин
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, password);
        given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @Description("Запрос без login и password — возвращает 400")
    void loginWithoutFieldsReturns400() {
        CourierLoginRequest loginRequest = new CourierLoginRequest("", "");

        given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Запрос только с login — возвращает 400")
    void loginWithoutPasswordReturns400() {
        CourierLoginRequest loginRequest = new CourierLoginRequest("ninja", "");

        given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Запрос только с password — возвращает 400")
    void loginWithoutLoginReturns400() {
        CourierLoginRequest loginRequest = new CourierLoginRequest("", "1234");

        given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @Description("Неправильный логин или пароль — возвращает 404")
    void wrongLoginOrPasswordReturns404() {
        String login = "nonexistent_user_" + new java.util.Random().nextInt(1000);
        String password = "WrongPassword";

        CourierLoginRequest loginRequest = new CourierLoginRequest(login, password);

        given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Правильный логин, неправильный пароль — возвращает 404")
    void correctLoginWrongPasswordReturns404() {
        String login = "AF_TEST_" + new java.util.Random().nextInt(1000);
        String password = "Secret123!";

        // Создаём курьера
        createdCourierId = createCourier(login, password);
        System.out.println("Created login: " + login);
        CourierLoginRequest loginRequest = new CourierLoginRequest(login, "WrongPassword123");

        given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Неправильный логин, правильный пароль")
    void WrongLoginСorrectPasswordReturns404() {
        String login = "AF_TEST_" + new java.util.Random().nextInt(1000);
        String password = "Secret123!";

        // Создаём курьера
        createdCourierId = createCourier(login, password);
        System.out.println("Created login: " + login);
        CourierLoginRequest loginRequest = new CourierLoginRequest("WrongLogin123", password);

        given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @Description("Попытка авторизации с несуществующим пользователем")
    void NonExistentUserReturn404() {
        String login = "Random_User_123456789" + System.currentTimeMillis();
        String password = "SomePassword";

        CourierLoginRequest loginRequest = new CourierLoginRequest(login, password);

        given()
                .header("Content-Type", "application/json")
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
