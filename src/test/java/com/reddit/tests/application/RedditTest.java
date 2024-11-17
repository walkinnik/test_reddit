package com.reddit.tests.application;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import com.reddit.helpers.TestUtils;

import java.net.MalformedURLException;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.URL;
import java.time.Duration;

public class RedditTest {

    static AppiumDriver driver;

    public static void main(String[] args) throws MalformedURLException {
        openMobileApp();
    }

    @BeforeMethod
    public static void openMobileApp() throws MalformedURLException {
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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Timeout of 20 seconds

        TestUtils.login(driver, "testusername", "testpassword", wait);

        // XPath for user icon that appears when user logged in
        String userIconXPath = "//android.widget.ImageView[@resource-id=\"com.reddit.frontpage:id/inner_user_icon\"]";

        // Check if the user icon is present
        boolean isUserIconPresent = TestUtils.isElementPresent(driver, AppiumBy.xpath(userIconXPath), wait);

        if (!isUserIconPresent) {
            System.out.println("Login failed as expected.");
        } else {
            /*
            If the icon is present, fail the test because login should not succeed with invalid credentials
            It highlights 'invalid username or password' in red, but couldn't find in appium inspector
            So this is a workaround
            */
            Assert.fail("Login succeeded unexpectedly!");
        }
    }

    @Test
    public void testLoginSuccess() {
        // Validate the title text
        String expectedTitleTextFirst = "Log in to Reddit";
        TestUtils.validateTitleText(driver, "//android.widget.TextView[@resource-id=\"onboarding_title\"]", expectedTitleTextFirst, "log_in_to_reddit");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Timeout of 20 seconds

        TestUtils.login(driver, "monkeyinspacetest", "SeeYouSpaceCowboy123$", wait);

        // Check if Reddit Notifications pop-up
        try {
            // Wait for the element with Reddit Notifications to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.android.permissioncontroller:id/permission_message\"]")
            ));
            if (TestUtils.isElementPresent(driver, AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.android.permissioncontroller:id/permission_message\"]"), wait)) {
                System.out.println("Reddit Notifications pop-up detected. Dismissing it...");
                // click on allow notifications
                driver.findElement(AppiumBy.xpath("//android.widget.Button[@resource-id=\"com.android.permissioncontroller:id/permission_allow_button\"]")).click();
                System.out.println("Reddit Notifications accepted");
            }
        } catch (Exception e) {
            System.out.println("No Reddit Notification pop-up detected. - This is fine if allowed on previous iterations.");
        }

        // XPath for user icon that appears when user logged in
        String userIconXPath = "//android.widget.ImageView[@resource-id=\"com.reddit.frontpage:id/inner_user_icon\"]";

        // Check if the user icon is present
        boolean isUserIconPresent = TestUtils.isElementPresent(driver, AppiumBy.xpath(userIconXPath), wait);

        if (isUserIconPresent) {
            System.out.println("Login succeeded as expected.");
        } else {
            // If the icon is not present, fail the test because login should have succeeded
            Assert.fail("Login failed unexpectedly!");
        }
    }

    @AfterMethod
    public static void closeApp() {
        if (driver != null) {
            driver.quit();
            System.out.println("Application closed!");
        }
    }
}
