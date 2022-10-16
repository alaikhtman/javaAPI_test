import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        while (locationURL != null) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(true)
                    .when()
                    .get(locationURL)
                    .andReturn();

            locationURL = response.header("Location");

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

        Thread.sleep(seconds* 1000L);

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
}



