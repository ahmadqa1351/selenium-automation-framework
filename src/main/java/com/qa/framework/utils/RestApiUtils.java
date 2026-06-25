package com.qa.framework.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.qa.framework.exceptions.FrameworkException;

import java.util.Map;

/**
 * Utility class for REST API testing
 * 
 * Features:
 * - GET, POST, PUT, DELETE request support
 * - Header and query parameter management
 * - Request/response logging
 * - Response validation
 * 
 * Design Pattern: Utility Class
 * Thread Safety: Thread-safe (RestAssured is thread-safe)
 */
public class RestApiUtils {
    private static final Logger logger = LogManager.getLogger(RestApiUtils.class);
    private static final com.qa.framework.config.ConfigReader configReader = 
            com.qa.framework.config.ConfigReader.getInstance();

    /**
     * Initialize RestAssured with base URL
     */
    public static void initializeRestAssured() {
        String baseUrl = configReader.getProperty("api.base.url");
        if (baseUrl != null && !baseUrl.isEmpty()) {
            RestAssured.baseURI = baseUrl;
            logger.info("RestAssured initialized with base URL: {}", baseUrl);
        }
    }

    /**
     * Perform GET request
     * 
     * @param endpoint API endpoint
     * @return Response object
     */
    public static Response getRequest(String endpoint) {
        try {
            logger.info("Performing GET request to: {}", endpoint);
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(endpoint)
                    .then()
                    .extract()
                    .response();
            
            logger.info("GET response status: {}", response.statusCode());
            return response;
        } catch (Exception e) {
            logger.error("GET request failed for endpoint: {}", endpoint, e);
            throw new FrameworkException("GET request failed", e);
        }
    }

    /**
     * Perform GET request with headers
     * 
     * @param endpoint API endpoint
     * @param headers Request headers
     * @return Response object
     */
    public static Response getRequest(String endpoint, Map<String, String> headers) {
        try {
            logger.info("Performing GET request to: {} with headers", endpoint);
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .headers(headers)
                    .when()
                    .get(endpoint)
                    .then()
                    .extract()
                    .response();
            
            logger.info("GET response status: {}", response.statusCode());
            return response;
        } catch (Exception e) {
            logger.error("GET request failed for endpoint: {}", endpoint, e);
            throw new FrameworkException("GET request failed", e);
        }
    }

    /**
     * Perform POST request
     * 
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    public static Response postRequest(String endpoint, String body) {
        try {
            logger.info("Performing POST request to: {}", endpoint);
            logger.debug("Request body: {}", body);
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when()
                    .post(endpoint)
                    .then()
                    .extract()
                    .response();
            
            logger.info("POST response status: {}", response.statusCode());
            return response;
        } catch (Exception e) {
            logger.error("POST request failed for endpoint: {}", endpoint, e);
            throw new FrameworkException("POST request failed", e);
        }
    }

    /**
     * Perform POST request with headers
     * 
     * @param endpoint API endpoint
     * @param body Request body
     * @param headers Request headers
     * @return Response object
     */
    public static Response postRequest(String endpoint, String body, Map<String, String> headers) {
        try {
            logger.info("Performing POST request to: {} with headers", endpoint);
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .headers(headers)
                    .body(body)
                    .when()
                    .post(endpoint)
                    .then()
                    .extract()
                    .response();
            
            logger.info("POST response status: {}", response.statusCode());
            return response;
        } catch (Exception e) {
            logger.error("POST request failed for endpoint: {}", endpoint, e);
            throw new FrameworkException("POST request failed", e);
        }
    }

    /**
     * Perform PUT request
     * 
     * @param endpoint API endpoint
     * @param body Request body
     * @return Response object
     */
    public static Response putRequest(String endpoint, String body) {
        try {
            logger.info("Performing PUT request to: {}", endpoint);
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .when()
                    .put(endpoint)
                    .then()
                    .extract()
                    .response();
            
            logger.info("PUT response status: {}", response.statusCode());
            return response;
        } catch (Exception e) {
            logger.error("PUT request failed for endpoint: {}", endpoint, e);
            throw new FrameworkException("PUT request failed", e);
        }
    }

    /**
     * Perform DELETE request
     * 
     * @param endpoint API endpoint
     * @return Response object
     */
    public static Response deleteRequest(String endpoint) {
        try {
            logger.info("Performing DELETE request to: {}", endpoint);
            Response response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .when()
                    .delete(endpoint)
                    .then()
                    .extract()
                    .response();
            
            logger.info("DELETE response status: {}", response.statusCode());
            return response;
        } catch (Exception e) {
            logger.error("DELETE request failed for endpoint: {}", endpoint, e);
            throw new FrameworkException("DELETE request failed", e);
        }
    }

    /**
     * Get response body as string
     * 
     * @param response Response object
     * @return Response body
     */
    public static String getResponseBody(Response response) {
        String body = response.getBody().asString();
        logger.debug("Response body: {}", body);
        return body;
    }

    /**
     * Get response header value
     * 
     * @param response Response object
     * @param headerName Header name
     * @return Header value
     */
    public static String getResponseHeader(Response response, String headerName) {
        String headerValue = response.getHeader(headerName);
        logger.debug("Response header '{}': {}", headerName, headerValue);
        return headerValue;
    }

    /**
     * Verify response status code
     * 
     * @param response Response object
     * @param expectedStatus Expected status code
     * @return true if status matches
     */
    public static boolean verifyResponseStatus(Response response, int expectedStatus) {
        int actualStatus = response.statusCode();
        boolean matches = actualStatus == expectedStatus;
        logger.info("Status verification - Expected: {}, Actual: {}, Match: {}", 
                expectedStatus, actualStatus, matches);
        return matches;
    }

    /**
     * Verify response contains text
     * 
     * @param response Response object
     * @param text Text to verify
     * @return true if response contains text
     */
    public static boolean verifyResponseContainsText(Response response, String text) {
        String body = response.getBody().asString();
        boolean contains = body.contains(text);
        logger.info("Response text verification - Expected text: '{}', Found: {}", text, contains);
        return contains;
    }
}
