# Configuration Guide - Selenium Automation Framework

## 📋 Configuration Overview

The framework supports multiple levels of configuration:
1. **Base Configuration** - Default settings for all environments
2. **Environment-Specific** - Dev, QA, UAT, Production overrides
3. **System Properties** - Runtime overrides via command line
4. **Environment Variables** - System-level configuration

## 🔧 Configuration Files

### Base Configuration: `config.properties`

Default settings applied to all environments:

```properties
# ============= Application Settings =============
base.url=https://www.saucedemo.com
api.base.url=https://api.example.com

# ============= Browser Settings =============
browser=chrome                          # Options: chrome, firefox, edge, safari
headless=false                          # Run browser in headless mode
window.maximize=true                    # Maximize browser window
accept.insecure.ssl=true                # Accept self-signed certificates
page.load.strategy=normal               # Options: normal, eager, none

# ============= Timeout Settings (seconds) =============
explicit.wait.timeout=10                # Explicit wait timeout
implicit.wait.timeout=5                 # Implicit wait timeout
page.load.timeout=15                    # Page load timeout
api.timeout=5000                        # API call timeout (milliseconds)

# ============= Screenshot Settings =============
capture.screenshot.on.failure=true      # Capture on test failure
capture.screenshot.on.success=false     # Capture on test success
screenshot.directory=reports/screenshots # Screenshot storage path

# ============= Report Settings =============
extent.report.path=reports/extent-reports           # Report directory
extent.report.name=TestExecutionReport              # Report name
attach.screenshot.to.report=true                    # Attach screenshots to report

# ============= Logging Settings =============
log.level=INFO                          # Options: DEBUG, INFO, WARN, ERROR
log.file=logs/automation.log            # Log file path

# ============= Test Data Settings =============
test.data.path=src/test/resources/data  # Test data directory

# ============= Parallel Execution =============
parallel.threads=4                      # Number of parallel threads

# ============= Retry Settings =============
retry.count=1                           # Number of retries for failed tests
retry.delay=2000                        # Delay between retries (ms)

# ============= Database Settings (if required) =============
db.driver=com.mysql.cj.jdbc.Driver     # Database driver
db.url=jdbc:mysql://localhost:3306/testdb  # Database URL
db.username=root                        # Database username
db.password=password                    # Database password
db.pool.size=5                          # Connection pool size
```

### Environment-Specific Configurations

#### Development: `config-dev.properties`
```properties
# Override specific properties for development
base.url=http://localhost:8080
browser=chrome
headless=false                  # See UI during development
explicit.wait.timeout=10
log.level=DEBUG                 # Verbose logging for debugging
parallel.threads=2              # Fewer threads during development
db.url=jdbc:mysql://localhost:3306/testdb_dev
```

#### QA: `config-qa.properties`
```properties
# QA environment configuration
base.url=https://qa.example.com
browser=chrome
headless=true                   # Run headless for CI/CD
explicit.wait.timeout=10
log.level=INFO
parallel.threads=4
db.url=jdbc:mysql://qa-db-server:3306/testdb_qa
```

#### UAT: `config-uat.properties`
```properties
# UAT environment configuration
base.url=https://uat.example.com
browser=chrome
headless=true
explicit.wait.timeout=15        # Longer timeout for UAT
log.level=INFO
parallel.threads=4
db.url=jdbc:mysql://uat-db-server:3306/testdb_uat
```

#### Production: `config-prod.properties`
```properties
# Production environment (smoke tests only)
base.url=https://www.example.com
browser=chrome
headless=true
explicit.wait.timeout=20        # Longest timeout for stability
log.level=INFO
parallel.threads=2              # Conservative threading
db.url=jdbc:mysql://prod-db-server:3306/testdb
```

## 🚀 Runtime Configuration

### Command Line Parameters

```bash
# Override browser
mvn clean test -Dbrowser=firefox

# Override environment
mvn clean test -Denvironment=qa

# Override base URL
mvn clean test -Dbase.url=http://custom-url.com

# Override log level
mvn clean test -Dlog.level=DEBUG

# Multiple overrides
mvn clean test -Dbrowser=chrome -Denvironment=uat -Dheadless=true

# With test group
mvn clean test -Dgroups="smoke" -Denvironment=qa -Dbrowser=firefox
```

### Maven Surefire Plugin Configuration

Edit `pom.xml` to customize test execution:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <!-- TestNG configuration -->
        <suiteXmlFiles>
            <suiteXmlFile>src/test/resources/testng.xml</suiteXmlFile>
        </suiteXmlFiles>
        
        <!-- Parallel execution -->
        <parallel>methods</parallel>
        <threadCount>4</threadCount>
        
        <!-- System properties -->
        <systemProperties>
            <property>
                <name>environment</name>
                <value>${environment}</value>
            </property>
            <property>
                <name>browser</name>
                <value>${browser}</value>
            </property>
        </systemProperties>
    </configuration>
