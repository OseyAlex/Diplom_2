package praktikumservices.stellar_burgers.user_tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikumservices.stellar_burgers.entities.User;
import praktikumservices.stellar_burgers.helpers.UserHelper;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateUserWithoutDataTest {
    private User user;
    private int expectedStatusCode;
    private boolean isUserCreated;
    private String expectedErrorMessage;
    private UserHelper userHelper;

    public CreateUserWithoutDataTest(User user, int expectedStatusCode, boolean isUserCreated, String expectedErrorMessage) {
        this.user = user;
        this.expectedStatusCode = expectedStatusCode;
        this.isUserCreated = isUserCreated;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Before
    public void setUp() {
        userHelper = new UserHelper();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {User.getEmailAndPassData(), 403, false, "Email, password and name are required fields"},
                {User.getEmailAndName(), 403, false, "Email, password and name are required fields"},
                {User.getPasswordAndName(), 403, false, "Email, password and name are required fields"}
        };
    }

    @Test
    @DisplayName("Создаём 1 курьера c минимальным кол-вом обяз. полей")
    public void createNewUser() {
        ValidatableResponse response = userHelper.registerNewUser(user);

        int actualStatusCode = response.extract().statusCode();
        boolean isUserCreatedActual = response.extract().path("success");
        String actualErrorMessage = response.extract().path("message");

        assertThat(actualStatusCode, equalTo(expectedStatusCode));
        assertThat(isUserCreatedActual, equalTo(isUserCreated));
        assertEquals("Сообщение отличается", expectedErrorMessage, actualErrorMessage);
    }
}
