package lib;

import io.qameta.allure.Step;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make POST-request to create user")
    public Response makePostRequestCreateUser(String url, Map<String, String> userData) {
        return given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();
    }

    @Step("Make POST-request to create user and and return Json")
    public JsonPath makePostRequestCreateUserAsJson(String url, Map<String, String> userData) {
        return given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
    }

    @Step("Make GET-request to get user without auth token and cookies")
    public Response makeGetUserRequest(String url) {
        return get(url)
                .andReturn();
    }

    @Step("Make GET-request to get user with token and cookie")
    public Response makeGetUserRequestWithTokenAndCookie(String url, String token, String cookie) {
        return given()
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make GET-request to get user with token only")
    public Response makeGetUserRequestWithTokenOnly(String url, String token) {
        return given()
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make GET-request to get user with cookie only")
    public Response makeGetUserRequestWithCookie(String url, String cookie) {
        return given()
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make POST-request login user")
    public Response makeUserLogin(String url, Map<String, String> authData) {
        return given()
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make PUT-request edit user")
    public Response makeEditUser(String url, String token, String cookie, Map<String, String> editData) {
        return given()
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make DELETE-request edit user")
    public Response makeDeleteUser(String url, String token, String cookie) {
        return given()
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .delete(url)
                .andReturn();
    }


}
