package praktikumservices.stellar_burgers.orders_tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikumservices.stellar_burgers.entities.User;
import praktikumservices.stellar_burgers.helpers.OrdersHelper;
import praktikumservices.stellar_burgers.helpers.UserHelper;
import praktikumservices.stellar_burgers.scripts.GetIngredientsData;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateOrderVariousIngredientsTest {
    private OrdersHelper orderHelper;
    private static GetIngredientsData getOrderHash = new GetIngredientsData();
    private int expectedStatusCode;
    private boolean isOrderCreated;
    private String accessToken;
    private String expectedErrorMessage;
    private HashMap<String, List> orderHash;
    private UserHelper userHelper;
    private User user;


    public CreateOrderVariousIngredientsTest(HashMap<String, List> orderHash, int expectedStatusCode, boolean isOrderCreated, String expectedErrorMessage) {
        this.orderHash = orderHash;
        this.expectedStatusCode = expectedStatusCode;
        this.isOrderCreated = isOrderCreated;
        this.expectedErrorMessage = expectedErrorMessage;
    }

    @Before
    public void setUp() {
        orderHelper = new OrdersHelper();
        user = User.getRequiredData();
        userHelper = new UserHelper();
        ValidatableResponse response = userHelper.registerNewUser(user);
        accessToken = response.extract().path("accessToken");
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @After
    public void tearDown() {
        userHelper.delete(accessToken);
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {getOrderHash.getEmptyOrderHash(), 400, false, "Ingredient ids must be provided"},
                {getOrderHash.getOrderHashWithWrongHash(), 500, false, ""}
        };
    }

    @Test
    @DisplayName("Создаём заказ с пустым списком ингредиентов и с неправильным id")
    public void createOrderVariousOrderHashTest() {
        ValidatableResponse response = orderHelper.createOrderWithToken(accessToken, orderHash);
        assertThat(response.extract().statusCode(), equalTo(expectedStatusCode));
        if (expectedStatusCode != 500) {
            String actualErrorMessage = response.extract().path("message");
            assertEquals("Сообщение отличается", expectedErrorMessage, actualErrorMessage);
            assertThat(response.extract().path("success"), equalTo(isOrderCreated));
        }
    }
}