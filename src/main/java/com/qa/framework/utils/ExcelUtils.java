package com.qa.framework.utils;

import com.qa.framework.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading and writing Excel files for test data management
 * 
 * Features:
 * - Read data from Excel sheets
 * - Write data to Excel files
 * - Support for multiple data types (String, Integer, Double, Boolean)
 * - Automatic sheet handling
 * - Data validation
 * 
 * Design Pattern: Utility Class
 * Thread Safety: Not thread-safe for write operations
 */
public class ExcelUtils {
    private static final Logger logger = LogManager.getLogger(ExcelUtils.class);

    /**
     * Read all data from Excel sheet
     * Returns list of maps where each map represents a row
     * 
     * @param filePath Path to Excel file
     * @param sheetName Sheet name to read from
     * @return List of maps containing row data
     */
    public static List<Map<String, String>> readExcelData(String filePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' not found in Excel file: {}", sheetName, filePath);
                throw new FrameworkException("Sheet not found: " + sheetName);
            }
            
            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                logger.warn("Excel sheet is empty: {}", sheetName);
                return dataList;
            }
            
            // Get column headers
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValue(cell));
            }
            
            // Read data rows
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                
                Map<String, String> rowData = new HashMap<>();
                for (int cellIndex = 0; cellIndex < headers.size(); cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    String value = cell != null ? getCellValue(cell) : "";
                    rowData.put(headers.get(cellIndex), value);
                }
                
                dataList.add(rowData);
            }
            
            logger.info("Successfully read {} rows from sheet '{}'", dataList.size(), sheetName);
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            throw new FrameworkException("Failed to read Excel file", e);
        }
        
        return dataList;
    }

    /**
     * Read specific rows from Excel sheet
     * 
     * @param filePath Path to Excel file
     * @param sheetName Sheet name to read from
     * @param rowIndexes Row indexes to read (0-based, excluding header)
     * @return List of maps containing selected row data
     */
    public static List<Map<String, String>> readExcelDataByRows(String filePath, String sheetName, int... rowIndexes) {
        List<Map<String, String>> dataList = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' not found", sheetName);
                throw new FrameworkException("Sheet not found: " + sheetName);
            }
            
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                logger.warn("Excel sheet is empty");
                return dataList;
            }
            
            // Get headers
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(getCellValue(cell));
            }
            
            // Read specified rows
            for (int rowIndex : rowIndexes) {
                Row row = sheet.getRow(rowIndex + 1); // +1 to skip header
                if (row == null) continue;
                
                Map<String, String> rowData = new HashMap<>();
                for (int cellIndex = 0; cellIndex < headers.size(); cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    String value = cell != null ? getCellValue(cell) : "";
                    rowData.put(headers.get(cellIndex), value);
                }
                
                dataList.add(rowData);
            }
            
            logger.info("Successfully read {} rows from sheet '{}'", dataList.size(), sheetName);
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            throw new FrameworkException("Failed to read Excel file", e);
        }
        
        return dataList;
    }

    /**
     * Read single row from Excel
     * 
     * @param filePath Path to Excel file
     * @param sheetName Sheet name to read from
     * @param rowIndex Row index to read (0-based, excluding header)
     * @return Map containing row data
     */
    public static Map<String, String> readExcelRow(String filePath, String sheetName, int rowIndex) {
        List<Map<String, String>> data = readExcelDataByRows(filePath, sheetName, rowIndex);
        return data.isEmpty() ? new HashMap<>() : data.get(0);
    }

    /**
     * Get cell value as string regardless of cell type
     * 
     * @param cell Excel cell
     * @return Cell value as string
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Get number of rows in sheet
     * 
     * @param filePath Path to Excel file
     * @param sheetName Sheet name
     * @return Number of rows (excluding header)
     */
    public static int getRowCount(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                logger.error("Sheet '{}' not found", sheetName);
                return 0;
            }
            
            int rowCount = sheet.getLastRowNum();
            logger.debug("Row count in sheet '{}': {}", sheetName, rowCount);
            return rowCount;
            
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", filePath, e);
            return 0;
        }
    }

    /**
     * Write data to Excel file
     * 
     * @param filePath Path to Excel file
     * @param sheetName Sheet name
     * @param dataList List of maps containing data to write
     */
    public static void writeExcelData(String filePath, String sheetName, List<Map<String, String>> dataList) {
        if (dataList.isEmpty()) {
            logger.warn("No data to write");
            return;
        }
        
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(filePath)) {
            
            Sheet sheet = workbook.createSheet(sheetName);
            
            // Write header
            Row headerRow = sheet.createRow(0);
            List<String> headers = new ArrayList<>(dataList.get(0).keySet());
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
            }
            
            // Write data
            for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                Map<String, String> rowData = dataList.get(rowIndex);
                
                for (int cellIndex = 0; cellIndex < headers.size(); cellIndex++) {
                    Cell cell = row.createCell(cellIndex);
                    String value = rowData.getOrDefault(headers.get(cellIndex), "");
                    cell.setCellValue(value);
                }
            }
            
            workbook.write(fos);
            logger.info("Successfully wrote {} rows to Excel file: {}", dataList.size(), filePath);
            
        } catch (IOException e) {
            logger.error("Error writing to Excel file: {}", filePath, e);
            throw new FrameworkException("Failed to write to Excel file", e);
        }
    }
}
