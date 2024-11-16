package com.reddit.tests.application;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.reddit.helpers.TestUtils; // Import TestUtils

import java.net.MalformedURLException;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.URL;
import java.util.NoSuchElementException;

public class RedditTest {

    static AppiumDriver driver;

    public static void main(String[] args) throws MalformedURLException {
        openMobileApp();
    }

    @BeforeClass
    public static void openMobileApp()throws MalformedURLException { // used to open app
        UiAutomator2Options connectionOptions = new UiAutomator2Options()
                .setDeviceName("Pixel7") // emulated device name  Pixel7 // Mipad4
                .setUdid("emulator-5554") // from 'adb devices' output  emulator-5554 // 8dc5ccfd
                .setPlatformName("Android") // emulated device platform
                .setPlatformVersion("15") // android version // 8.1.0
                .setAutomationName("UiAutomator2") // appium driver name
                .setAppPackage("com.reddit.frontpage") // from apkinfo app
                .setAppActivity("com.reddit.launch.main.MainActivity"); // from apkinfo app

        URL appiumServerUrl = new URL("http://127.0.0.1:4723");
        driver = new AppiumDriver(appiumServerUrl, connectionOptions);

        System.out.println("Application started!");
    }

    @Test
    public void testLoginFailure() {
        // Validate the title text
        String expectedTitleTextFirst = "Log in to Reddit";
        TestUtils.validateTitleText(driver, "//android.widget.TextView[@resource-id=\"onboarding_title\"]", expectedTitleTextFirst, "log_in_to_reddit");

        login("testusername", "testpassword");

        // Locate title element and get its text
        String titleText = "";
        try {
            titleText = driver.findElement(
                    AppiumBy.xpath("//android.widget.TextView[@resource-id=\"login_title\"]")
            ).getText();
        } catch (Exception exception) {
            Assert.fail("Element not found: login_title", exception);
        }

        String expectedTitleText = "Enter your login information";
        Assert.assertEquals(titleText, expectedTitleText, "Login title text doesn't match, means user logged in!");
    }

    private void login(String username, String password) throws NoSuchElementException{
        try {
            driver.findElement(AppiumBy.xpath("//android.widget.Button[@content-desc=\"Use email or username\"]")).click();
            driver.findElement(AppiumBy.xpath("(//android.widget.EditText[@resource-id=\"text_auto_fill\"])[1]")).sendKeys(username);
            driver.findElement(AppiumBy.xpath("(//android.widget.EditText[@resource-id=\"text_auto_fill\"])[2]")).sendKeys(password);
            driver.findElement(AppiumBy.xpath("//android.view.View[@resource-id=\"continue_button\"]")).click();
        } catch (NoSuchElementException exception) {
            System.out.println("Element not found: " + exception.getMessage());
            // Take screenshot on failure
            TestUtils.takeScreenshot(driver, "login_error_screenshot");
        }
    }

    @AfterClass
    public static void closeApp() {
        if (driver != null) {
            driver.quit();
            System.out.println("Application closed!");
        }
    }
}
