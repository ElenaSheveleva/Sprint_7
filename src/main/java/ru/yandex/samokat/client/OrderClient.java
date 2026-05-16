package ru.yandex.samokat.client;

import io.restassured.response.Response;
import ru.yandex.samokat.models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private static final String ORDERS_PATH = "/api/v1/orders";

    public Response createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDERS_PATH);
    }

    public Response getOrdersList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDERS_PATH);
    }

    public Response cancelOrder(int track) {
        return given()
                .spec(getBaseSpec())
                .body("{\"track\": " + track + "}")
                .when()
                .put("/api/v1/orders/cancel");
    }
}