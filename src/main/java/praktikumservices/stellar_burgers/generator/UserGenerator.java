package praktikumservices.stellar_burgers.generator;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

import java.util.Locale;

public class UserGenerator {

    @Step("Сгенерировать Email")
    public static String getEmail() {
        Faker faker = new Faker();
        String email = faker.bothify("????##@gmail.com");
        return email;
    }

    @Step("Сгенерировать Password")
    public static String getPassword() {
        Faker faker = new Faker();
        String password = faker.bothify("????##????");
        return password;
    }

    @Step("Сгенерировать Имя")
    public static String getName() {
        Faker faker = new Faker(Locale.forLanguageTag("ru"));
        String name = faker.name().username();
        return name;
    }
}
