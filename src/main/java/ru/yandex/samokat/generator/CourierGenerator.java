package ru.yandex.samokat.generator;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.samokat.models.Courier;

public class CourierGenerator {

    public static Courier getRandomCourier() {
        String login = RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphabetic(6);
        return new Courier(login, password, firstName);
    }

    public static Courier getCourierWithoutLogin() {
        String password = RandomStringUtils.randomAlphanumeric(8);
        String firstName = RandomStringUtils.randomAlphabetic(6);
        return new Courier(null, password, firstName);
    }

    public static Courier getCourierWithoutPassword() {
        String login = RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        String firstName = RandomStringUtils.randomAlphabetic(6);
        return new Courier(login, null, firstName);
    }

    public static Courier getCourierWithoutFirstName() {
        String login = RandomStringUtils.randomAlphanumeric(8).toLowerCase();
        String password = RandomStringUtils.randomAlphanumeric(8);
        return new Courier(login, password, null);
    }
}