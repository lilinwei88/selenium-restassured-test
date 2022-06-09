package com.test.my.e2e;

import com.test.my.e2e.driver.DefaultCapabilities;
import com.test.my.e2e.driver.LocalDriverManager;
import com.test.my.utils.CustomLogger;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.test.my.utils.PropertiesManager.loadProp;


public class BaseTest {

    protected WebDriver driver;
    public static final String DEFAULT_PASSWORD = "default";
    private LocalDriverManager localDriverManager;
    private String testMethodName;
    private String testPlatform = "LOCAL";
    private DefaultCapabilities capabilities;
    private static final Logger logger = CustomLogger.getLogger(BaseTest.class);


    /**
     * Gets the name of the currently running method and passes it back to be used for Accessibility Report Names.
     *
     * @return Test Method Name as a String
     */
    public static String getCurrentMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }


    /**
     * Get web driver.
     *
     * @param browser           browser to be tested
     * @param version           browser version
     * @param platform          platform e.g. Windows, Mac
     * @param osVersion         platform version
     * @param location          location for the app installer
     * @param resolution        resolution
     * @param incognito         if true sets the browser to launch in incognito mode
     * @param headless          if true runs in headless mode
     * @return a web driver
     */
    private WebDriver getDriver(String browser, String version, String platform, String osVersion,
                                String location, String resolution, boolean incognito, boolean headless) {
        try {
            capabilities = new DefaultCapabilities(browser, version, platform, osVersion,
                    location, resolution, incognito, headless);

            localDriverManager = new LocalDriverManager();
            driver = localDriverManager.getDriver(testMethodName, capabilities);

        } catch (Exception ex) {
            logger.error("Exception: Could not create web driver!", ex);
        }
        return driver;
    }

    public DefaultCapabilities getCapabilities() {
        return capabilities;
    }


    /**
     * Set up configuration and driver before class.
     *
     * @param browser           browser to be run e.g. chrome or firefox
     * @param version           browser version
     * @param platform          platform e.g. WIN10, Mac
     * @param osVersion         platform version to use
     * @param testPlatform      where the test is to be run LOCAL or PERFECTO
     * @param location          location of the device
     * @param resolution        resolution of the device (default 1280x1024 - do NOT change, adjust in test config)
     * @param incognito         if true launches the browser in incognito mode
     * @param headless          if true runs in headless mode.
     */
    @BeforeClass(alwaysRun = true)
    @Parameters(value = {"browser", "version", "platform", "osVersion",
            "testPlatform", "location", "resolution", "incognito", "headless"})
    public void setup(@Optional("Chrome") String browser, @Optional("latest") String version,
                      @Optional("Windows") String platform, @Optional("10") String osVersion,
                      @Optional("local") String testPlatform, @Optional("") String location,
                      @Optional("1280x1024") String resolution, @Optional("false") boolean incognito,
                      @Optional("false") boolean headless
                      ) {

        this.testPlatform = (testPlatform == null || testPlatform.isEmpty()) ? loadProp("testPlatform").toUpperCase() :
                testPlatform.toUpperCase();
        logger.info("testPlatform: " + this.testPlatform);
        driver = getDriver(browser, version, platform, osVersion,
                  location, resolution, incognito,  headless);
    }

    /**
     * Before Method used to set up the existing Test Name from the Method Name.
     *
     * @param getMethod current test method name
     */
    @BeforeMethod
    public void beforeTestSetup(Method getMethod) {
        testMethodName = getMethod.getName();
        logger.info("About to run: " + testMethodName);
    }

    /**
     * cleanup after each test method.
     *
     * @param result test result
     */
    @AfterMethod(alwaysRun = true)
    public void cleanUpAfterTestMethod(ITestResult result) {
        if (result.isSuccess()) {
            logger.info("Test Result: " + result.isSuccess());
        }
    }

    /**
     * clean up after class.
     */
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        try {
            quitDriver();
        } catch (Exception e) {
            logger.error("Error with quitting WebDriver.... Handling gracefully.", e);
        }
    }

    private void quitDriver() {
        if (driver != null) {
            logger.info("Quitting web driver!");
            driver.quit();
        }
    }
}
