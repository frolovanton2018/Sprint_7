import common.GetOrdersResponse;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetAllOrdersTests {
    @Test
    @Step("Получение списка заказов")
    @Description("Получение списка заказов - статус 200 OK")
    void getOrdersReturns200() {
        given()
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200);
    }

    @Test
    @Step("Проверка наличия полей orders")
    @Description("Список заказов содержит поле orders")
    void getListOfOrders() {
        GetOrdersResponse response = given()
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("orders", hasSize(greaterThan(0)))
                .extract().response().as(GetOrdersResponse.class);

        System.out.println("Total orders: " + response.pageInfo.total);
        System.out.println("Orders count in response: " + response.orders.size());
    }

    @Test
    @Step("Проверка pageInfo")
    @Description("Список заказов содержит инфу о паганации")
    void getListOfOrdersContainsPageInfo() {
        GetOrdersResponse response = given()
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200)
                .body("pageInfo.page", notNullValue())
                .body("pageInfo.total", greaterThan(0))
                .body("pageInfo.limit", greaterThan(0))
                .extract().response().as(GetOrdersResponse.class);

        System.out.println("Page: " + response.pageInfo.page);
        System.out.println("Total: " + response.pageInfo.total);
        System.out.println("Limit: " + response.pageInfo.limit);
    }
}
