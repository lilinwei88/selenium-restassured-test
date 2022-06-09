package com.test.my.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class PropertiesManager {
    private static Properties properties;
    private static final String REGEX = "^((?![\\\\/]).)*$";
    private static final Logger logger = CustomLogger.getLogger(PropertiesManager.class);

    /**
     * A private constructor.
     */
    private PropertiesManager() {
        // Keep it empty.
    }

    static {
        try {
            //setting up testing env properties.
            properties = loadFile("src/test/resources/" + getEnv() + ".properties");
        } catch (Exception e) {
            logger.error("Unable to find the properties file", e);
        }
    }

    private static Properties loadFile(String filePath) {
        try (InputStream input = new FileInputStream(filePath)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException ex) {
            logger.error("unable to find the properties file", ex);
            return null;
        }
    }

    /**
     * Loads a property by key.
     *
     * @param propertyKey   the property key
     * @return the property value if it exists
     */
    public static String loadProp(String propertyKey) {

        String val = "";
        if (StringUtils.isNotBlank(propertyKey) && propertyKey.matches(REGEX)) {
            val = System.getProperty(propertyKey);
        }

        if (StringUtils.isBlank(val) && propertyKey.matches(REGEX)
                && StringUtils.isNotBlank(System.getenv(propertyKey))) {
            val = System.getenv(propertyKey);
            if (StringUtils.isNotBlank(val)) {
                return val;
            }
        }
        if (StringUtils.isBlank(val) && propertyKey.matches(REGEX) && properties != null) {
            val = properties.getProperty(propertyKey);
            if (StringUtils.isNotBlank(val)) {
                return val;
            }
        }
        logger.info("get property: " + val);
        return val;
    }

    /**
     * Loads a property by key with default value.
     *
     * @param propertyKey       the property key
     * @param propertyDefault   default value if it is not exist
     * @return the property value if it exists, default value if it doesn't
     */
    public static String loadProp(String propertyKey, String propertyDefault) {
        String val;
        val = loadProp(propertyKey);

        if (val == null) {
            val = propertyDefault;
        }
        return val;
    }

    /**
     * Return which test environment.
     *
     * @return environment e.g. lab, dev (or null if others)
     */
    public static String getEnv() {
        final String ENV = "-Denv=";
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> vmOptions = runtimeMxBean.getInputArguments();
        boolean hasElement = vmOptions.stream().anyMatch(option -> option.contains(ENV));
        if (hasElement) {
            Optional<String> element = vmOptions.stream().filter(option -> option.contains(ENV)).findFirst();
            if (element.isPresent()) {
                return element.get().replace(ENV, "");
            }
        }
        if (System.getProperty("env") != null && !System.getProperty("env").isBlank()) {
            return System.getProperty("env");
        }
        return "dev"; // default to dev if vm options are not set
    }
}
