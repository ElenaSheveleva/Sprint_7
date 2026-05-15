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

    public Response getOrderByTrack(int track) {
        return given()
                .spec(getBaseSpec())
                .queryParam("t", track)
                .when()
                .get(ORDERS_PATH + "/track");
    }

    public Response acceptOrder(int orderId, int courierId) {
        return given()
                .spec(getBaseSpec())
                .queryParam("courierId", courierId)
                .when()
                .put(ORDERS_PATH + "/accept/" + orderId);
    }
}
