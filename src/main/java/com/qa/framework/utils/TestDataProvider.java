package com.qa.framework.utils;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Test Data Provider for parameterized and data-driven testing
 * 
 * Features:
 * - Excel data provider
 * - JSON data provider
 * - CSV data provider
 * - Dynamic data loading
 * - Configurable data sources
 * 
 * Design Pattern: Data Provider Pattern
 * Usage: Used with TestNG @DataProvider annotation
 * 
 * Example:
 * @Test(dataProvider = "excelDataProvider", dataProviderClass = TestDataProvider.class)
 * public void testWithExcelData(Map<String, String> testData) {
 *     String username = testData.get("username");
 *     String password = testData.get("password");
 * }
 */
public class TestDataProvider {
    private static final Logger logger = LogManager.getLogger(TestDataProvider.class);
    private static final ConfigReader configReader = ConfigReader.getInstance();
    private static final String TEST_DATA_PATH = configReader.getProperty(
            "test.data.path",
            "src/test/resources/data"
    );

    /**
     * Excel data provider
     * Loads test data from Excel file
     * 
     * Usage: @Test(dataProvider = "excelDataProvider", dataProviderClass = TestDataProvider.class)
     * 
     * @param excelFile Excel file name (without path)
     * @param sheetName Sheet name in Excel
     * @return 2D array of test data
     */
    @DataProvider(name = "excelDataProvider", parallel = true)
    public Object[][] excelDataProvider(String excelFile, String sheetName) {
        try {
            String filePath = TEST_DATA_PATH + "/" + excelFile;
            List<Map<String, String>> testData = ExcelUtils.readExcelData(filePath, sheetName);
            
            Object[][] dataArray = new Object[testData.size()][1];
            for (int i = 0; i < testData.size(); i++) {
                dataArray[i][0] = testData.get(i);
            }
            
            logger.info("Loaded {} test cases from Excel", testData.size());
            return dataArray;
            
        } catch (Exception e) {
            logger.error("Error loading Excel test data", e);
            throw new FrameworkException("Failed to load Excel test data", e);
        }
    }

    /**
     * JSON data provider
     * Loads test data from JSON file
     * 
     * Usage: @Test(dataProvider = "jsonDataProvider", dataProviderClass = TestDataProvider.class)
     * 
     * @param jsonFile JSON file name (without path)
     * @return 2D array of test data
     */
    @DataProvider(name = "jsonDataProvider", parallel = true)
    public Object[][] jsonDataProvider(String jsonFile) {
        try {
            String filePath = TEST_DATA_PATH + "/" + jsonFile;
            List<Map<String, Object>> testData = JsonUtils.readJsonArray(filePath);
            
            Object[][] dataArray = new Object[testData.size()][1];
            for (int i = 0; i < testData.size(); i++) {
                dataArray[i][0] = testData.get(i);
            }
            
            logger.info("Loaded {} test cases from JSON", testData.size());
            return dataArray;
            
        } catch (Exception e) {
            logger.error("Error loading JSON test data", e);
            throw new FrameworkException("Failed to load JSON test data", e);
        }
    }

    /**
     * CSV data provider
     * Loads test data from CSV file
     * 
     * Usage: @Test(dataProvider = "csvDataProvider", dataProviderClass = TestDataProvider.class)
     * 
     * @param csvFile CSV file name (without path)
     * @return 2D array of test data
     */
    @DataProvider(name = "csvDataProvider", parallel = true)
    public Object[][] csvDataProvider(String csvFile) {
        try {
            String filePath = TEST_DATA_PATH + "/" + csvFile;
            List<Map<String, String>> testData = CsvUtils.readCsvData(filePath);
            
            Object[][] dataArray = new Object[testData.size()][1];
            for (int i = 0; i < testData.size(); i++) {
                dataArray[i][0] = testData.get(i);
            }
            
            logger.info("Loaded {} test cases from CSV", testData.size());
            return dataArray;
            
        } catch (Exception e) {
            logger.error("Error loading CSV test data", e);
            throw new FrameworkException("Failed to load CSV test data", e);
        }
    }

    /**
     * Get test data as list (useful for custom data loading)
     * 
     * @param filePath File path (absolute or relative)
     * @param fileType File type: "excel", "json", or "csv"
     * @param sheetName Sheet name (for Excel files only)
     * @return List of test data maps
     */
    public static List<Map<?, ?>> getTestData(String filePath, String fileType, String sheetName) {
        try {
            switch (fileType.toLowerCase()) {
                case "excel":
                    return new ArrayList<>(ExcelUtils.readExcelData(filePath, sheetName));
                case "json":
                    return new ArrayList<>(JsonUtils.readJsonArray(filePath));
                case "csv":
                    return new ArrayList<>(CsvUtils.readCsvData(filePath));
                default:
                    logger.error("Unsupported file type: {}", fileType);
                    throw new FrameworkException("Unsupported file type: " + fileType);
            }
        } catch (Exception e) {
            logger.error("Error loading test data from {}", filePath, e);
            throw new FrameworkException("Failed to load test data", e);
        }
    }
}
