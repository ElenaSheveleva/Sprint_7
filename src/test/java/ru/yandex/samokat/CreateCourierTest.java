package ru.yandex.samokat;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.samokat.client.CourierClient;
import ru.yandex.samokat.generator.CourierGenerator;
import ru.yandex.samokat.models.Courier;

import static org.hamcrest.Matchers.*;

public class CreateCourierTest {
    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Создание курьера: успешный сценарий")
    @Description("Проверка, что курьера можно создать, ответ содержит ok: true и код 201")
    public void createCourierSuccessTest() {
        courier = CourierGenerator.getRandomCourier();
        Response response = courierClient.createCourier(courier);

        response.then()
                .statusCode(201)
                .body("ok", is(true));

        courierId = courierClient.getCourierId(courier);
    }

    @Test
    @DisplayName("Создание курьера: нельзя создать двух одинаковых курьеров")
    @Description("Проверка, что при попытке создать дубликат возвращается ошибка 409")
    public void createDuplicateCourierTest() {
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);

        Response duplicateResponse = courierClient.createCourier(courier);

        duplicateResponse.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        courierId = courierClient.getCourierId(courier);
    }

    @Test
    @DisplayName("Создание курьера: без логина")
    @Description("Проверка, что запрос без логина возвращает ошибку 400")
    public void createCourierWithoutLoginTest() {
        courier = CourierGenerator.getCourierWithoutLogin();
        Response response = courierClient.createCourier(courier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера: без пароля")
    @Description("Проверка, что запрос без пароля возвращает ошибку 400")
    public void createCourierWithoutPasswordTest() {
        courier = CourierGenerator.getCourierWithoutPassword();
        Response response = courierClient.createCourier(courier);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        if (courierId != null && courierId > 0) {
            courierClient.deleteCourier(courierId);
        }
    }
}
