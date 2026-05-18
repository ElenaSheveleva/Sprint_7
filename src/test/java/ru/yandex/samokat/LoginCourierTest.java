package ru.yandex.samokat;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.samokat.client.CourierClient;
import ru.yandex.samokat.generator.CourierGenerator;
import ru.yandex.samokat.models.Courier;
import ru.yandex.samokat.models.LoginCredentials;

import static org.apache.http.HttpStatus.*;
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

    @Step("Логин курьера с credentials: {credentials.login}")
    public Response loginCourier(LoginCredentials credentials) {
        return courierClient.loginCourier(credentials);
    }

    @Step("Проверка успешного логина")
    public void assertLoginSuccess(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Step("Проверка ошибки при неверных учётных данных")
    public void assertNotFoundError(Response response) {
        response.then()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Проверка ошибки при отсутствии обязательного поля")
    public void assertBadRequestError(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Удаление курьера после теста")
    public void deleteCourier() {
        if (courierId > 0) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Логин курьера: успешный вход")
    @Description("Проверка, что курьер может авторизоваться и возвращается id")
    public void loginCourierSuccessTest() {
        LoginCredentials credentials = new LoginCredentials(courier.getLogin(), courier.getPassword());
        Response response = loginCourier(credentials);
        assertLoginSuccess(response);
    }

    @Test
    @DisplayName("Логин курьера: неверный пароль")
    @Description("Проверка, что при неверном пароле возвращается ошибка 404")
    public void loginWithWrongPasswordTest() {
        LoginCredentials credentials = new LoginCredentials(courier.getLogin(), "wrong_password");
        Response response = loginCourier(credentials);
        assertNotFoundError(response);
    }

    @Test
    @DisplayName("Логин курьера: несуществующий логин")
    @Description("Проверка, что при несуществующем логине возвращается ошибка 404")
    public void loginWithNonExistentLoginTest() {
        LoginCredentials credentials = new LoginCredentials("non_existent_login", "password123");
        Response response = loginCourier(credentials);
        assertNotFoundError(response);
    }

    @Test
    @DisplayName("Логин курьера: без логина")
    @Description("Проверка, что запрос без логина возвращает ошибку 400")
    public void loginWithoutLoginTest() {
        LoginCredentials credentials = new LoginCredentials(null, courier.getPassword());
        Response response = loginCourier(credentials);
        assertBadRequestError(response);
    }

    @Test
    @DisplayName("Логин курьера: без пароля")
    @Description("Проверка, что запрос без пароля возвращает ошибку 400")
    public void loginWithoutPasswordTest() {
        LoginCredentials credentials = new LoginCredentials(courier.getLogin(), "");
        Response response = loginCourier(credentials);
        assertBadRequestError(response);
    }

    @After
    public void tearDown() {
        deleteCourier();
    }
}
