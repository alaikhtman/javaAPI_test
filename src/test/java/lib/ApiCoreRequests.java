package lib;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make POST-request to create user")
    public Response makePostRequestCreateUser(String url, Map<String, String> userData) {
        return given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
    }

}
