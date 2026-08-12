import common.*;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class CourierMakeOrderTests extends BaseTest {

    @ParameterizedTest
    @ValueSource(strings = {"BLACK", "GREY", "BLACK,GREY", ""})
    @Description("Создание заказа с разными цветами")
    void createOrderWithColors(String colorParam) {
        String[] colors = colorParam.isEmpty() ? new String[]{} : colorParam.split(",");

        OrderRequest request = new OrderRequest(
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

        OrderResponse response = given()
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/api/v1/orders")
                .then()
                .statusCode(201)
                .body("track", notNullValue())
                .extract().response().as(OrderResponse.class);

        System.out.println("Color: " + colorParam + " - track: " + response.getTrack());
    }
}
