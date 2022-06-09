package com.test.my.e2e;

import com.test.my.e2e.pages.HomePage;
import com.test.my.e2e.pages.LocatorType;
import com.test.my.e2e.pages.LoginPage;
import com.test.my.utils.CustomLogger;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import static com.test.my.utils.PropertiesManager.loadProp;
import static org.testng.Assert.assertTrue;

public class LoginTest extends BaseTest {

    private static final Logger logger = CustomLogger.getLogger(LoginTest.class);


    @Test(description = "Verify a User can login and click on continue to the home page.")
    public void testLogin() {
        logger.info("Login for: test user");
        LoginPage loginPage = new LoginPage(driver, loadProp("BASE_URL"));
        HomePage homePage = loginPage.login(loadProp("USER_ID"), DEFAULT_PASSWORD);
        assertTrue(homePage.doesElementExist("div.logo.logo", LocatorType.CSS), "Welcome is displayed");
    }
}