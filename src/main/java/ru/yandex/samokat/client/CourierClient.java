package ru.yandex.samokat.client;

import io.restassured.response.Response;
import ru.yandex.samokat.models.Courier;
import ru.yandex.samokat.models.LoginCredentials;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class CourierClient extends BaseClient {
    private static final String COURIER_PATH = "/api/v1/courier";

    public Response createCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    public Response loginCourier(LoginCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(COURIER_PATH + "/login");
    }

    public Response loginCourier(Courier courier) {
        LoginCredentials credentials = new LoginCredentials(courier.getLogin(), courier.getPassword());
        return loginCourier(credentials);
    }

    public Response deleteCourier(int courierId) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }

    public Integer getCourierId(Courier courier) {
        try {
            return loginCourier(courier)
                    .then()
                    .statusCode(SC_OK)
                    .extract()
                    .path("id");
        } catch (Exception e) {
            return null;
        }
    }
}