package praktikumservices.stellar_burgers.helpers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikumservices.stellar_burgers.RestAssuredSpecification;
import praktikumservices.stellar_burgers.entities.User;
import praktikumservices.stellar_burgers.entities.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserHelper extends RestAssuredSpecification {
    private static final String endpointUrl = "/api/auth/";

    @Step("Создать пользователя")
    public ValidatableResponse registerNewUser(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(endpointUrl + "register")
                .then();
    }

    @Step("Логин пользователя")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(userCredentials)
                .when()
                .post(endpointUrl + "login")
                .then();
    }

    @Step("Изменение пользователя с авторизацией")
    public ValidatableResponse userPatchWithToken(UserCredentials userCredentials, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .headers("Authorization", accessToken)
                .body(userCredentials)
                .when()
                .patch(endpointUrl + "user")
                .then();
    }

    @Step("Изменение пользователя без авторизации")
    public ValidatableResponse userPatchWithoutToken(UserCredentials userCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(userCredentials)
                .when()
                .patch(endpointUrl + "user")
                .then();
    }

    @Step("Удалить пользователя")
    public void delete(String accessToken) {
        given()
                .spec(getBaseSpec())
                .headers("Authorization", accessToken)
                .when()
                .delete(endpointUrl + "user")
                .then()
                .statusCode(202);
    }
}
