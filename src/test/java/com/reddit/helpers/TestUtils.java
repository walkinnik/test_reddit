package com.reddit.helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestUtils {

    /**
     * Takes a screenshot and saves it with a unique name based on the current timestamp.
     *
     * @param driver   The Appium driver.
     * @param fileName The base name of the file to save the screenshot as.
     */
    public static void takeScreenshot(AppiumDriver driver, String fileName) {
        // Generate timestamp for unique screenshot name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotName = fileName + "_" + timestamp + ".png";

        // Take screenshot
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File("src/test/resources/screenshots/" + screenshotName);  // Save in 'screenshots' folder

        try {
            // Ensure the 'screenshots' directory exists
            FileUtils.forceMkdir(destFile.getParentFile());
            // Save the screenshot to the specified file
            FileUtils.copyFile(srcFile, destFile);
            System.out.println("Screenshot saved: " + destFile.getAbsolutePath());
        } catch (IOException exception) {
            System.out.println("Error while saving screenshot: " + exception.getMessage());
        }
    }

    /**
     * Validates the title text of a screen and takes a screenshot if it doesn't match.
     *
     * @param driver            The Appium driver.
     * @param xpath             The XPath of the title element.
     * @param expectedTitleText The expected title text.
     * @param screenshotName    The base name for the screenshot in case of a mismatch.
     */
    public static void validateTitleText(AppiumDriver driver, String xpath, String expectedTitleText, String screenshotName) {
        String titleText = "";
        try {
            // Locate title element and get its text
            titleText = driver.findElement(AppiumBy.xpath(xpath)).getText();
        } catch (Exception exception) {
            // Capture a screenshot if the element is not found
            takeScreenshot(driver, screenshotName + "_not_found");
            Assert.fail("Element not found: " + screenshotName, exception);
        }

        // Validate the title text
        if (!titleText.equals(expectedTitleText)) {
            // Capture a screenshot if the title text doesn't match
            takeScreenshot(driver, screenshotName + "_mismatch");
        }
    }

    /**
     * Automates the login process in the Reddit mobile application.
     * This method interacts with the app's login interface to enter the provided credentials
     * and handle various potential scenarios during the login process, such as saved accounts pop-ups
     * or missing fields.
     *
     * @param driver   The Appium driver.
     * @param username The username or email address to be used for login.
     * @param password The corresponding password for the provided username.
     * @param wait     The WebDriverWait instance to wait for elements.
     * @throws NoSuchElementException If an expected element on the login screen is not found.
     */
    public static void login(AppiumDriver driver, String username, String password, WebDriverWait wait) throws NoSuchElementException {
        try {
            driver.findElement(AppiumBy.xpath("//android.widget.Button[@content-desc=\"Use email or username\"]")).click();
            try {
                // Check if the saved accounts pop-up is present
                if (isElementPresent(driver, AppiumBy.xpath("//android.widget.FrameLayout[@resource-id=\"com.reddit.frontpage:id/bottomsheet_header\"]"), wait)) {
                    System.out.println("Saved accounts pop-up detected. Dismissing it...");
                    // click on add account to continue test
                    driver.findElement(AppiumBy.xpath("//androidx.recyclerview.widget.RecyclerView[@resource-id=\"com.reddit.frontpage:id/account_picker_accounts\"]/android.view.ViewGroup[2]")).click();
                }
            } catch (Exception exception) {
                System.out.println("No saved accounts pop-up detected. - This is fine if no account has ever logged in.");
            }
            try {
                // Check if login page loaded properly
                if (isElementPresent(driver, AppiumBy.xpath("(//android.widget.EditText[@resource-id=\"text_auto_fill\"])[1]"), wait)) {
                    // click on add account to continue test
                    driver.findElement(AppiumBy.xpath("(//android.widget.EditText[@resource-id=\"text_auto_fill\"])[1]")).sendKeys(username);
                }
            } catch (Exception exception) {
                System.out.println("No email or username detected. Fiasco");
            }
            driver.findElement(AppiumBy.xpath("(//android.widget.EditText[@resource-id=\"text_auto_fill\"])[2]")).sendKeys(password);
            driver.findElement(AppiumBy.xpath("//android.view.View[@resource-id=\"continue_button\"]")).click();
        } catch (NoSuchElementException exception) {
            System.out.println("Element not found: " + exception.getMessage());
        }
    }

    /**
     * Utility method to check if an element is present.
     *
     * @param driver The Appium driver.
     * @param by     The locator for the element.
     * @param wait   The WebDriverWait instance.
     * @return true if the element is present, false otherwise.
     */
    public static boolean isElementPresent(AppiumDriver driver, By by, WebDriverWait wait) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(by));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
