import common.GetOrdersResponse;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class GetAllOrdersTests {
    @Test
    @Description("Получение списка заказов - статус 200 OK")
    void getOrdersReturns200() {
        given()
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200);
    }

    @Test
    @Description("Список заказов содержит поле orders")
    void getListOfOrders() {
        GetOrdersResponse response = given()
                .when()
                .get("/api/v1/orders")
                .then()
                .body("orders", notNullValue())
                .extract().response().as(GetOrdersResponse.class);

        System.out.println("Total orders: " + response.pageInfo.total);
        System.out.println("Orders count in response: " + response.orders.size());
    }

    @Test
    @Description("Список заказов содержит инфу о паганации")
    void getListOfOrdersContainsPageInfo() {
        GetOrdersResponse response = given()
                .when()
                .get("/api/v1/orders")
                .then()
                .body("pageInfo", notNullValue())
                .extract().response().as(GetOrdersResponse.class);

        System.out.println("Page: " + response.pageInfo.page);
        System.out.println("Total: " + response.pageInfo.total);
        System.out.println("Limit: " + response.pageInfo.limit);
    }
}
