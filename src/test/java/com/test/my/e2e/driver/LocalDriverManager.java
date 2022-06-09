package com.test.my.e2e.driver;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class LocalDriverManager {

    private static final Logger logger = LoggerFactory.getLogger(LocalDriverManager.class);
    private static final String CHROME_DRIVER_VERSION = "90.0.4430.24";

    private ChromeDriverService chService;
    private WebDriver driver;


    private void startService() {
        if (null == chService) {
            String testPlatform = System.getProperty("testPlatform");
            try {
                if (testPlatform != null && testPlatform.equalsIgnoreCase("docker")) {
                    WebDriverManager.chromedriver().driverVersion(CHROME_DRIVER_VERSION).setup();
                    chService = new ChromeDriverService.Builder()
                            .usingDriverExecutable(
                                    new File(WebDriverManager.chromedriver().driverVersion(CHROME_DRIVER_VERSION)
                                            .getDownloadedDriverPath()))
                            .usingAnyFreePort().build();
                    chService.start();
                } else {
                    WebDriverManager.chromedriver().setup();
                    chService = new ChromeDriverService.Builder().
                            usingDriverExecutable(new File(WebDriverManager.chromedriver().getDownloadedDriverPath())).
                            usingAnyFreePort().
                            build();
                    chService.start();
                }
            } catch (Exception e) {
                logger.error("Fail to start chrome", e);
            }
        }
    }

    /**
     * create driver based on capabilities.
     *
     * @param defaultCaps default capabilities
     * @return driver ready to use.
     */
    public WebDriver getDriver(DefaultCapabilities defaultCaps) {
        return getDriver("", defaultCaps, null);
    }
    /**
     * create driver based on capabilities.
     *
     * @param testName    test name
     * @param defaultCaps default capabilities
     * @return driver ready to use.
     */
    public WebDriver getDriver(String testName, DefaultCapabilities defaultCaps) {
        return getDriver(testName, defaultCaps, null);
    }

    /**
     * create driver based on capabilities.
     *
     * @param testName      test name
     * @param defaultCaps   default capabilities
     * @param seleniumProxy BrowserMobProxy for Selenium, to capture web traffic.
     * @return driver ready to use.
     */
    public WebDriver getDriver(String testName, DefaultCapabilities defaultCaps, Proxy seleniumProxy) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("name", testName);
        if (seleniumProxy != null) {
            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        }
        return setCapabilities(defaultCaps, capabilities);
    }

    /**
     * Set capabilities for the driver.
     *
     * @param defaultCaps  Default capabilities
     * @param capabilities Desired capabilities
     * @return WebDriver
     */
    private WebDriver setCapabilities(DefaultCapabilities defaultCaps, DesiredCapabilities capabilities) {

        createDriver(capabilities, defaultCaps);

        driver.manage().deleteAllCookies();

        return driver;
    }

    private void createDriver(DesiredCapabilities capabilities, DefaultCapabilities defaultCaps) {

        if (defaultCaps.getBrowser().equalsIgnoreCase("chrome")) {
            startService();
            ChromeOptions options = new ChromeOptions();
            if (defaultCaps.isIncognito()) {
                options.addArguments("--incognito");
            }
            if (defaultCaps.isHeadless()) {
                options.addArguments("--headless");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
            }
            options.merge(capabilities);
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            driver = new ChromeDriver(chService, options);
        } else if (defaultCaps.getBrowser().equalsIgnoreCase("firefox")) {
            capabilities.setCapability("networkConnectionEnabled", true);
            capabilities.setCapability("browserConnectionEnabled", true);
            capabilities.setJavascriptEnabled(false);
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            if (defaultCaps.isHeadless()) {
                FirefoxBinary firefoxBinary = new FirefoxBinary();
                firefoxBinary.addCommandLineOptions("--headless");
                firefoxOptions.setBinary(firefoxBinary);
            }
            firefoxOptions.merge(capabilities);
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefoxOptions);
            driver = new FirefoxDriver(firefoxOptions);
        } else if (defaultCaps.getBrowser().equalsIgnoreCase("safari")) {
            driver = new SafariDriver();
        }
        driver.manage().window().maximize();
    }
}
