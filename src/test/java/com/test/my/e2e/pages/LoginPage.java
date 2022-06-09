package com.test.my.e2e.pages;

import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final String passwordId = "password";
    private static final String userNameId = "username";
    private static final String loginInId = "loginButton";
    private static final String ICMF_AETNA_XPATH = "//li[5]/a";
    private static final String continueId = "icmfContinueButton";

    public LoginPage(WebDriver driver, String url) {
        super(driver, url);
    }


    /**
     * Entering Username and Password and clicks Submit.
     *
     * @param userName User Name to login
     * @param password Password for the user
     */
    public HomePage login(String userName, String password) {
        fillTextField(getElement(userNameId, LocatorType.ID), userName);
        fillTextField(getElement(passwordId, LocatorType.ID), password);
        clickOnElementUsingJavaScript(loginInId, LocatorType.ID);
        clickOnElementUsingJavaScript(continueId, LocatorType.ID);
        pause("Waiting for Home Page to load.", 8000);
        return new HomePage(driver);
    }

}
