package org.assignments;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.log4j.PropertyConfigurator;
import org.testng.annotations.BeforeTest;

import java.io.IOException;

public class BaseFile {
    protected static String url;
    protected static Logger logger = Logger.getLogger("BaseFile.class");

    @BeforeTest
    public void setup() {
        try {
            url = ConfigReader.getUrl();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while reading URL from ConfigReader", e);
        }
        configureLogger();
    }

    public void configureLogger() {
        PropertyConfigurator.configure("log4j2.properties");
    }
}