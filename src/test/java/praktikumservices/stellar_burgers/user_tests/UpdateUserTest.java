package praktikumservices.stellar_burgers.user_tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import praktikumservices.stellar_burgers.entities.User;
import praktikumservices.stellar_burgers.entities.UserCredentials;
import praktikumservices.stellar_burgers.helpers.UserHelper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateUserTest {
    private UserHelper userHelper;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp() {
        userHelper = new UserHelper();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @DisplayName("Обновление информации о пользователе с токеном")
    public void updateUserDataWithTokenTest() {
        User user = User.getRequiredData();
        ValidatableResponse response = userHelper.registerNewUser(user);

        accessToken = response.extract().path("accessToken");
        User userUpdate = User.getRequiredData();

        ValidatableResponse responseUpdateUserData = userHelper.userPatchWithToken(UserCredentials.from(userUpdate), accessToken);

        assertThat(responseUpdateUserData.extract().statusCode(), equalTo(200));
        assertTrue(responseUpdateUserData.extract().path("success"));

        //Логин пользователя чтобы проверить возможность авторизации
        ValidatableResponse responseLoginWithUpdatedData = userHelper.loginUser(UserCredentials.from(userUpdate));
        assertThat(responseLoginWithUpdatedData.extract().statusCode(), equalTo(200));
        assertTrue(responseLoginWithUpdatedData.extract().path("success"));
    }

    @Test
    @DisplayName("Обновление информации о пользователе без токена")
    public void updateUserDataWithoutTokenTest() {
        User user = User.getRequiredData();
        userHelper.registerNewUser(user);

        User userUpdate = User.getRequiredData();

        ValidatableResponse responseUpdateUserData = userHelper.userPatchWithoutToken(UserCredentials.from(userUpdate));

        assertThat(responseUpdateUserData.extract().statusCode(), equalTo(401));
        assertEquals("Сообщение отличается", "You should be authorised", responseUpdateUserData.extract().path("message"));
    }
}
