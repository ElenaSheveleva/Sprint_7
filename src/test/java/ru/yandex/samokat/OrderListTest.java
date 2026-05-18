package ru.yandex.samokat;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.samokat.client.OrderClient;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class OrderListTest {
    private final OrderClient orderClient = new OrderClient();

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return orderClient.getOrdersList();
    }

    @Step("Проверка, что список заказов получен успешно")
    public void assertOrdersListSuccess(Response response) {
        response.then()
                .statusCode(SC_OK)
                .body("orders", notNullValue())
                .body("orders", instanceOf(List.class));
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что GET /api/v1/orders возвращает список заказов")
    public void getOrdersListTest() {
        Response response = getOrdersList();
        assertOrdersListSuccess(response);
    }
}
