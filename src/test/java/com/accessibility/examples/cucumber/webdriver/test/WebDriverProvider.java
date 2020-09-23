package com.accessibility.examples.cucumber.webdriver.test;

import com.google.inject.Provider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverProvider implements Provider<WebDriver> {

    public WebDriver get() {
        WebDriverManager.chromedriver().forceCache().setup();
        return new ChromeDriver();
    }

}
