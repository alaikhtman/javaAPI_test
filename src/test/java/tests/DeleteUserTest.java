package tests;

import io.qameta.allure.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Epic("Delete cases")
@Feature("Delete user")
@Link("https://example.org")
public class DeleteUserTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test checks impossible to delete user with id2")
    @DisplayName("Test unsuccessfully delete user: id2")
    @Severity(SeverityLevel.MINOR)
    public void testDeleteUsersId2() {
        Map<String, String> authData = DataGenerator.getAuthData("vinkotov@example.com", "1234");
        Response userLogin = apiCoreRequests.makeUserLogin("https://playground.learnqa.ru/api/user/login", authData);

        String header = userLogin.getHeader("x-csrf-token");
        String cookie = userLogin.getCookie("auth_sid");


        Response responseDeleteUser = apiCoreRequests.makeDeleteUser("https://playground.learnqa.ru/api/user/2", header, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
        Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");

    }

    @Test
    @Description("This test checks successfully deleting user")
    @DisplayName("Test successfully delete user")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteUsersDataWithSameUser() {
        //Create
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = apiCoreRequests
                .makePostRequestCreateUserAsJson("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateUser.getString("id");

        //Login
        Map<String, String> authData = DataGenerator.getAuthData(userData.get("email"), userData.get("password"));
        Response userLogin = apiCoreRequests.makeUserLogin("https://playground.learnqa.ru/api/user/login", authData);
        String header = userLogin.getHeader("x-csrf-token");
        String cookie = userLogin.getCookie("auth_sid");

        Response responseDeleteUser = apiCoreRequests.makeDeleteUser("https://playground.learnqa.ru/api/user/" + userId, header, cookie);

        Response responseUserData = apiCoreRequests.makeGetUserRequestWithTokenAndCookie("https://playground.learnqa.ru/api/user/" + userId, header, cookie);

        Assertions.assertResponseCodeEquals(responseDeleteUser, 200);
        Assertions.assertResponseTextEquals(responseUserData, "User not found");
    }


    @Test
    @Description("This test checks unsuccessfully deleting user with other user")
    @DisplayName("Test unsuccessfully delete user: other user")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteUsersDataWithOtherUser() {
        //Create
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = apiCoreRequests
                .makePostRequestCreateUserAsJson("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateUser.getString("id");

        Map<String, String> userData2 = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser2 = apiCoreRequests
                .makePostRequestCreateUserAsJson("https://playground.learnqa.ru/api/user/", userData2);
        String userId2 = responseCreateUser2.getString("id");


        //Login
        Map<String, String> authData = DataGenerator.getAuthData(userData2.get("email"), userData2.get("password"));
        Response userLogin = apiCoreRequests.makeUserLogin("https://playground.learnqa.ru/api/user/login", authData);
        String header = userLogin.getHeader("x-csrf-token");
        String cookie = userLogin.getCookie("auth_sid");

        Response responseDeleteUser = apiCoreRequests.makeDeleteUser("https://playground.learnqa.ru/api/user/" + userId, header, cookie);
        Response responseUserData = apiCoreRequests.makeGetUserRequestWithTokenAndCookie("https://playground.learnqa.ru/api/user/" + userId, header, cookie);


        Assertions.assertResponseCodeEquals(responseUserData, 200);

       }
}
