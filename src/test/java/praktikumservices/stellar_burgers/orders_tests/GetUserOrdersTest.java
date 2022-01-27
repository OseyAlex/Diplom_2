package praktikumservices.stellar_burgers.orders_tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikumservices.stellar_burgers.entities.User;
import praktikumservices.stellar_burgers.helpers.OrdersHelper;
import praktikumservices.stellar_burgers.helpers.UserHelper;
import praktikumservices.stellar_burgers.scripts.GetIngredientsData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class GetUserOrdersTest {
    private OrdersHelper orderHelper;
    private User user;
    private UserHelper userHelper;
    private String accessToken;
    private HashMap<String, List> orderHash = new HashMap<>();
    private GetIngredientsData getOrderHashWithRandomElements = new GetIngredientsData();
    private int numberOfIngredients = 3;
    private int numberOfOrders = 3;


    @Before
    public void setUp() {
        user = User.getRequiredData();
        userHelper = new UserHelper();
        ValidatableResponse response = userHelper.registerNewUser(user);
        accessToken = response.extract().path("accessToken");
        orderHelper = new OrdersHelper();
        orderHash = getOrderHashWithRandomElements.getOrderHashWithIngredients(numberOfIngredients);
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @After
    public void tearDown() {
        userHelper.delete(accessToken);
    }

    @Test
    @DisplayName("Получение заказов пользователя с авторизацией")
    public void getAuthorizationUserOrdersTest() {
        for (int i = 1; i <= numberOfOrders; i++) {
            orderHelper.createOrderWithToken(accessToken, orderHash);
        }

        ValidatableResponse responseGetOrders = orderHelper.getOrdersByToken(accessToken);
        ArrayList<String> actualOrdersList = responseGetOrders.extract().path("orders._id");

        assertThat(responseGetOrders.extract().statusCode(), equalTo(200));
        assertTrue(responseGetOrders.extract().path("success"));
        assertEquals("Кол-во заказов разное", numberOfOrders, actualOrdersList.size());
    }

    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    public void getNotAuthorizationUserOrdersTest() {
        ValidatableResponse responseGetOrders = orderHelper.getOrdersWithoutToken();
        assertThat(responseGetOrders.extract().statusCode(), equalTo(401));
        assertFalse(responseGetOrders.extract().path("success"));
        assertEquals("Сообщение отличается", "You should be authorised", responseGetOrders.extract().path("message"));
    }
}
