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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {
    private CourierClient courierClient;
    private Courier currentCourier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return courierClient.createCourier(courier);
    }

    @Step("Проверка успешного создания курьера")
    public void assertCreateSuccess(Response response) {
        response.then()
                .statusCode(SC_CREATED)
                .body("ok", is(true));
    }

    @Step("Проверка ошибки при дубликате курьера")
    public void assertDuplicateError(Response response) {
        response.then()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Проверка ошибки при отсутствии обязательного поля")
    public void assertBadRequestError(Response response) {
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Удаление курьера после теста")
    public void deleteCourierIfExists() {
        if (currentCourier != null) {
            try {
                Integer id = courierClient.getCourierId(currentCourier);
                if (id != null && id > 0) {
                    courierClient.deleteCourier(id);
                }
            } catch (Exception e) {
                // Курьер не был создан или сервер недоступен — ничего не удаляем
            }
        }
    }

    @Test
    @DisplayName("Создание курьера: успешный сценарий")
    @Description("Проверка, что курьера можно создать, ответ содержит ok: true и код 201")
    public void createCourierSuccessTest() {
        currentCourier = CourierGenerator.getRandomCourier();
        Response response = createCourier(currentCourier);
        assertCreateSuccess(response);
    }

    @Test
    @DisplayName("Создание курьера: нельзя создать двух одинаковых курьеров")
    @Description("Проверка, что при попытке создать дубликат возвращается ошибка 409")
    public void createDuplicateCourierTest() {
        currentCourier = CourierGenerator.getRandomCourier();
        createCourier(currentCourier);
        Response duplicateResponse = createCourier(currentCourier);
        assertDuplicateError(duplicateResponse);
    }

    @Test
    @DisplayName("Создание курьера: без логина")
    @Description("Проверка, что запрос без логина возвращает ошибку 400")
    public void createCourierWithoutLoginTest() {
        currentCourier = CourierGenerator.getCourierWithoutLogin();
        Response response = createCourier(currentCourier);
        assertBadRequestError(response);
        currentCourier = null;
    }

    @Test
    @DisplayName("Создание курьера: без пароля")
    @Description("Проверка, что запрос без пароля возвращает ошибку 400")
    public void createCourierWithoutPasswordTest() {
        currentCourier = CourierGenerator.getCourierWithoutPassword();
        Response response = createCourier(currentCourier);
        assertBadRequestError(response);
        currentCourier = null;
    }

    @After
    public void tearDown() {
        deleteCourierIfExists();
    }
}
