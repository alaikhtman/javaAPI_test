package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {
    public static void assertJsonByName(Response Response, String name, int expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        int value = Response.jsonPath().getInt(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertJsonByName(Response Response, String name, String expectedValue) {
        Response.then().assertThat().body("$", hasKey(name));

        String value = Response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equal to expected value");
    }

    public static void assertResponseTextEquals(Response Response, String expectedAnswer) {
        assertEquals(expectedAnswer, Response.asString(), "Response text is not expected");
    }


    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode) {
        assertEquals(expectedStatusCode, Response.statusCode(), "Response status is not expected");
    }

    public static void assertJsonHasField(Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", hasKey(expectedFieldName));
    }

    public static void assertJsonHasNoField(Response Response, String expectedFieldName) {
        Response.then().assertThat().body("$", not(hasKey(expectedFieldName)));
    }

    public static void assertJsonHasFields(Response Response, String [] expectedFieldNames) {
        for (String expectedFieldName : expectedFieldNames) {
            Assertions.assertJsonHasField(Response, expectedFieldName);
        }
    }
}
