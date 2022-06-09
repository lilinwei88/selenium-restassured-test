package com.test.my.utils;

import com.test.my.e2e.driver.DefaultCapabilities;
import com.test.my.e2e.driver.LocalDriverManager;
import com.test.my.e2e.pages.LoginPage;
import com.test.my.e2e.BaseTest;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.test.my.utils.PropertiesManager.loadProp;

public class UiHelper  {

    private static final Logger logger = CustomLogger.getLogger(UiHelper.class);

    protected static WebDriver driver;
    private static final LocalDriverManager localDriverManager;
    private static final DefaultCapabilities capabilities;
    static {
        capabilities = new DefaultCapabilities(Boolean.parseBoolean(PropertiesManager.loadProp("headless")));
        localDriverManager = new LocalDriverManager();
        driver = localDriverManager.getDriver(capabilities);
    }

    public static String getAuthCodeFromAccountManagerLogin(String url, String userId, String password) {

        String authCode = "";
        try {
            LoginPage loginPage = new LoginPage(driver, url );
            loginPage.login(userId, password);
            String currentUrl = driver.getCurrentUrl();
            String patternString = "code=(.*?)$";
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(currentUrl);
            if (matcher.find()) {
                authCode = matcher.group(1);
            }
            System.out.println("auth code found: "+ authCode);
        } finally {
            driver.quit();
        }
        return authCode;
    }

    @Test
    public void verifyCode() {

        Assert.assertTrue(getAuthCodeFromAccountManagerLogin(PropertiesManager.loadProp("ID_BROKER_URL"),
                PropertiesManager.loadProp("ICMF_USER_ID"), BaseTest.DEFAULT_PASSWORD) != null);
    }
}
