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
import static org.junit.Assert.assertEquals;

public class CreateOrderAuthorizationTest {
    private OrdersHelper orderHelper;
    private User user;
    private UserHelper userHelper;
    private String accessToken;
    private int numberOfIngredients = 5;
    private HashMap<String, List> orderHash = new HashMap<>();
    private GetIngredientsData getOrderHashWithRandomElements = new GetIngredientsData();

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
    @DisplayName("Создание заказа без авторизации")
    public void createNewOrderWithoutTokenTest() {
        ValidatableResponse responseOrderWithoutToken = orderHelper.createOrderWithoutToken(orderHash);

        assertThat(responseOrderWithoutToken.extract().statusCode(), equalTo(200));
        assertTrue(responseOrderWithoutToken.extract().path("success"));
        assertNotNull(responseOrderWithoutToken.extract().path("order.number"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createNewOrderWithTokenTest() {
        ValidatableResponse responseOrderWithToken = orderHelper.createOrderWithToken(accessToken, orderHash);
        ArrayList<String> actualIngredientsList = responseOrderWithToken.extract().path("order.ingredients._id");

        assertThat(responseOrderWithToken.extract().statusCode(), equalTo(200));
        assertTrue(responseOrderWithToken.extract().path("success"));
        assertNotNull(responseOrderWithToken.extract().path("order.number"));
        assertEquals("Кол-во ингредиентов разное", numberOfIngredients, actualIngredientsList.size());
    }
}
