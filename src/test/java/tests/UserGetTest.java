package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Epic("Get cases")
@Feature("Get user")
@Link("https://example.org")
public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();


    @Test
    @Description("This test checks get existing user without Auth Headers and Cookie")
    @DisplayName("Test  get user: without Auth")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUsersDataNotAuth() {
        Response responseUserData = apiCoreRequests.makeGetUserRequest("https://playground.learnqa.ru/api/user/2");

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNoField(responseUserData, "firstName");
        Assertions.assertJsonHasNoField(responseUserData, "lastName");
        Assertions.assertJsonHasNoField(responseUserData, "email");
    }

    @Test
    @Description("This test checks get existing user with same Auth Headers and Cookie")
    @DisplayName("Test  get user: with same Auth")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUsersDataAsSameUser() {
        Map<String, String> authData = DataGenerator.getAuthData("vinkotov@example.com", "1234");
        Response userLogin = apiCoreRequests.makeUserLogin("https://playground.learnqa.ru/api/user/login", authData);

        String header = userLogin.getHeader("x-csrf-token");
        String cookie = userLogin.getCookie("auth_sid");


        Response responseUserData = apiCoreRequests.makeGetUserRequestWithTokenAndCookie("https://playground.learnqa.ru/api/user/2", header, cookie);

        String[] expectedFields = {"username", "firstName", "lastName", "email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);

    }

    @Test
    @Description("This test checks get existing user with  other Auth Headers and Cookie")
    @DisplayName("Test  get user: with other Auth")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUsersDataAsOtherUser() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        apiCoreRequests
                .makePostRequestCreateUser("https://playground.learnqa.ru/api/user/", userData);

        Map<String, String> authData = DataGenerator.getAuthData(userData.get("email"), userData.get("password"));
        Response userLogin = apiCoreRequests.makeUserLogin("https://playground.learnqa.ru/api/user/login", authData);

        String header = userLogin.getHeader("x-csrf-token");
        String cookie = userLogin.getCookie("auth_sid");

        Response responseUserData = apiCoreRequests.makeGetUserRequestWithTokenAndCookie("https://playground.learnqa.ru/api/user/2", header, cookie);

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNoField(responseUserData, "firstName");
        Assertions.assertJsonHasNoField(responseUserData, "lastName");
        Assertions.assertJsonHasNoField(responseUserData, "email");

    }


}
