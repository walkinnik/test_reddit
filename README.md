
# Reddit Mobile Automation Test Suite

This repository contains an automated test suite for the Reddit mobile application using **Appium** and **TestNG**.

### **Test Framework**
- **Appium**: Automation tool for mobile applications.
- **TestNG**: Testing framework to execute and manage tests.
- **Java**: The programming language for the test scripts.

---

## **Prerequisites**

1. **Java 11 or above**  
   Make sure you have Java/JDK installed on your machine. You can verify this by running:
   ```bash
   java -version
   javac -version
   ```

2. **Maven**  
   This project uses Maven to handle dependencies and run tests. You can verify Maven installation by running:
   ```bash
   mvn -v
   ```

3. **Appium**  
   Appium should be installed and running on your machine. You can install it globally using npm:
   ```bash
   npm install -g appium
   ```
   Make sure to have UIAutomator2 driver installed for Appium.
   ```bash
   appium driver list --installed
   ```
   If it's not on the list you can install it using:
   ```bash
   appium driver install uiautomator2
   ```

4. **Android Emulator or Real Device**  
   Make sure to have an Android emulator or a real device connected. You can verify this by running:
   ```bash
   adb devices
   ```
   Make sure to have Reddit application installed either from Google Store or via apk download-installation. 
---

## **Setting Up the Project**

### 1. Clone the Repository
```bash
git clone https://github.com/walkinnik/test_reddit.git
cd test_reddit
```

### 2. Install Dependencies

Run the following command to install all required dependencies using Maven:
```bash
mvn clean install
```

---

## **Test Structure**

### 1. **Test Cases**
This project currently includes the following test cases:

- **testLoginFailure**: Tests login failure with invalid credentials.
- **testLoginSuccess**: Tests successful login with valid credentials.
- **testNavigation**: Verifies navigation within the app, including moving between the Home, Communities, and Search sections. 
Searches for 'java' and validates first output result.

### 2. **TestUtils Helper Class**
A helper class that provides reusable methods for:
- Validating text elements.
- Performing login operations.
- Checking element presence.
- Taking screenshots in case of failures.

---

# **Configuration**

The test suite utilizes the following configuration parameters. These parameters are loaded from a YAML configuration file located in `src/test/resources/config.yaml`:

```yaml
deviceName: "Pixel7"
udid: "emulator-5554"
platformName: "Android"
platformVersion: "15"
automationName: "UiAutomator2"
appPackage: "com.reddit.frontpage"
appActivity: "com.reddit.launch.main.MainActivity"
appiumServerUrl: "http://127.0.0.1:4723"
invalidLogin: ""
invalidPassword: ""
validLogin: ""
validPassword: ""
```
Parameter Descriptions:

- deviceName: Name of the emulator or real device.
- udid: Unique device identifier from adb devices.
- platformName: Mobile operating system platform (e.g., Android).
- platformVersion: Version of the mobile operating system.
- automationName: Appium automation driver name.
- appPackage: Package name of the application under test.
- appActivity: Main activity of the application.
- appiumServerUrl: URL of the Appium server.
- invalidLogin / invalidPassword: Credentials for testing invalid login scenarios.
- validLogin / validPassword: Credentials for testing successful login scenarios.

### ! Important ! ###
Fill empty values for invalidLogin / invalidPassword and validLogin / validPassword before running tests. 


___

## **Running the Tests**

### 1. To run all tests:
```bash
mvn test
```

### 2. To run a specific test:
Use the following command, replacing `TestName` with the desired test method name:
```bash
mvn -Dtest=RedditTest#TestName test
```

Example:
```bash
mvn -Dtest=RedditTest#testLoginFailure test
```

---

## **Future Work**

- Add more test cases covering additional features like form submissions, validation messages, and more complex user workflows.
- Integrate with CI/CD tools like Jenkins for automated test execution on every commit.

---

## **License**

This project is not licensed.
