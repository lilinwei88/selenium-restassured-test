package com.test.my.e2e.driver;

public class DefaultCapabilities {

    private String browser;
    private String browserVersion;
    private String platformName;
    private String platformVersion;
    private String deviceName;
    private String manufacturer;
    private String model;
    private String location;
    private String resolution;
    private boolean incognito;
    private boolean headless;

    public DefaultCapabilities(String browser, String browserVersion, String platformName, String platformVersion) {
        this.browser = browser;
        this.browserVersion = browserVersion;
        this.platformName = platformName;
        this.platformVersion = platformVersion;
    }
    /**
     * DefaultCapabilities constructor use to run headless incongnito chrome.
     *
     *
     * @param headless         if true run in headless mode
     */
    public DefaultCapabilities(boolean headless) {
        this.browser = "chrome";
        this.incognito = true;
        this.headless = headless;
    }

    /**
     * DefaultCapabilities constructor.
     *
     * @param browser          browser
     * @param browserVersion   browser version
     * @param platformName     platform name
     * @param platformVersion  platform version
     * @param location         location of the device
     * @param resolution       resolution of the device
     * @param incognito        if true run in incognito mode
     * @param headless         if true run in headless mode
     */
    public DefaultCapabilities(String browser, String browserVersion, String platformName, String platformVersion,
                               String location, String resolution, boolean incognito, boolean headless) {
        this(browser, browserVersion, platformName, platformVersion);
        this.location = location;
        this.resolution = resolution;
        this.incognito = incognito;
        this.headless = headless;
    }

    /**
     * DefaultCapabilities constructor.
     *
     * @param browser          browser
     * @param browserVersion   browser version
     * @param platformName     platform name
     * @param platformVersion  platform version
     * @param deviceName       device name
     * @param manufacturer     manufacture e.g. apple, google
     * @param model            model e.g. pixel 3
     * @param location         location of the device
     * @param resolution       resolution of the device
     * @param incognito        if true run in incognito mode
     * @param headless         if true run in headless mode
     */
    public DefaultCapabilities(String browser, String browserVersion, String platformName, String platformVersion,
                               String deviceName, String manufacturer, String model, String location,
                               String resolution, boolean incognito, boolean headless) {
        this(browser, browserVersion, platformName, platformVersion);
        this.deviceName = deviceName;
        this.manufacturer = manufacturer;
        this.model = model;
        this.location = location;
        this.resolution = resolution;
        this.incognito = incognito;
        this.headless = headless;
    }

    @Override
    public String toString() {
        return "DefaultCapabilities{" +
                "browser='" + browser + '\'' +
                ", browserVersion='" + browserVersion + '\'' +
                ", platformName='" + platformName + '\'' +
                ", platformVersion='" + platformVersion + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", location='" + location + '\'' +
                ", resolution='" + resolution + '\'' +
                ", incognito=" + incognito +
                ", headless=" + headless +
                '}';
    }

    public String getBrowser() {
        return browser;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getPlatformVersion() {
        return platformVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getLocation() {
        return location;
    }

    public String getResolution() {
        return resolution;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public boolean isIncognito() {
        return incognito;
    }

    public boolean isHeadless() {
        return headless;
    }
}
