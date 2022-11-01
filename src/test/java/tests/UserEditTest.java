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

@Epic("Edit cases")
@Feature("Edit user")
@Link("https://example.org")
public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test checks successfully update firstname of user")
    @DisplayName("Test update user: with firstName")
    @Severity(SeverityLevel.CRITICAL)
    public void testEditUsersDataFirstName() {
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

        //Edit
        Map<String, String> editData = DataGenerator.getEditData("firstName", "updatedName");
        Response responseEditUser = apiCoreRequests.makeEditUser("https://playground.learnqa.ru/api/user/" + userId, header, cookie, editData);

        //Get
        Response responseUserData = apiCoreRequests.makeGetUserRequestWithTokenAndCookie("https://playground.learnqa.ru/api/user/" + userId, header, cookie);

        Assertions.assertJsonByName(responseUserData, "firstName", "updatedName");
    }


    @Test
    @Description("This test checks unsuccessfully update user without auth")
    @DisplayName("Test unsuccessfully update user: without auth")
    @Severity(SeverityLevel.MINOR)
    public void testEditUsersWithoutAuth() {
        //Create
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateUser = apiCoreRequests
                .makePostRequestCreateUserAsJson("https://playground.learnqa.ru/api/user/", userData);
        String userId = responseCreateUser.getString("id");


        //Edit
        Map<String, String> editData = DataGenerator.getEditData("firstName", "updatedName");
        Response responseEditUser = apiCoreRequests.makeEditUser("https://playground.learnqa.ru/api/user/" + userId, null, null, editData);


        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
    }


    @Test
    @Description("This test checks unsuccessfully update of other user")
    @DisplayName("Test unsuccessfully update user: other user")
    @Severity(SeverityLevel.NORMAL)
    public void testEditUsersDataWithOtherUser() {
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

        //Edit
        Map<String, String> editData = DataGenerator.getEditData("firstName", "updatedName");
        Response responseEditUser = apiCoreRequests.makeEditUser("https://playground.learnqa.ru/api/user/" + userId, header, cookie, editData);


        //Login
        Map<String, String> authData2 = DataGenerator.getAuthData(userData.get("email"), userData.get("password"));
        Response userLogin2 = apiCoreRequests.makeUserLogin("https://playground.learnqa.ru/api/user/login", authData2);
        String header2 = userLogin2.getHeader("x-csrf-token");
        String cookie2 = userLogin2.getCookie("auth_sid");

        //Get
        Response responseUserData = apiCoreRequests.makeGetUserRequestWithTokenAndCookie("https://playground.learnqa.ru/api/user/" + userId, header2, cookie2);

        Assertions.assertJsonByName(responseUserData, "firstName", userData.get("firstName"));

    }

    @Test
    @Description("This test checks unsuccessfully update email")
    @DisplayName("Test unsuccessfully update user: with incorrect email")
    @Severity(SeverityLevel.NORMAL)
    public void testEditUsersDataIncorrectEmail() {
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

        //Edit
        Map<String, String> editData = DataGenerator.getEditData("email", "testexamples.com");
        Response responseEditUser = apiCoreRequests.makeEditUser("https://playground.learnqa.ru/api/user/" + userId, header, cookie, editData);

        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");


    }

    @Test
    @Description("This test checks unsuccessfully update firstName")
    @DisplayName("Test unsuccessfully update user: with incorrect firstName")
    @Severity(SeverityLevel.MINOR)
    public void testEditUsersDataIncorrectFirstName() {
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

        //Edit
        String incorrectName = DataGenerator.getStringWithLength(1);
        Map<String, String> editData = DataGenerator.getEditData("firstName", incorrectName);
        Response responseEditUser = apiCoreRequests.makeEditUser("https://playground.learnqa.ru/api/user/" + userId, header, cookie, editData);

        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"Too short value for field firstName\"}");


    }


}
