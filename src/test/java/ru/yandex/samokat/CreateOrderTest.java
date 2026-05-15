package ru.yandex.samokat;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.samokat.client.OrderClient;
import ru.yandex.samokat.generator.OrderGenerator;
import ru.yandex.samokat.models.Order;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final Order order;
    private final String testName;
    private final OrderClient orderClient;

    public CreateOrderTest(Order order, String testName) {
        this.order = order;
        this.testName = testName;
        this.orderClient = new OrderClient();
    }

    @Parameterized.Parameters(name = "{1}")
    public static Object[][] getOrders() {
        return new Object[][]{
                {OrderGenerator.getOrderWithBlackColor(), "Только BLACK цвет"},
                {OrderGenerator.getOrderWithGreyColor(), "Только GREY цвет"},
                {OrderGenerator.getOrderWithBothColors(), "Оба цвета BLACK и GREY"},
                {OrderGenerator.getOrderWithoutColors(), "Без указания цвета"}
        };
    }

    @Test
    @DisplayName("Создание заказа с разными вариантами цвета")
    @Description("Параметризованный тест проверяет, что заказ создаётся с BLACK, GREY, обоими цветами и без цвета")
    public void createOrderTest() {
        Response response = orderClient.createOrder(order);

        response.then()
                .statusCode(201)
                .body("track", notNullValue())
                .body("track", greaterThan(0));
    }
}