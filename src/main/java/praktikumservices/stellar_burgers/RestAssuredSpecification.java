package praktikumservices.stellar_burgers;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestAssuredSpecification {
    private static final String baseUri = "https://stellarburgers.nomoreparties.site/";

    public RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(baseUri)
                .build();
    }
}
