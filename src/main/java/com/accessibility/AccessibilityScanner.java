package com.accessibility;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class AccessibilityScanner {

    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final Logger log = Logger.getLogger(AccessibilityScanner.class);
    private final JsFactory jsFactory;

    public AccessibilityScanner(WebDriver driver) throws IOException {
        this.driver = driver;
        js = (JavascriptExecutor) driver;
        jsFactory = JsFactory.getInstance();

    }

    public Map<String, Object> runAccessibilityAudit() throws IOException {
        Map<String, Object> auditReport = new HashMap<>();
        driver.manage().window().maximize();
        js.executeScript(jsFactory.getAccessibilityContent());
        String accessibilityTests = "var auditConfig = new axs.AuditConfiguration(); "
                + "var results = axs.Audit.run(auditConfig);"
                + "var auditResults = axs.Audit.auditResults(results);"
                + "var report = axs.Audit.createReport(results);return report";
        String report = (String) js.executeScript(accessibilityTests);

        log.info(report);

        try {
            log.info(js.executeScript("$.active;"));
        } catch (WebDriverException e) {
            log.info("++++++++Injecting jQuery+++++++++++++");
            js.executeScript(jsFactory.getJqueryContent());
        }

        List<Result> errors = parseReport(report, "Error:");
        List<Result> warnings = parseReport(report, "Warning:");

        decorateElements(errors, "red");
        decorateElements(warnings, "yellow");
        BufferedImage fullScreen = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver).getImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(fullScreen, "jpg", baos);
        baos.flush();
        byte[] screenshot = baos.toByteArray();
        baos.close();
        auditReport.put("error", errors);
        auditReport.put("warning", warnings);
        auditReport.put("screenshot", screenshot);
        auditReport.put("plain_report", report);
        return auditReport;
    }

    private void decorateElements(List<Result> results, String color) {
        for (Result result : results) {
            List<String> locators = result.getElements();
            addBorder(locators, result.getRule(), color);
        }
    }

    public List<Result> parseReport(String report, String filterOn) {
        if (filterOn.toLowerCase().contains("error"))
            filterOn = "Error:";
        else if (filterOn.toLowerCase().contains("warning"))
            filterOn = "Warning:";
        else
            throw new IllegalArgumentException(
                    "Currently only support filtering on Error: and Warning:");
        if (report == null)
            throw new NullPointerException("Report to parse cannot be null");
        List<Result> parsedResult = new ArrayList<>();
        int startError = report.indexOf(filterOn);
        while (startError > 0) {
            Result result = new Result();
            int end = report.indexOf("\n\n", startError);
            String error = report.substring(startError + filterOn.length(),
                    end).trim();
            result.setRule(error.substring(0, error.indexOf("\n")));
            String link = null;
            String[] locators;
            int elementStart = error.indexOf("\n") + 1;
            String element;
            if (error.indexOf("See") > 0) {
                element = error.substring(elementStart, error.indexOf("See"));
                link = error.substring(error.indexOf("See"));
            } else {
                element = error.substring(elementStart);
            }
            locators = element.split("\n");
            result.setElements(Arrays.asList(locators));
            result.setUrl(link);
            parsedResult.add(result);
            startError = report.indexOf(filterOn, end);
        }
        return parsedResult;
    }

    private void addBorder(List<String> locators, String rule, String color) {
        StringBuilder ruleBuilder = new StringBuilder(rule);
        for (String locator : locators) {
            ruleBuilder = new StringBuilder("<p>" + ruleBuilder + "</p>");
            String script = "$(\"" + locator
                    + "\").css(\"border\",\"5px solid " + color + "\")";
            js.executeScript(script);
        }
        rule = ruleBuilder.toString();
    }
}
