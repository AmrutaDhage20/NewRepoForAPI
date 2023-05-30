package org.example;

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

    public PostAllProductList() {
        try {
            url = ConfigReader.getUrl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Logger logger = Logger.getLogger("ProductLists.class");

    @BeforeTest
    public void getLoggerDisplay() {
        PropertyConfigurator.configure("log4j2.properties");
    }

    @Test(priority = 1)
    public void validateResponseCode() {
        Response response = given().when().post(url);
        var results = response.body().asString();
        JSONObject jsonObject = new JSONObject(results);
        var responseCode = jsonObject.get("responseCode");
        logger.info("responseCode: " + responseCode);
        Assert.assertEquals(responseCode, 405);
    }

    @Test(priority = 2)
    public void validateResponseMessage() {
        var response = given().when().post(url).then().extract().asString();
        JsonPath jsonResponse = new JsonPath(response);
        var responseMessage = jsonResponse.get("message");
        logger.info("Response Message is" + responseMessage);
        Assert.assertEquals(responseMessage, "This request method is not supported.");
    }
}