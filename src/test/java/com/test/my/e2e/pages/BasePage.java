package com.test.my.e2e.pages;

import com.test.my.exceptions.ElementNotFoundException;
import com.test.my.utils.CustomLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class BasePage<W extends BasePage> {
    public static final Duration WEB_DRIVER_TIMEOUT_IN_SECONDS = Duration.ofSeconds(60);
    public static final String ELEMENT_NOT_FOUND = "Element not found";
    private static final Duration TIME_OUT_IN_SECONDS = Duration.ofSeconds(30);
    private static final Logger logger = CustomLogger.getLogger(BasePage.class);
    protected final WebDriver driver;
    private final HashMap<String, String> elementLocators = new HashMap<>();
    protected WebDriverWait wait;
    protected String pageUrl;
    private boolean printElementNotFoundFlag = true;

    /**
     * constructor for base page.
     *
     * @param driver web driver
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, WEB_DRIVER_TIMEOUT_IN_SECONDS);
        pageUrl = driver.getCurrentUrl();
    }

    public BasePage(WebDriver driver, String url) {
        this(driver);
        pageUrl = url;
        this.driver.navigate().to(url);
    }

    /**
     * Get By locator from String locator.
     *
     * @param locator     locator name
     * @param locatorType locator type
     * @return locator  the desired locator
     */
    public static By getLocator(String locator, LocatorType locatorType) {
        By by = null;
        switch (locatorType) {
            case CSS:
                by = By.cssSelector(locator);
                break;
            case XPATH:
                by = By.xpath(locator);
                break;
            case ID:
                by = By.id(locator);
                break;
            case CLASS_NAME:
                by = By.className(locator);
                break;
            case TAG_NAME:
                by = By.tagName(locator);
                break;
            case NAME:
                by = By.name(locator);
                break;
            case LINK_TEXT:
                by = By.linkText(locator);
                break;
            case PARTIAL_LINK_TEXT:
                by = By.partialLinkText(locator);
                break;

            default:
                break;
        }
        return by;
    }

    public static void pause(String reason, long timeToSleep) {
        pause(reason, timeToSleep, 0);
    }

    /**
     * This method is to pause the automation execution for any reason.
     *
     * @reason reason
     * @timeToSleep timeToSleep
     * @nanos nanosecond
     */
    public static void pause(String reason, long timeToSleep, int nanos) {
        logger.info("Pausing thread for " + timeToSleep + " MILLISECONDS and " +
                nanos + " NANOSECONDS. Reason: " + reason);

        long startTime;
        long endTime;
        long timeSlept = 0;
        boolean interrupted = false;

        while (timeToSleep > 0) {
            startTime = System.currentTimeMillis();
            try {
                Thread.sleep(timeToSleep, nanos);
                break;
            } catch (InterruptedException e) {
                logger.error("Error in waiting: ", e);
                endTime = System.currentTimeMillis();
                timeSlept = endTime - startTime;
                timeToSleep -= timeSlept;
                interrupted = true;
                Thread.currentThread().interrupt();
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
            logger.info("Pausing thread for " + timeToSleep + " MILLISECONDS and " +
                    nanos + " NANOSECONDS. Reason: " + reason);
        }
        logger.info("Thread is unpaused.");
    }

    /**
     * NOTE: MAKE SURE TO CALL {@link #resetDefaultTimeout()} after your work done.
     * Not recommended to use in general purpose use. Use it only for special cases.
     *
     * @param timeOutInSec timeout value in seconds.
     */
    public void changeDefaultTimeout(Duration timeOutInSec) {
        wait = new WebDriverWait(driver, timeOutInSec);
    }

    /**
     * Reset the wait to default time out.
     */
    public void resetDefaultTimeout() {
        wait = new WebDriverWait(driver, WEB_DRIVER_TIMEOUT_IN_SECONDS);
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    /**
     * Simulate browser browser back button press.
     *
     * @return url of navigated page
     */
    public String navigateToBack() {
        driver.navigate().back();
        return driver.getCurrentUrl();
    }

    /**
     * Navigate to page itself.
     *
     * @return pageObject
     */
    @SuppressWarnings("unchecked")
    public W navigateToSelf() {
        if (navigateToURL(pageUrl)) {
            return (W) this;
        }
        return null;
    }

    /**
     * Navigate to given URL.
     *
     * @param url url to load
     */
    public boolean navigateToURL(String url) {
        try {
            driver.navigate().to(url);
            wait.until(ExpectedConditions.urlContains(url));

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Helper keyboard to dismiss keyboard if present.
     */
    public void hideKeyboard() {
        try {
            driver.navigate().back();
        } catch (Exception ex) {
            logger.error("Keyboard is not visible on the screen", ex);
        }
    }

    /**
     * Return element and wait for default time for availability.
     *
     * @param aLocatorNameFromProperty element locator
     * @param aLocatorType             locator type
     * @return WebElement
     */
    public WebElement getElement(String aLocatorNameFromProperty, LocatorType aLocatorType) {
        return getElement(aLocatorNameFromProperty, "", aLocatorType,
                WEB_DRIVER_TIMEOUT_IN_SECONDS);
    }

    /**
     * Return element and wait for default time for availability.
     *
     * @param locatorNameFromProperty element locator
     * @param changeablePartInLocator desired content of the locator that is dynamic
     * @param locatorType             locator type
     * @return WebElement
     */
    public WebElement getElement(String locatorNameFromProperty, String changeablePartInLocator,
                                 LocatorType locatorType) {
        return getElement(locatorNameFromProperty, changeablePartInLocator, locatorType,
                WEB_DRIVER_TIMEOUT_IN_SECONDS);
    }

    /**
     * Return element and wait for specific timeout for availability.
     *
     * @param locatorNameFromProperty element locator
     * @param locatorType             locator type
     * @param timeOutInSec            time out in seconds
     * @return WebElement
     */
    public WebElement getElement(String locatorNameFromProperty,
                                 LocatorType locatorType, Duration timeOutInSec) {
        return getElement(locatorNameFromProperty, "", locatorType,
                timeOutInSec);
    }

    /**
     * Return desired element for availability.
     *
     * @param locatorNameFromProperty element locator
     * @param changeablePartInLocator desired content of the locator that is dynamic
     * @param aLocatorType            locator type
     * @param timeOutInSec            max time to wait
     * @return WebElement
     */
    public WebElement getElement(String locatorNameFromProperty, String changeablePartInLocator,
                                 LocatorType aLocatorType, Duration timeOutInSec) {
        if (!elementLocators.containsKey(locatorNameFromProperty)) {
            elementLocators.put(locatorNameFromProperty, locatorNameFromProperty);
        }

        if (timeOutInSec == WEB_DRIVER_TIMEOUT_IN_SECONDS) {
            return getWebElement(String.format(elementLocators.get(locatorNameFromProperty), changeablePartInLocator),
                    aLocatorType);
        }
        return getWebElement(String.format(elementLocators.get(locatorNameFromProperty), changeablePartInLocator),
                aLocatorType, timeOutInSec);
    }

    /**
     * Find out and return desired web element with default timeout.
     *
     * @param locator     name of the locator
     * @param locatorType type of the locator
     * @return WebElement  the web element of the locator
     */
    public WebElement getWebElement(String locator, LocatorType locatorType) {
        return getWebElement(locator, locatorType, WEB_DRIVER_TIMEOUT_IN_SECONDS);
    }

    /**
     * Find out and return desired web element with given timeout.
     *
     * @param locator      name of the locator
     * @param locatorType  type of the locator
     * @param timeOutInSec timeout value in seconds
     * @return web element
     */
    public WebElement getWebElement(String locator, LocatorType locatorType, Duration timeOutInSec) {
        WebElement webElement;
        WebDriverWait localWait = new WebDriverWait(driver, timeOutInSec);
        By byLocator = getLocator(locator, locatorType);
        try {
            localWait.until(ExpectedConditions.presenceOfElementLocated(byLocator));
            webElement = localWait.until(ExpectedConditions.visibilityOfElementLocated(byLocator));
        } catch (Exception e) {
            if (printElementNotFoundFlag) {
                logger.error(ELEMENT_NOT_FOUND + locator, e);
            }
            throw new ElementNotFoundException("Could not find element " + locator + " using " + locatorType);
        }

        return webElement;
    }

    /**
     * Finds and returns multiple elements with the default timeout.
     *
     * @param locator     the locator property
     * @param locatorType the locator type
     * @return a list of elements found by the provided locator
     */
    public List<WebElement> getWebElements(String locator, LocatorType locatorType) {
        return getWebElements(locator, locatorType, TIME_OUT_IN_SECONDS);
    }

    /**
     * Finds and returns multiple elements with the default timeout.
     *
     * @param locator      the locator property
     * @param locatorType  the locator type
     * @param timeOutInSec custom timeout
     * @return a list of elements found by the provided locator
     */
    public List<WebElement> getWebElements(String locator, LocatorType locatorType, Duration timeOutInSec) {
        List<WebElement> webElements;
        By byLocator = getLocator(locator, locatorType);
        WebDriverWait localWait = new WebDriverWait(driver, timeOutInSec);
        try {
            localWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(byLocator));
            webElements = localWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(byLocator));
        } catch (Exception e) {
            if (printElementNotFoundFlag) {
                logger.error(ELEMENT_NOT_FOUND + locator, e);
            }
            throw new ElementNotFoundException("Could not find element" + locator + " using " + locatorType);
        }
        return webElements;
    }

    /**
     * returns a list of elements.
     *
     * @param locatorNameFromProperty property key for the locator
     * @param locatorType             the locator type
     * @return web elements
     */
    public List<WebElement> getElements(String locatorNameFromProperty, LocatorType locatorType) {
        return getElements(locatorNameFromProperty, "", locatorType, TIME_OUT_IN_SECONDS);
    }

    /**
     * returns a list of elements.
     *
     * @param locatorNameFromProperty property key for locator
     * @param locatorType             the locator type
     * @param timeOutInSec            the amount of time to wait for the element to appear
     * @return web elements
     */
    public List<WebElement> getElements(String locatorNameFromProperty, LocatorType locatorType, Duration timeOutInSec) {
        return getElements(locatorNameFromProperty, "", locatorType, timeOutInSec);
    }

    /**
     * returns a list of elements.
     *
     * @param locatorNameFromProperty property key for locator
     * @param variableInLocator       if a locator contains a %s for string formatting, this is the variable being added
     * @param locatorType             the locator type
     * @return web elements
     */
    public List<WebElement> getElements(String locatorNameFromProperty, String variableInLocator,
                                        LocatorType locatorType) {
        return getElements(locatorNameFromProperty, variableInLocator, locatorType, TIME_OUT_IN_SECONDS);
    }

    /**
     * returns a list of web elements.
     *
     * @param locatorNameFromProperty the property key for the locator
     * @param variableInLocator       if a locator contains a %s for string formatting, this is the variable being added
     * @param locatorType             the locator type
     * @param timeOutInSec            the amount of time the driver will wait for the elements to appear
     * @return web elements
     */
    public List<WebElement> getElements(String locatorNameFromProperty, String variableInLocator,
                                        LocatorType locatorType, Duration timeOutInSec) {
        if (!elementLocators.containsKey(locatorNameFromProperty)) {
            elementLocators.put(locatorNameFromProperty, locatorNameFromProperty);
        }

        if (timeOutInSec == TIME_OUT_IN_SECONDS) {
            return getWebElements(String.format(elementLocators.get(locatorNameFromProperty), variableInLocator),
                    locatorType);
        }

        return getWebElements(String.format(elementLocators.get(locatorNameFromProperty), variableInLocator),
                locatorType, timeOutInSec);
    }

    /**
     * Check existence of an element.
     *
     * @param locator     element of interest
     * @param locatorType locator type
     * @return true if the element exists on the current page, false otherwise
     */
    public boolean doesElementExist(String locator, String changeablePartInLocator, LocatorType locatorType) {
        printElementNotFoundFlag = false;
        boolean exist;
        try {
            getElement(locator, changeablePartInLocator, locatorType);
            exist = true;
        } catch (Exception e) {
            exist = false;
        }
        printElementNotFoundFlag = true;
        return exist;
    }

    /**
     * To check if the element exists.
     *
     * @param locator      locator as String
     * @param aLocatorType LocatorType enum value
     * @return
     */
    public boolean doesElementExist(String locator, LocatorType aLocatorType) {
        return doesElementExist(locator, "", aLocatorType);
    }

    /**
     * To click for actions.
     *
     * @param webElement element
     * @return
     */
    public W clickOnElementActions(WebElement webElement) {
        Actions actionBuilder = new Actions(driver);
        actionBuilder.moveToElement(webElement).click().build().perform();
        return (W) this;
    }

    /**
     * Safely click on an element.
     *
     * @param locatorNameFromProperty path locator from property file.
     * @param locatorType             type of locator to be used.
     * @return instance of the page.
     */
    @SuppressWarnings("unchecked")
    public W clickOnElement(String locatorNameFromProperty, LocatorType locatorType) {
        clickOnElement(getElement(locatorNameFromProperty, locatorType));
        return (W) this;
    }

    /**
     * Safely click on an element.
     *
     * @param webElement      web element to click
     * @param scrollToElement true scrolls to element.
     * @return pageObject page object
     */
    @SuppressWarnings("unchecked")
    public W clickOnElement(WebElement webElement, boolean scrollToElement) {
        if (scrollToElement) {
            scrollToACertainElement(webElement);
        }
        webElement.click();
        return (W) this;
    }

    /**
     * Safely click on an element.
     *
     * @param webElement web element to click
     * @return pageObject page object
     */
    @SuppressWarnings("unchecked")
    public W clickOnElement(WebElement webElement) {
        clickOnElement(webElement, true);
        return (W) this;
    }

    /**
     * Forcefully click on an element using java script executor.
     *
     * @param locatorNameFromProperty locator property name.
     * @param locatorType             type of locator.
     * @return pageObject instance of the page.
     */
    @SuppressWarnings("unchecked")
    public W clickOnElementUsingJavaScript(String locatorNameFromProperty, LocatorType locatorType) {
        return clickOnElementUsingJavaScript(locatorNameFromProperty, "", locatorType);
    }

    /**
     * Forcefully click on an element using java script executor.
     *
     * @param locatorNameFromProperty locator property name.
     * @param locatorType             type of locator.
     * @param variablePart            variable part of locator.
     * @return pageObject instance of the page.
     */
    @SuppressWarnings("unchecked")
    public W clickOnElementUsingJavaScript(String locatorNameFromProperty, String variablePart,
                                           LocatorType locatorType) {
        By locator = getLocator(String.format(locatorNameFromProperty, variablePart), locatorType);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
        return (W) this;
    }

    /**
     * Forcefully click on an element using java script executor.
     *
     * @param element element to click
     * @return pageObject
     */
    @SuppressWarnings("unchecked")
    public W clickOnElementUsingJavaScript(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
        return (W) this;
    }

    /**
     * Safely double click on an element.
     *
     * @param webElement web element to double click
     * @return pageObject page object
     */
    @SuppressWarnings("unchecked")
    public W doubleClickOnElement(WebElement webElement) {
        Actions actionBuilder = new Actions(driver);
        actionBuilder.moveToElement(webElement).doubleClick().build().perform();
        return (W) this;
    }

    /**
     * Safely right click on an element.
     *
     * @param webElement web element to right click on
     * @return pageObject page object
     */
    @SuppressWarnings("unchecked")
    public W rightClickOnElement(WebElement webElement) {
        Actions actionBuilder = new Actions(driver);
        actionBuilder.moveToElement(webElement).contextClick().build().perform();
        return (W) this;
    }

    /**
     * Safely drag and drop an element.
     *
     * @param webElementSource element to be dragged
     * @param webElementTarget destination element for dragged element to be dropped
     * @return pageObject page object
     */
    @SuppressWarnings("unchecked")
    public W dragAndDropElement(WebElement webElementSource, WebElement webElementTarget) {
        Actions actionBuilder = new Actions(driver);
        actionBuilder.dragAndDrop(webElementSource, webElementTarget).build().perform();
        return (W) this;
    }

    /**
     * hover the mouse over the given element.
     *
     * @param webElement web element to hover
     * @return pageObject
     */
    @SuppressWarnings("unchecked")
    public W hoverOnElement(WebElement webElement) {
        Actions actionBuilder = new Actions(driver);
        actionBuilder.moveToElement(webElement).build().perform();
        return (W) this;
    }

    /**
     * Safely mouse hover on an element and get text.
     *
     * @param webElement Element hovered on.
     * @return Text
     */
    public String getHoverOnElementText(WebElement webElement) {
        hoverOnElement(webElement);
        return getElement("pointerEventsXpath", LocatorType.XPATH).getText();
    }

    /**
     * Scroll to a certain element.
     */
    @SuppressWarnings("unchecked")
    public W scrollToACertainElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        return (W) this;
    }

    /**
     * Select and unselect a checkbox.
     *
     * @param checkBox checkbox object
     * @param select   true to select, otherwise deselect
     * @return page
     */
    @SuppressWarnings("unchecked")
    public W selectCheckBox(WebElement checkBox, boolean select) {
        if (checkBox.isSelected() != select) {
            scrollToACertainElement(checkBox);
            clickOnElementUsingJavaScript(checkBox);
        }
        return (W) this;
    }

    /**
     * Safely return text from an element.
     *
     * @param locatorNameFromProperty path locator from property file.
     * @param locatorType             type of locator to be used.
     * @return displayed text of an element
     */
    public String getElementText(String locatorNameFromProperty, LocatorType locatorType) {
        return getElementText(getElement(locatorNameFromProperty, locatorType));
    }

    /**
     * Safely return text from an element.
     *
     * @param locatorNameFromProperty path locator from property file.
     * @param changeablePartInLocator changeable part in the locator.
     * @param locatorType             type of locator to be used.
     * @return displayed text of an element.
     */
    public String getElementText(String locatorNameFromProperty, String changeablePartInLocator,
                                 LocatorType locatorType) {
        return getElementText(getElement(locatorNameFromProperty, changeablePartInLocator, locatorType));
    }

    /**
     * Safely return text from an element.
     *
     * @param element WebElement.
     * @return displayed text of an element
     */
    public String getElementText(WebElement element) {
        String text = element.getText();
        if (text.length() == 0) {
            text = element.getAttribute("innerHTML");
        }
        return text;
    }

    @SuppressWarnings("unchecked")
    public W findPageElement(By by) {
        driver.findElement(by);
        return (W) this;
    }

    /**
     * click on element with location.
     *
     * @param elementLocation location of the element
     * @return page
     */
    @SuppressWarnings("unchecked")
    public W click(By elementLocation) {
        driver.findElement(elementLocation).click();
        return (W) this;
    }

    /**
     * Scroll a page to the top.
     * USEFUL WHERE ELEMENT ARE OVERLAPPED BY MENU BAR AND NAVIGATE TO SAME PAGE DOES NOT SCROLL TO TOP POSITION.
     *
     * @return pageObject
     */
    @SuppressWarnings("unchecked")
    public W scrollToTopUsingJavaScript() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,-document.body.scrollHeight)");
        return (W) this;
    }

    /**
     * Scroll a page to the bottom.
     *
     * @return pageObject
     */
    @SuppressWarnings("unchecked")
    public W scrollToBottomUsingJavaScript() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
        return (W) this;
    }

    /**
     * fill text field.
     *
     * @param txtField text field
     * @param value    value to be filled
     * @return the page itself
     */
    @SuppressWarnings("unchecked")
    public W fillTextFieldUsingJavaScript(WebElement txtField, String value) {
        new Actions(driver).moveToElement(txtField).click().perform();

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = arguments[1];", txtField, "");
        js.executeScript("arguments[0].value = arguments[1];", txtField, value);

        try {
            wait.until(localDriver -> txtField.getAttribute("value").length() != 0);
        } catch (NullPointerException ex) {
            logger.error("returns null for value attribute.", ex);
        }
        return (W) this;
    }

    /**
     * Fill out text field safely.
     *
     * @param aTxtField    text field
     * @param aTextToWrite content to write to
     * @return pageObject
     */
    @SuppressWarnings("unchecked")
    public W fillTextField(WebElement aTxtField, String aTextToWrite) {
        pause("wait for the field to be editable", 500);
        if (aTxtField.isDisplayed() && aTxtField.isEnabled() && aTextToWrite.length() > 0) {
            aTxtField.clear();
            wait.until(ExpectedConditions.visibilityOf(aTxtField));

            aTxtField.sendKeys(aTextToWrite);

            try {
                wait.until(localDriver -> aTxtField.getAttribute("value").length() == aTextToWrite.length());
            } catch (NullPointerException ex) {
                logger.error("Tiny MCE returns null for value attribute.", ex);
            }
        }
        return (W) this;
    }

    /**
     * write text for the element.
     *
     * @param elementLocation element location
     * @param text            text to be written
     */
    @SuppressWarnings("unchecked")
    public W type(By elementLocation, String text) {
        driver.findElement(elementLocation).clear();
        driver.findElement(elementLocation).sendKeys(text);
        return (W) this;
    }

    /**
     * go to a page.
     *
     * @param pageUrl page url
     * @return page
     */
    @SuppressWarnings("unchecked")
    public W goToPage(URL pageUrl) {
        driver.get(pageUrl.toString());
        wait.until(ExpectedConditions.urlContains((pageUrl.toString())));
        return (W) this;
    }

    /**
     * go to a page.
     *
     * @param pageUrl     page url
     * @param redirectUrl partial or full redirect url
     * @return page that contains redirect url
     */
    @SuppressWarnings("unchecked")
    public W goToPage(URL pageUrl, String redirectUrl) {
        driver.get(pageUrl.toString());
        wait.until(ExpectedConditions.urlContains((redirectUrl)));
        return (W) this;
    }

    /**
     * Just go to a page.  No need to check URL, that times out with Rest URLs
     *
     * @param pageUrl page url
     * @return page
     */
    public W getAPage(URL pageUrl) {
        driver.get(pageUrl.toString());
        return (W) this;
    }

    /**
     * check if element present.
     *
     * @param by by locator
     * @return true if present
     */
    public boolean checkIfPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Element not found " + by, e);
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * verify if text present.
     *
     * @param text text to be check
     * @return true if the text present
     */
    public boolean isTextPresent(String text) {
        try {
            return driver.getPageSource().contains(text);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Switch driver to child window/tab and wait for desired url.
     *
     * @param waitForContainingUrl the string to wait for in url.
     * @return page object
     */
    public W switchToChildWindow(String waitForContainingUrl) {
        String topWindowHandle = null;
        Set<String> windowHandles = driver.getWindowHandles();
        Iterator<String> iterator = windowHandles.iterator();
        while (iterator.hasNext()) {
            topWindowHandle = iterator.next();
        }

        driver.switchTo().window(topWindowHandle);
        try {
            pause("wait for handle", 1000); //Give some room to completely take over the handle of child window/tab.
            if (waitForContainingUrl.length() > 0) {
                wait.until(ExpectedConditions.urlContains(waitForContainingUrl));
            }
        } catch (Exception e) {
            logger.error("Window not found", e);
        }
        return (W) this;
    }

    /**
     * NOTE: MAKE SURE TO CALL {@link #resetDefaultWait()} after your work done.
     * Not recommended to use in general purpose use. Use it only for special cases.
     *
     * @param timeOutInSec timeout value in seconds.
     */
    public void changeDefaultWait(Duration timeOutInSec) {
        wait = new WebDriverWait(driver, timeOutInSec);
    }

    /**
     * Reset the wait to default time out.
     */
    public void resetDefaultWait() {
        wait = new WebDriverWait(driver, WEB_DRIVER_TIMEOUT_IN_SECONDS);
    }

}
