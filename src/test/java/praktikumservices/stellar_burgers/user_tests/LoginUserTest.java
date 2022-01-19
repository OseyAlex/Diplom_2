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
import praktikumservices.stellar_burgers.entities.UserCredentials;
import praktikumservices.stellar_burgers.helpers.UserHelper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class LoginUserTest {
    private UserHelper userHelper;
    private String accessToken;
    private String refreshToken;

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
    @DisplayName("Позитивный логин пользователя")
    public void loginUserTest() {
        User user = User.getRequiredData();
        userHelper.registerNewUser(user);
        ValidatableResponse response = userHelper.loginUser(UserCredentials.from(user));

        assertThat(response.extract().statusCode(), equalTo(200));
        assertTrue(response.extract().path("success"));
        assertEquals("Почта отличается", user.getEmail(), response.extract().path("user.email"));
        assertEquals("Имя отличается", user.getName(), response.extract().path("user.name"));
        assertNotNull("Токен пустой", response.extract().path("accessToken"));

        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Логин пользователя с неверным паролем")
    public void loginUserWithWrongDataTest() {
        User user = User.getRequiredData();
        ValidatableResponse response = userHelper.registerNewUser(user);
        accessToken = response.extract().path("accessToken");

        ValidatableResponse responseNegative = userHelper.loginUser(new UserCredentials(user.getEmail(), "wrong"));
        assertThat(responseNegative.extract().statusCode(), equalTo(401));
        assertFalse(responseNegative.extract().path("success"));
        assertEquals("Сообщение отличается", "email or password are incorrect", responseNegative.extract().path("message"));
    }
}