</plugin>
```

## 🎯 Configuration Priority

Configurations are applied in the following order (highest to lowest priority):

1. **System Properties** (Command line: `-Dproperty=value`)
2. **Environment Variables** (System environment variables)
3. **Environment-Specific Config** (`config-{environment}.properties`)
4. **Base Configuration** (`config.properties`)
5. **Hardcoded Defaults** (In code)

## 📝 Adding Custom Properties

### Step 1: Add to Base Configuration
```properties
# config.properties
custom.property=default_value
```

### Step 2: Add to Environment-Specific Configs
```properties
# config-qa.properties
custom.property=qa_specific_value
```

### Step 3: Read in Code
```java
String customValue = ConfigReader.getInstance().getProperty("custom.property");
```

## 🔐 Sensitive Information

### Using Environment Variables for Secrets

```java
// In ConfigReader or custom utility
String password = System.getenv("DB_PASSWORD");
if (password == null) {
    password = configReader.getProperty("db.password");
}
```

### Using .env Files (Optional)

Create `.env` file (add to .gitignore):
```properties
DB_USERNAME=myuser
DB_PASSWORD=mypassword
API_KEY=my_api_key
```

Load in application:
```java
Dotenv dotenv = Dotenv.load();
String apiKey = dotenv.get("API_KEY");
```

## 🔄 Configuration Examples

### Example 1: Local Development
```bash
# Run tests locally with Chrome in non-headless mode
mvn clean test -Denvironment=dev -Dbrowser=chrome -Dheadless=false
```

### Example 2: CI/CD Pipeline
```bash
# Run in CI/CD with Firefox in headless mode
mvn clean test -Denvironment=qa -Dbrowser=firefox -Dheadless=true -DforkCount=4
```

### Example 3: Debug Mode
```bash
# Run with debugging enabled
mvn clean test -Denvironment=dev -Dlog.level=DEBUG -Dheadless=false
```

### Example 4: Smoke Tests Only
```bash
# Run only smoke tests in QA
mvn clean test -Dgroups="smoke" -Denvironment=qa -DforkCount=2
```

## 📊 Browser-Specific Configuration

### Chrome Configuration
```properties
browser=chrome
headless=true
window.maximize=true
accept.insecure.ssl=true
```

### Firefox Configuration
```properties
browser=firefox
headless=true
window.maximize=true
```

### Edge Configuration
```properties
browser=edge
headless=true
window.maximize=true
```

### Safari Configuration
```properties
browser=safari
headless=false    # Safari doesn't support headless
window.maximize=true
```

## 🌐 Multi-Environment Setup

### Adding New Environment

1. Create new config file: `config-staging.properties`
2. Add environment to enum: `Environment.java`
```java
public enum Environment {
    DEV("dev"),
    QA("qa"),
    UAT("uat"),
    STAGING("staging"),  // New
    PROD("prod");
}
```

3. Use in tests:
```bash
mvn clean test -Denvironment=staging
```

## ✅ Configuration Validation

```java
// In BaseTest or utility
public static void validateConfiguration() {
    ConfigReader config = ConfigReader.getInstance();
    
    // Validate required properties
    String baseUrl = config.getProperty("base.url");
    if (baseUrl == null || baseUrl.isEmpty()) {
        throw new FrameworkException("base.url is not configured");
    }
    
    // Validate URL format
    if (!CommonStringUtils.isValidUrl(baseUrl)) {
        throw new FrameworkException("Invalid base.url: " + baseUrl);
    }
    
    // Validate timeout values
    Integer timeout = config.getPropertyAsInteger("explicit.wait.timeout");
    if (timeout == null || timeout <= 0) {
        throw new FrameworkException("Invalid timeout configuration");
    }
}
```

## 🛠️ Troubleshooting Configuration Issues

### Issue: Properties Not Loading
```bash
# Solution: Check file encoding and path
mvn clean install
mvn test -X  # Enable debug output
```

### Issue: Environment Not Recognized
```bash
# Solution: Verify environment name matches enum
mvn clean test -Denvironment=qa  # lowercase
```

### Issue: Wrong Configuration Loaded
```bash
# Solution: Check configuration priority
# 1. Command line override
# 2. Environment-specific config
# 3. Base config
```

---

**Configuration Best Practices:**
- ✅ Never commit sensitive data (passwords, API keys)
- ✅ Use environment variables for production secrets
- ✅ Keep meaningful property names
- ✅ Document all custom properties
- ✅ Test configuration loading before test execution
- ✅ Use type-safe getters (getPropertyAsInteger, getPropertyAsBoolean)
