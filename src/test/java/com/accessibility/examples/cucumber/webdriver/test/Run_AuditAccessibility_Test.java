package com.accessibility.examples.cucumber.webdriver.test;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(format = {"json:target/accessibility.json", "pretty",
        "html:target/cucumber-html-report/accessibility"}, features = {"classpath:features/accessibility_audit.feature"}, monochrome = true, strict = true)
public class Run_AuditAccessibility_Test {

}
