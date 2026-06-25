# Extending the Framework - Developer Guide

## 🏗️ Adding New Page Objects

### Step 1: Create New Page Class

```java
package com.qa.pages;

import com.qa.framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for [Page Name]
 * Contains all elements and actions for [Page Description]
 */
public class YourPage extends BasePage {
    
    // ============= Web Elements =============
    @FindBy(id = "element_id")
    private WebElement elementName;
    
    @FindBy(xpath = "//button[contains(text(), 'Click')]") 
    private WebElement clickButton;
    
    // By locators for alternative access
    private final By elementLocator = By.id("element_id");
    
    // ============= Page Actions =============
    
    public void performAction() {
        logger.info("Performing action");
        click(elementName);
    }
    
    public String getElementText() {
        logger.info("Getting element text");
        return getText(elementName);
    }
    
    public boolean isElementDisplayed() {
        logger.info("Checking element display");
        return isElementDisplayed(elementName);
    }
}
```

### Step 2: Use in Tests

```java
public class YourTests extends BaseTest {
    
    @Test
    public void testYourFeature() {
        YourPage page = new YourPage();
        page.performAction();
        Assert.assertTrue(page.isElementDisplayed());
    }
}
```

## ✍️ Adding New Test Classes

### Step 1: Create Test Class

```java
package com.qa.tests.your_feature;

import com.qa.framework.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test class for [Feature Name]
 * Groups: smoke, regression
 */
public class YourFeatureTests extends BaseTest {
    
    @Test(groups = {"smoke", "regression"}, description = "Test description")
    public void testSomething() {
        logger.info("Test: Test something");
        
        // Setup
        // Execute
        // Verify
        Assert.assertTrue(true);
    }
    
    @Test(groups = {"regression"}, 
          dataProvider = "excelDataProvider",
          dataProviderClass = TestDataProvider.class)
    public void testWithData(Map<String, String> testData) {
        String testValue = testData.get("column_name");
        // Use test data
    }
}
```

### Step 2: Register Test Group

Update `testng.xml`:

```xml
<test name="Your Feature Tests">
    <groups>
        <run>
            <include name="smoke" />
            <include name="regression" />
        </run>
    </groups>
    <packages>
        <package name="com.qa.tests.your_feature.*" />
    </packages>
</test>
```

## 🛠️ Creating Custom Utilities

### Step 1: Create Utility Class

```java
package com.qa.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Custom utility for [Purpose]
 */
public class YourCustomUtils {
    private static final Logger logger = LogManager.getLogger(YourCustomUtils.class);
    
    /**
     * Method description
     * @param param Parameter description
     * @return Return value description
     */
    public static String performCustomAction(String param) {
        try {
            logger.debug("Performing custom action with: {}", param);
            // Implementation
            return "result";
        } catch (Exception e) {
            logger.error("Error in custom action", e);
            throw new FrameworkException("Custom action failed", e);
        }
    }
}
```

### Step 2: Use in Tests

```java
public class YourTests extends BaseTest {
    @Test
    public void testWithCustomUtil() {
        String result = YourCustomUtils.performCustomAction("input");
        Assert.assertEquals(result, "expected");
    }
}
```

## 🎯 Adding New Test Data Providers

### Step 1: Add Test Data File

Create `src/test/resources/data/yourData.json`:

```json
[
  {
    "username": "user1",
    "password": "pass1",
    "expectedResult": "success"
  },
  {
    "username": "user2",
    "password": "pass2",
    "expectedResult": "failure"
  }
]
```

### Step 2: Create Data Provider Method

```java
public class YourDataProvider {
    
    @DataProvider(name = "yourDataProvider")
    public Object[][] yourDataProvider() {
        List<Map<String, Object>> testData = 
            JsonUtils.readJsonArray("src/test/resources/data/yourData.json");
        
        Object[][] dataArray = new Object[testData.size()][1];
        for (int i = 0; i < testData.size(); i++) {
            dataArray[i][0] = testData.get(i);
        }
        return dataArray;
    }
}
```

### Step 3: Use in Tests

```java
@Test(dataProvider = "yourDataProvider", dataProviderClass = YourDataProvider.class)
public void testWithCustomData(Map<String, Object> testData) {
    String username = (String) testData.get("username");
    // Use test data
}
```

## 📋 Adding Custom Listeners

### Step 1: Create Listener Class

```java
package com.qa.framework.listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Custom listener for [Purpose]
 */
public class CustomListener implements ITestListener {
    private static final Logger logger = LogManager.getLogger(CustomListener.class);
    
    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Custom action on test start");
        // Custom implementation
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Custom action on test success");
        // Custom implementation
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("Custom action on test failure");
        // Custom implementation
    }
}
```

