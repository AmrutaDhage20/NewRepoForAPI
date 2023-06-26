package org.assignments;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

public class BrandListEndpoint {
    private static final Logger logger = Logger.getLogger(BrandListEndpoint.class);

        public static Response GetAllBrandList() {
            String endpointUrl = "/brandsList";
            Response response = RestAssured.get(endpointUrl);
            logger.info("Response: " + response.getBody().asString());
            return response;
        }
}