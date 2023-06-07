package restapi_assignments;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class PostAllProductList {
    private static String url;
    private static final Logger logger = Logger.getLogger("PostAllProductList.class");
    private Response response;

    public PostAllProductList() {
        try {
            url = ConfigReader.getUrl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeTest
    public void getLoggerDisplay() {
        PropertyConfigurator.configure("log4j2.properties");
    }

    @Test(priority = 1)
    public void validateResponseCode() {
        response = given().when().post(url);
        var results = response.body().asString();
        JSONObject jsonObject = new JSONObject(results);
        var responseCode = jsonObject.get("responseCode");
        logger.info("The responseCode is: " + responseCode);
        Assert.assertEquals(responseCode, 405);
    }

    @Test(priority = 2)
    public void validateResponseMessage() {
        response = given().when().post(url);
        var results = response.body().asString();
        JsonPath jsonResponse = new JsonPath(results);
        var responseMessage = jsonResponse.get("message");
        logger.info("The Response Message is: " + responseMessage);
        Assert.assertEquals(responseMessage, "This request method is not supported.");
    }
}