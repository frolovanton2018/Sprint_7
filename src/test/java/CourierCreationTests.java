import common.*;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CourierCreationTests extends BaseTest {

    private String createdCourierId = null;

    @AfterEach
    void cleanup() {
        deleteCourier(createdCourierId);
    }

    @Test
    @Description("курьера можно создать и успешный запрос возвращает ok: true;")
    void createCourier() {
        CourierRequest courier = CourierRequestBuilder.createWithRandomLogin();
        System.out.println(courier);

        createdCourierId = given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true))
                .extract().path("id");
    }

    @Test
    @Description("нельзя создать двух одинаковых курьеров")
    void cannotCreateDuplicateCourier() {
        String uniqueLogin = "AF_TEST_" + new java.util.Random().nextInt(1000);
        CourierRequest courier = CourierRequestBuilder.create(uniqueLogin);
        System.out.println(courier);

        // создаём курьера
        createdCourierId = given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true))
                .extract().path("id");

        // пробуем создать того же — ожидается 409
        given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and()
                .body("code", is(409));
    }


    @Test
    @Description("Валидация полей - пустой логин")
    void missingLoginReturnsError() {
        CourierRequest courier = new CourierRequest(
                "",
                "Secret123!",
                "Anton"
        );
        System.out.println(courier);

        given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @Description("Валидация полей - пустой пароль")
    void missingPasswordReturnsError() {
        CourierRequest courier = new CourierRequest(
                "AF_TEST_" + new java.util.Random().nextInt(1000),
                "",
                "Anton"
        );
        System.out.println(courier);

        given()
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

}
