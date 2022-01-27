package praktikumservices.stellar_burgers.user_tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikumservices.stellar_burgers.entities.User;
import praktikumservices.stellar_burgers.helpers.UserHelper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class CreateUserTest {
    private UserHelper userHelper;
    private String accessToken;

    @Before
    public void setUp() {
        userHelper = new UserHelper();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @After
    public void tearDown() {
        userHelper.delete(accessToken);
    }

    @Test
    @DisplayName("Создать пользователя")
    public void createUserTest() {
        User user = User.getRequiredData();
        ValidatableResponse response = userHelper.registerNewUser(user);

        accessToken = response.extract().path("accessToken");

        assertThat(response.extract().statusCode(), equalTo(200));
        assertTrue(response.extract().path("success"));
        assertEquals("Email отличается", user.getEmail(), response.extract().path("user.email"));
        assertEquals("Имя пользователя отличается", user.getName(), response.extract().path("user.name"));
    }

    @Test
    @DisplayName("Создать одинакового пользователя 2 раза")
    public void createSameUserTest() {
        User user = User.getRequiredData();
        ValidatableResponse responsePositive = userHelper.registerNewUser(user);
        accessToken = responsePositive.extract().path("accessToken");

        ValidatableResponse responseNegative = new UserHelper().registerNewUser(user);

        assertThat(responseNegative.extract().statusCode(), equalTo(403));
        assertFalse(responseNegative.extract().path("success"));
        assertEquals("Сообщение отличается", "User already exists", responseNegative.extract().path("message"));
    }
}
