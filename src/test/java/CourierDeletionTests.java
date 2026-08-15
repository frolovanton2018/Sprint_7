import common.*;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierDeletionTests extends BaseTest {

    @Test
    @DisplayName("Удаление курьера.Успешный запрос возвращает ok: true")
    @Description("Тест создаёт курьера, авторизуется, удаляет его и проверяет корректный ответ")
    void deleteCourierReturnsOk() {
        String login = "AF_TEST_" + new java.util.Random().nextInt(1000000);
        String password = "Secret123!";

        // Создаём курьера
        createCourier(login, password);
        System.out.println("Created login: " + login);

        // Авторизуемся
        CourierLoginResponse loginResponse = loginCourier(login, password);
        String userId = loginResponse.getId();
        System.out.println("Login response id: " + userId);

        // Удаляем по newCourierId
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(new DeleteCourierRequest(userId))
                .when()
                .delete(BASE_URL + "/api/v1/courier/" + userId)
                .then()
                .log().all()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Удаление курьера. Запрос без id - возвращает 400")
    @Step("Удаление курьера: отсутствует id")
    @Description("Тест проверяет что запрос без id возвращает код ответа 400")
    void deleteCourierWithoutIdReturns400() {
        String login = "AF_TEST_" + new java.util.Random().nextInt(1000000);
        String password = "Secret123!";

        // Создаём курьера
        createCourier(login, password);
        System.out.println("Created login: " + login);

        // Авторизуемся
        CourierLoginResponse loginResponse = loginCourier(login, password);
        String userId = loginResponse.getId();
        System.out.println("Login response id: " + userId);

        // Удаляем без id
        given()
                .contentType(ContentType.JSON)
                .body(new DeleteCourierRequest())
                .when()
                .delete(BASE_URL + "/api/v1/courier/")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для удаления курьера")); //(в реализации баг, по спецификации код статуса ответа должен быть 400, а отвечает 400)
    }

    @Test
    @DisplayName("Удаление курьера. Запрос с несуществующим id - возвращает 404")
    @Step("Удаление курьера: несуществующий id")
    @Description("Тест проверяет что запрос с несуществующим id - возвращает код ответа 404")
    void deleteCourierWithNonExistentIdReturns404() {
        String login = "AF_TEST_" + new java.util.Random().nextInt(1000000);
        String password = "Secret123!";

        // Создаём курьера
        createCourier(login, password);
        System.out.println("Created login: " + login);

        // Авторизуемся
        CourierLoginResponse loginResponse = loginCourier(login, password);
        String userId = loginResponse.getId();
        System.out.println("Login response id: " + userId);

        // Удаляем курьера (успешно)
        deleteCourierById(userId);

        // Пытаемся удалить повторно - ожидаем 404
        given()
                .contentType(ContentType.JSON)
                .body(new DeleteCourierRequest(userId))
                .when()
                .delete(BASE_URL + "/api/v1/courier/" + userId)
                .then()
                .statusCode(404)
                .body("message", equalTo("Курьера с таким id нет."));
    }

    @Test
    @DisplayName("Удаление курьера. Повторная попытка удалить уже удаленного курьрера")
    @Step("Удаление курьера: повторная попытка")
    @Description("Тест проверяет что удаление уже удаленного курьера - возвращает код ответа 404")
    void deleteAlreadyDeletedCourierReturns404() {
        String login = "AF_TEST_" + new java.util.Random().nextInt(1000000);
        String password = "Secret123!";

        // Создаём курьера
        createCourier(login, password);
        System.out.println("Created login: " + login);

        // Авторизуемся
        CourierLoginResponse loginResponse = loginCourier(login, password);
        String userId = loginResponse.getId();
        System.out.println("Login response id: " + userId);

        // Удаляем курьера (успешно)
        deleteCourierById(userId);

        // Пытаемся удалить повторно
        given()
                .contentType(ContentType.JSON)
                .body(new DeleteCourierRequest(userId))
                .when()
                .delete(BASE_URL + "/api/v1/courier/" + userId)
                .then()
                .statusCode(404)
                .body("message", equalTo("Курьера с таким id нет."));
    }
}
