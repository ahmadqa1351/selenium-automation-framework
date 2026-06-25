package com.qa.framework.utils;

import com.qa.framework.config.ConfigReader;
import com.qa.framework.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for database operations and validations
 * 
 * Features:
 * - Database connection management
 * - Query execution (SELECT, INSERT, UPDATE, DELETE)
 * - Result set processing
 * - Data validation
 * 
 * Design Pattern: Utility Class
 * Thread Safety: Not thread-safe for write operations
 * 
 * Note: Requires database driver in classpath
 */
public class DatabaseUtils {
    private static final Logger logger = LogManager.getLogger(DatabaseUtils.class);
    private static final ConfigReader configReader = ConfigReader.getInstance();

    /**
     * Get database connection
     * 
     * @return Database connection
     */
    public static Connection getConnection() {
        try {
            String driver = configReader.getProperty("db.driver");
            String url = configReader.getProperty("db.url");
            String username = configReader.getProperty("db.username");
            String password = configReader.getProperty("db.password");

            if (driver == null || url == null) {
                throw new FrameworkException("Database configuration not found");
            }

            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            logger.info("Database connection established");
            return connection;

        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found", e);
            throw new FrameworkException("Database driver not found", e);
        } catch (Exception e) {
            logger.error("Failed to establish database connection", e);
            throw new FrameworkException("Database connection failed", e);
        }
    }

    /**
     * Execute SELECT query and return results as list of maps
     * 
     * @param query SQL query
     * @return List of maps containing result rows
     */
    public static List<Map<String, Object>> executeSelectQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            logger.info("Executing SELECT query: {}", query);
            
            int columnCount = rs.getMetaData().getColumnCount();
            
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = rs.getMetaData().getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
            
            logger.info("Query returned {} rows", results.size());
            
        } catch (Exception e) {
            logger.error("Error executing SELECT query: {}", query, e);
            throw new FrameworkException("Database SELECT query failed", e);
        }
        
        return results;
    }

    /**
     * Execute UPDATE/INSERT/DELETE query
     * 
     * @param query SQL query
     * @return Number of rows affected
     */
    public static int executeUpdateQuery(String query) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            logger.info("Executing UPDATE/INSERT/DELETE query: {}", query);
            int rowsAffected = stmt.executeUpdate(query);
            logger.info("Query affected {} rows", rowsAffected);
            
            return rowsAffected;
            
        } catch (Exception e) {
            logger.error("Error executing UPDATE/INSERT/DELETE query: {}", query, e);
            throw new FrameworkException("Database UPDATE query failed", e);
        }
    }

    /**
     * Get single value from database
     * 
     * @param query SQL query
     * @return Single value result
     */
    public static Object getSingleValue(String query) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            logger.info("Executing query: {}", query);
            
            if (rs.next()) {
                Object value = rs.getObject(1);
                logger.debug("Retrieved value: {}", value);
                return value;
            }
            
            logger.warn("No results found for query: {}", query);
            return null;
            
        } catch (Exception e) {
            logger.error("Error retrieving single value: {}", query, e);
            throw new FrameworkException("Database query failed", e);
        }
    }

    /**
     * Verify data exists in database
     * 
     * @param query SQL query to check
     * @return true if data exists
     */
    public static boolean verifyDataExists(String query) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            logger.info("Verifying data existence with query: {}", query);
            boolean exists = rs.next();
            logger.info("Data exists: {}", exists);
            
            return exists;
            
        } catch (Exception e) {
            logger.error("Error verifying data existence: {}", query, e);
            throw new FrameworkException("Database verification failed", e);
        }
    }

    /**
     * Get row count from table
     * 
     * @param tableName Table name
     * @return Row count
     */
    public static int getRowCount(String tableName) {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try {
            Object count = getSingleValue(query);
            return count != null ? Integer.parseInt(count.toString()) : 0;
        } catch (Exception e) {
            logger.error("Error getting row count for table: {}", tableName, e);
            return 0;
        }
    }

    /**
     * Delete all records from table
     * 
     * @param tableName Table name
     * @return Number of rows deleted
     */
    public static int deleteAllRecords(String tableName) {
        String query = "DELETE FROM " + tableName;
        return executeUpdateQuery(query);
    }

    /**
     * Close database connection
     * 
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (Exception e) {
            logger.error("Error closing database connection", e);
        }
    }
}
