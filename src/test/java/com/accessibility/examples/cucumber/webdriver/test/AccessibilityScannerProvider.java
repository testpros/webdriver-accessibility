package com.accessibility.examples.cucumber.webdriver.test;

import com.accessibility.AccessibilityScanner;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class AccessibilityScannerProvider implements
        Provider<AccessibilityScanner> {
    private WebDriver driver;

    @Inject
    public AccessibilityScannerProvider(WebDriver driver) {
        this.driver = driver;
    }

    public AccessibilityScanner get() {
        try {
            return new AccessibilityScanner(driver);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
