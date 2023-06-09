package org.assignments;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class PostAllProductList extends BaseFile {
    private Response response;

    @Test(priority = 1)
    public void getNegativeResponseCode() {
        response = given().when().post(url);
        var results = response.body().asString();
        JSONObject jsonObject = new JSONObject(results);
        var responseCode = jsonObject.get("responseCode");
        logger.info("The responseCode is: " + responseCode);
        Assert.assertEquals(responseCode, 405);
    }

    @Test(priority = 2)
    public void getResponseMessage() {
        response = given().when().post(url);
        var results = response.body().asString();
        JsonPath jsonResponse = new JsonPath(results);
        var responseMessage = jsonResponse.get("message");
        logger.info("The Response Message is: " + responseMessage);
        Assert.assertEquals(responseMessage, "This request method is not supported.");
    }
}