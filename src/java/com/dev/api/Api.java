package com.dev.api;

import com.dev.def.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 16/11/2025
 * @version 1
 */
public final class Api {

    private String apiKey;
    private String apiUser;

    public Api(Properties properties) {
        this.apiKey = properties.getProperty("API_KEY");
        this.apiUser = properties.getProperty("API_USER");
    }

    public String findAll() throws IOException {
        HttpURLConnection connection = null;
        String response = null;
        try {
            URL url = new URL("http://localhost:" + Constants.PORT + "/" + Constants.API_VIDEOS);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(Boolean.FALSE);
            connection.setDoInput(Boolean.TRUE);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            connection.connect();
            int statusCode = connection.getResponseCode();
            
            System.out.println("---select---");
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            System.out.println("------------");

            InputStream stream = (statusCode >= Constants.OK && statusCode < Constants.MULTIPLE_CHOICES) ? connection.getInputStream() : connection.getErrorStream();

            StringBuilder responseBuilder = null;
            if (stream != null) {
                responseBuilder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                }
                
                response = responseBuilder.toString();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IOException(e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }

    public String delete(String id) throws IOException {
        HttpURLConnection connection = null;
        String response = null;
        try {
            URL url = new URL("http://localhost:" + Constants.PORT + "/" + Constants.API_VIDEOS + "/" + id);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoOutput(Boolean.FALSE);
            connection.setDoInput(Boolean.TRUE);
            connection.setRequestProperty("Content-Type", "application/json");

            final String AUTH = this.apiUser + ":" + this.apiKey;
            final String ENCODE_AUTH = Base64.getEncoder().encodeToString(AUTH.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + ENCODE_AUTH);

            connection.connect();
            int statusCode = connection.getResponseCode();
            
            System.out.println("---delete---");
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getResponseMessage());
            System.out.println("------------");
            
            InputStream stream = (statusCode >= Constants.OK && statusCode < Constants.MULTIPLE_CHOICES) ? connection.getInputStream() : connection.getErrorStream();
            if (stream != null) {
                StringBuilder responseBuilder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IOException(e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }
}
