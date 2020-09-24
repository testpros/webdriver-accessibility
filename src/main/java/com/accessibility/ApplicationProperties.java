package com.accessibility;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    Properties properties;

    public ApplicationProperties(String fileName) {
        this.properties = readFile(fileName);
    }

    public Properties readFile(String fileName) {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public String getProperty(String property) {
        return this.properties.getProperty(property);
    }

}