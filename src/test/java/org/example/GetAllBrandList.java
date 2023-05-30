package org.example;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class GetAllBrandList {
    private String url;
    private String brandId;
    private String brandName;

    public GetAllBrandList() {
        try {
            url = ConfigReader.getUrl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GetAllBrandList(String brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }

    private static final Logger logger = Logger.getLogger("BrandList.class");

    @BeforeTest
    public void getLoggerDisplay() {
        PropertyConfigurator.configure("log4j2.properties");
    }

    @Test(priority = 1)
    public void getStatusCode() {
        Response response = RestAssured.get(url).then().extract().response();
        logger.info("Status code is " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(priority = 2)
    public void getBrandList() {
        var getList = given().when().get(url).then().log().all().toString();
        logger.info("Brand List is  " + getList);
    }

    @Test(priority = 3)
    public void validateContent() {
        Response response = RestAssured.get(url).then().extract().response();
        JsonPath jsonObject = new JsonPath(response.asString());
        var size = jsonObject.getInt("brands.size()");
        List<GetAllBrandList> brandData = new ArrayList<>();
        GetAllBrandList dataList = new GetAllBrandList("1", "Polo");
        brandData.add(dataList);
        dataList = new GetAllBrandList("2", "H&M");
        brandData.add(dataList);
        for (int index = 0; index < size; index++) {
            String id = jsonObject.getString("brands[" + index + "].id");
            String name = jsonObject.getString("brands[" + index + "].brand");
            brandData.forEach(brand -> {
                if (name.equals(brand.brandName)) {
                    logger.info(brand.brandId);
                    logger.info(brand.brandName);
                    Assert.assertEquals(brand.brandId, id);
                    Assert.assertEquals(brand.brandName, name);
                }
            });
        }
    }

    @Test(priority = 4)
    public void validateLength() {
        var response = given().when().get(url).then().extract().asString();
        JsonPath jsonResponse = new JsonPath(response);
        var idLength = jsonResponse.getInt("brands.id.size()");
        logger.info("The brand length is " + idLength);
        Assert.assertEquals(idLength, 34, "number of brands are not expected");
    }
}