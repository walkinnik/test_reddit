package com.reddit.helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.AppiumBy;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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
}
