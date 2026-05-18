package ru.yandex.samokat.generator;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.samokat.models.Order;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final List<String> METRO_STATIONS = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    public static Order getRandomOrder() {
        return new Order(
                RandomStringUtils.randomAlphabetic(6),
                RandomStringUtils.randomAlphabetic(8),
                RandomStringUtils.randomAlphanumeric(15),
                METRO_STATIONS.get((int) (Math.random() * METRO_STATIONS.size())),
                "+7" + RandomStringUtils.randomNumeric(10),
                1 + (int) (Math.random() * 7),
                LocalDate.now().plusDays(1 + (int) (Math.random() * 30)).format(DATE_FORMATTER),
                RandomStringUtils.randomAlphanumeric(20),
                null
        );
    }

    public static Order getOrderWithBlackColor() {
        Order order = getRandomOrder();
        order.setColor(Collections.singletonList("BLACK"));
        return order;
    }

    public static Order getOrderWithGreyColor() {
        Order order = getRandomOrder();
        order.setColor(Collections.singletonList("GREY"));
        return order;
    }

    public static Order getOrderWithBothColors() {
        Order order = getRandomOrder();
        order.setColor(Arrays.asList("BLACK", "GREY"));
        return order;
    }

    public static Order getOrderWithoutColors() {
        Order order = getRandomOrder();
        order.setColor(null);
        return order;
    }
}