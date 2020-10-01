package com.accessibility.examples.cucumber.webdriver.test;

import com.accessibility.examples.junit.webdriver.test.WebDriverJunitIT;
import com.google.inject.Provider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class WebDriverProvider implements Provider<WebDriver> {

    private static final Logger log = Logger.getLogger(WebDriverJunitIT.class);

    public static WebDriver getDriver() {
        Properties prop = new Properties();
        try (InputStream input = WebDriverProvider.class.getClassLoader().getResourceAsStream("test.properties")) {
            prop.load(input);
        } catch (NullPointerException | IOException e) {
            log.info(e);
        }
        String browser = prop.getProperty("browser");
        WebDriver driver;
        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().forceCache().setup();
                driver = new FirefoxDriver();
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().forceCache().setup();
                driver = new ChromeDriver();
        }
        return driver;
    }

    public WebDriver get() {
        return getDriver();
    }
}
