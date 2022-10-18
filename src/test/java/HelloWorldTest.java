import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloWorldTest {


    @Test
    public void testHelloWorld() {
        System.out.println("Hello from Sasha");
    }

    @Test
    public void testHelloAPI() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
        response.prettyPrint();
    }

    @Test
    public void testGetText() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }

    @Test
    public void testGetJSON() {

        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        List<String> messages = response.getList("messages.message");
        System.out.println(messages.get(1));
    }

    @Test
    public void testRedirect() {

        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String locationHeader = response.getHeader("Location");
        System.out.println(locationHeader);
    }

    @Test
    public void testLongRedirect() {

        String locationURL = "https://playground.learnqa.ru/api/long_redirect";
        int amountRedirects = 0;

        while (locationURL != null) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(true)
                    .when()
                    .get(locationURL)
                    .andReturn();

            locationURL = response.header("Location");
            amountRedirects += 1;

            System.out.println("Amounts of redirections are: " + amountRedirects);
            response.prettyPrint();

        }
    }

    @Test
    public void testLongTime() throws InterruptedException {

        JsonPath responseWithoutQuery = RestAssured
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String token = responseWithoutQuery.get("token");
        int seconds = responseWithoutQuery.get("seconds");

        JsonPath responseUnreadyTask = RestAssured
                .given()
                .queryParam("token", token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        String status = responseUnreadyTask.get("status");
        System.out.println("Status for unready query = " + status);

        Thread.sleep(seconds * 1000L);

        JsonPath responseReadyTask = RestAssured
                .given()
                .queryParam("token", token)
                .when()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        status = responseReadyTask.get("status");
        String result = responseReadyTask.get("result");
        System.out.println("Status for ready query = " + status);
        System.out.println("Result for ready query = " + result);
    }

    @Test
    public void testPassword() {

        String[] passwords = new String[]{
                "123456",
                "Password",
                "12345678",
                "letmein",
                "qwerty",
                "12345",
                "123456789",
                "1234567",
                "football",
                "iloveyou",
                "admin",
                "welcome",
                "monkey",
                "login",
                "abc123",
                "starwars",
                "123123",
                "dragon",
                "passw0rd",
                "master",
                "hello",
                "freedom",
                "whatever",
                "qazwsx",
                "trustno1"};

        Map<String, String> data = new HashMap<>();
        data.put("login", "super_admin");
        for (String password : passwords) {
            data.put("password", password);

            Response response = RestAssured
                    .given()
                    .body(data)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String authCookie = response.getCookie("auth_cookie");

            Map<String, String> cookies = new HashMap<>();
            cookies.put("auth_cookie", authCookie);

            Response response2 = RestAssured
                    .given()
                    .cookies(cookies)
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            String answer = response2.asString();

            if (!answer.equals("You are NOT authorized")) {
                System.out.println("Correct password: " + password);
                break;
            }
        }
    }
}




