package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static io.restassured.RestAssured.*;

public class GetAllProductList {

    private static String url;
    private String productId;
    private String productName;
    private String productPrice;
    private String productBrand;

    public GetAllProductList() {

        try {
            url = ConfigReader.getUrl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GetAllProductList(String productId, String productName, String productPrice, String productBrand) {

        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productBrand = productBrand;
    }

    private static Logger logger = Logger.getLogger("Product.class");

    @BeforeTest
    public void getLoggerDisplay() {
        PropertyConfigurator.configure("log4j2.properties");
    }
    @Test(priority = 1)
    public void validateStatusCode() throws IOException {
        {
            baseURI = ConfigReader.getUrl();
            Response response = RestAssured.get(ConfigReader.getUrl()).then().extract().response();
            Assert.assertEquals(response.getStatusCode(), 200);
            logger.info("Status code is " + response.getStatusCode());
        }
    }
    @Test(priority = 2)
    public void validateProductList() {
        var getList = given().when().get(url).then().log().all().toString();
        logger.info("Product List is " + getList);
    }
    @Test(priority = 3)
    public void validateContent() {
        Response response = RestAssured.get(url).then().extract().response();
        JsonPath jsonObject = new JsonPath(response.asString());
        List<GetAllProductList> productList = jsonObject.getList("products");
        List<GetAllProductList> mockData = new ArrayList<>();
        GetAllProductList productLists = new GetAllProductList("5", "Winter Top", "Rs. 600", "Mast & Harbour");
        mockData.add(productLists);
        productLists = new GetAllProductList("41", "Beautiful Peacock Blue Cotton Linen Saree", "Rs. 5000", "Madame");
        for (int index = 0; index < productList.size(); index++) {
            mockData.add(productLists);
            String id = jsonObject.getString("products[" + index + "].id");
            String name = jsonObject.getString("products[" + index + "].name");
            String price = jsonObject.getString("products[" + index + "].price");
            String brand = jsonObject.getString("products[" + index + "].brand");
            mockData.forEach(mockProduct -> {
                if (name.equals(mockProduct.productName)) {
                    logger.info(mockProduct.productId);
                    logger.info(mockProduct.productName);
                    logger.info(mockProduct.productBrand);
                    logger.info(mockProduct.productPrice);
                    Assert.assertEquals(mockProduct.productId, id);
                    Assert.assertEquals(mockProduct.productName, name);
                    Assert.assertEquals(mockProduct.productPrice, price);
                    Assert.assertEquals(mockProduct.productBrand, brand);
                }
            });
        }
    }
    @Test(priority = 4)
    public void validateLength() {
        var response = given().when().get(url).then().extract().asString();
        JsonPath jsonResponse = new JsonPath(response);
        var idLength = jsonResponse.getInt("products.id.size()");
        logger.info("The length is : " + idLength + " products");
        Assert.assertEquals(idLength, 34, "number of products are Not expected");
    }
}