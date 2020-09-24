package com.accessibility;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * @author nikulkarni
 * Singleton, package protected class;
 * could have used Guice but want to keep it DI library agnostic so that others can use this library
 */
class JsFactory {

    ApplicationProperties applicationProperties;

    private static JsFactory INSTANCE = null;
    private String accessibilityContent = null;

    private String jqueryContent = null;

    private JsFactory() {
        this.applicationProperties = new ApplicationProperties("application.properties");
    }

    static synchronized JsFactory getInstance() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new JsFactory();
            INSTANCE.load();
        }
        return INSTANCE;
    }

    private void load() throws IOException {
        jqueryContent = Jsoup.connect(applicationProperties.getProperty("jquerycdnurl")).ignoreContentType(true).execute().body();
        accessibilityContent = Jsoup.connect(applicationProperties.getProperty("applicationcdnurl")).ignoreContentType(true).execute().body();
    }

    String getAccessibilityContent() {
        return accessibilityContent;
    }

    String getJqueryContent() {
        return jqueryContent;
    }

}
