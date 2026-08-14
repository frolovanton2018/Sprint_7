import common.*;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CourierPlaceOrderTests extends BaseTest {

    @ParameterizedTest
    @Step("Создание заказа: цвета {color}")
    @ValueSource(strings = {"BLACK", "GREY", "BLACK,GREY", ""})
    @Description("Проверка создания заказа с разными цветами")
    void createOrderWithColors(String colorParam) {
        String[] colors = colorParam.isEmpty() ? new String[]{} : colorParam.split(",");

        PostOrdersRequest request = new PostOrdersRequest(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                colors
        );

        PostOrdersResponse response = given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract().response().as(PostOrdersResponse.class);

        System.out.println("Color: " + colorParam + " - track: " + response.getTrack());
    }
}
