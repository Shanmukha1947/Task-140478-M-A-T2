package com.example.scraper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimplifiedErrorHandlingMiddleware {

    // Middleware interface for handling errors
    public interface ErrorHandler {
        void handleError(Exception e);
    }

    // Middleware class for error handling
    public static class ErrorHandlingMiddleware {

        private final ErrorHandler errorHandler;

        public ErrorHandlingMiddleware(ErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
        }

        public String fetchDataWithErrorHandling(String urlString) {
            try {
                return fetchData(urlString);
            } catch (Exception e) {
                errorHandler.handleError(e);
                return null;
            }
        }

        private String fetchData(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                throw new IOException("Failed to fetch data. Response code: " + responseCode);
            }

            InputStream inputStream = connection.getInputStream();
            // Perform data parsing or processing here if needed
            // For simplicity, we'll just return the raw response as a string
            return inputStream.toString();
        }
    }

    // Sample usage of the ErrorHandlingMiddleware
    public static void main(String[] args) {
        ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public void handleError(Exception e) {
                System.err.println("Error occurred: " + e.getMessage());
            }
        };

        ErrorHandlingMiddleware middleware = new ErrorHandlingMiddleware(errorHandler);
        String urlToScrape = "https://example.com"; // Replace this with the actual URL you want to scrape

        String data = middleware.fetchDataWithErrorHandling(urlToScrape);
        if (data != null) {
            System.out.println("Scraped data: " + data);
        }
    }
}
