package praktikumservices.stellar_burgers.helpers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikumservices.stellar_burgers.entities.IngredientsData;
import praktikumservices.stellar_burgers.RestAssuredSpecification;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class OrdersHelper extends RestAssuredSpecification {
    private static final String endpointUrl = "/api/";

    @Step("Получить ингредиенты")
    public IngredientsData getIngredients() {
        return given()
                .spec(getBaseSpec())
                .get(endpointUrl + "ingredients")
                .body().as(IngredientsData.class);
    }

    @Step("Создать заказ без токена")
    public ValidatableResponse createOrderWithoutToken(HashMap ingredients) {
        return given()
                .spec(getBaseSpec())
                .body(ingredients)
                .when()
                .post(endpointUrl + "orders")
                .then();
    }

    @Step("Создать заказ с токеном")
    public ValidatableResponse createOrderWithToken(String accessToken, HashMap ingredients) {
        return given()
                .spec(getBaseSpec())
                .headers("Authorization", accessToken)
                .body(ingredients)
                .when()
                .post(endpointUrl + "orders")
                .then();
    }

    @Step("Получить заказы конкретного пользователя c авторизацией")
    public ValidatableResponse getOrdersByToken(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .headers("Authorization", accessToken)
                .when()
                .get(endpointUrl + "orders")
                .then();
    }

    @Step("Получить заказы конкретного пользователя без авторизации")
    public ValidatableResponse getOrdersWithoutToken() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(endpointUrl + "orders")
                .then();
    }
}
