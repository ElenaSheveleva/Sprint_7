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
import ru.yandex.samokat.models.LoginCredentials;

import static org.hamcrest.Matchers.*;

public class LoginCourierTest {
    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandomCourier();
        courierClient.createCourier(courier);
        courierId = courierClient.getCourierId(courier);
    }

    @Test
    @DisplayName("Логин курьера: успешный вход")
    @Description("Проверка, что курьер может авторизоваться и возвращается id")
    public void loginCourierSuccessTest() {
        LoginCredentials credentials = new LoginCredentials(courier.getLogin(), courier.getPassword());
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Логин курьера: неверный пароль")
    @Description("Проверка, что при неверном пароле возвращается ошибка 404")
    public void loginWithWrongPasswordTest() {
        LoginCredentials credentials = new LoginCredentials(courier.getLogin(), "wrong_password");
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера: несуществующий логин")
    @Description("Проверка, что при несуществующем логине возвращается ошибка 404")
    public void loginWithNonExistentLoginTest() {
        LoginCredentials credentials = new LoginCredentials("non_existent_login", "password123");
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Логин курьера: без логина")
    @Description("Проверка, что запрос без логина возвращает ошибку 400")
    public void loginWithoutLoginTest() {
        LoginCredentials credentials = new LoginCredentials(null, courier.getPassword());
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Логин курьера: без пароля")
    @Description("Проверка, что запрос без пароля возвращает ошибку 400")
    public void loginWithoutPasswordTest() {
        LoginCredentials credentials = new LoginCredentials(courier.getLogin(), "");
        Response response = courierClient.loginCourier(credentials);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void tearDown() {
        if (courierId > 0) {
            courierClient.deleteCourier(courierId);
        }
    }
}