### Step 2: Register Listener

In `testng.xml`:

```xml
<listeners>
    <listener class-name="com.qa.framework.listeners.CustomListener" />
</listeners>
```

## 🌐 Adding New Browser Support

### Step 1: Update BrowserType Enum

```java
public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    YOUR_BROWSER("your_browser");
    // ...
}
```

### Step 2: Add Browser Options in DriverFactory

```java
private WebDriver createYourBrowserDriver() {
    WebDriverManager.yourbrowserdriver().setup();
    YourBrowserOptions options = getYourBrowserOptions();
    return new YourBrowserDriver(options);
}

private YourBrowserOptions getYourBrowserOptions() {
    YourBrowserOptions options = new YourBrowserOptions();
    // Configure options
    return options;
}
```

### Step 3: Update Switch Statement

```java
switch (browserType) {
    case YOUR_BROWSER:
        driver = createYourBrowserDriver();
        break;
    // ...
}
```

## 🗄️ Adding Database Support

### Step 1: Create DatabaseUtils

```java
public class DatabaseUtils {
    private static final Logger logger = LogManager.getLogger(DatabaseUtils.class);
    
    public static Connection getConnection() {
        try {
            ConfigReader config = ConfigReader.getInstance();
            String driver = config.getProperty("db.driver");
            String url = config.getProperty("db.url");
            String username = config.getProperty("db.username");
            String password = config.getProperty("db.password");
            
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("Failed to get database connection", e);
            throw new FrameworkException("Database connection failed", e);
        }
    }
}
```

### Step 2: Use in Tests

```java
public class DatabaseTests extends BaseTest {
    @Test
    public void testDatabaseOperation() {
        Connection conn = DatabaseUtils.getConnection();
        // Execute queries
    }
}
```

## 🔗 Adding API Testing Support

### Step 1: Create RestApiUtils

```java
public class RestApiUtils {
    private static final Logger logger = LogManager.getLogger(RestApiUtils.class);
    
    public static Response getRequest(String endpoint) {
        Response response = RestAssured.get(endpoint);
        logger.info("GET request to: {} - Status: {}", endpoint, response.statusCode());
        return response;
    }
    
    public static Response postRequest(String endpoint, String body) {
        Response response = RestAssured.given()
            .body(body)
            .post(endpoint);
        logger.info("POST request to: {} - Status: {}", endpoint, response.statusCode());
        return response;
    }
}
```

### Step 2: Use in Tests

```java
public class ApiTests extends BaseTest {
    @Test
    public void testApiEndpoint() {
        Response response = RestApiUtils.getRequest("/api/endpoint");
        Assert.assertEquals(response.statusCode(), 200);
    }
}
```

## 📊 Custom Assertions

### Step 1: Create Assertions Class

```java
public class CustomAssertions {
    private static final Logger logger = LogManager.getLogger(CustomAssertions.class);
    
    public static void assertElementPresent(By locator, String message) {
        try {
            DriverManager.getDriver().findElement(locator);
            logger.info("Element found: {}", message);
        } catch (NoSuchElementException e) {
            logger.error("Element not found: {}", message);
            Assert.fail(message);
        }
    }
    
    public static void assertTextPresent(String text) {
        String pageText = DriverManager.getDriver().getPageSource();
        Assert.assertTrue(pageText.contains(text), "Text not found: " + text);
    }
}
```

### Step 2: Use in Tests

```java
public class YourTests extends BaseTest {
    @Test
    public void testCustomAssertions() {
        CustomAssertions.assertElementPresent(
            By.id("element"), 
            "Element should be present"
        );
    }
}
```

## 🔄 Customizing Test Execution

### Add Test Retry Logic

```java
@Test(retryAnalyzer = RetryAnalyzer.class)
public void flakeyTest() {
    // This test will retry on failure
}
```

### Add Test Dependencies

```java
@Test(dependsOnMethods = {"testLoginSuccess"})
public void testAfterLogin() {
    // Runs only after testLoginSuccess passes
}
```

## ✅ Extension Checklist

- [ ] Follow existing naming conventions
- [ ] Add proper logging
- [ ] Include Javadoc comments
- [ ] Handle exceptions appropriately
- [ ] Add to relevant test groups/suites
- [ ] Test your implementation
- [ ] Update documentation
- [ ] Follow SOLID principles
- [ ] Avoid code duplication
- [ ] Add meaningful assertions

---

**Need Help Extending?**
- Review existing implementations for patterns
- Check the Architecture guide: `docs/ARCHITECTURE.md`
- Follow the SOLID principles
- Write clean, maintainable code
