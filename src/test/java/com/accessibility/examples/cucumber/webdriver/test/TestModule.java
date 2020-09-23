package com.accessibility.examples.cucumber.webdriver.test;

import com.accessibility.AccessibilityScanner;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.openqa.selenium.WebDriver;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(WebDriver.class).toProvider(WebDriverProvider.class).in(
                Singleton.class);
        bind(AccessibilityScanner.class).toProvider(
                AccessibilityScannerProvider.class);
    }
}
