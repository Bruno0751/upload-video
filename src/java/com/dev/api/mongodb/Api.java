package com.dev.api.mongodb;

import com.dev.def.Constants;
import com.dev.documentBson.VideoBson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
public class Api {

    private String apiKey;
    private String apiUser;

    public Api(Properties properties) {
        this.apiKey = properties.getProperty("API_KEY");
        this.apiUser = properties.getProperty("API_USER");
    }

    public String requestGET() throws IOException {
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
            
            InputStream stream = (connection.getResponseCode() >= Constants.OK && connection.getResponseCode() < Constants.MULTIPLE_CHOICES) ? connection.getInputStream() : connection.getErrorStream();
            
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

    public String requestPOST(VideoBson videoBson) throws IOException {
        HttpURLConnection connection = null;
        String response = null;
        try {
            URL url = new URL("http://localhost:" + Constants.PORT + "/" + Constants.API_VIDEOS);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(Boolean.TRUE);
            connection.setDoInput(Boolean.TRUE);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            final String AUTH = this.apiUser + ":" + this.apiKey;
            final String ENCODE_AUTH = Base64.getEncoder().encodeToString(AUTH.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + ENCODE_AUTH);
            String jsonBody = "{"
                    + "\"idVideo\":" + videoBson.getIdVideo() + ","
                    + "\"name\":\"" + videoBson.getName() + "\","
                    + "\"length\":" + videoBson.getLength() + ","
                    + "\"email\":\"" + videoBson.getEmail() + "\""
                    + "}";
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            connection.connect();

            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getResponseCode() >= Constants.OK && connection.getResponseCode() < Constants.MULTIPLE_CHOICES ? connection.getInputStream() : connection.getErrorStream())
            )) {
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
            }
            if (connection.getResponseCode() == Constants.OK || connection.getResponseCode() == Constants.CREATED) {
                response = response.toString();
            } else {
                System.out.println("ERRO: " + responseBuilder);
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

    public static String requestPUT(int id) throws IOException {
        return null;
    }

    public String requestDELETE(Long idVideo) throws IOException {
        HttpURLConnection connection = null;
        String response = null;
        try {
            URL url = new URL("http://localhost:" + Constants.PORT + "/" + Constants.API_VIDEOS + "/" + idVideo);
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

    public void saveFile(VideoBson videoBson) throws IOException {
        HttpURLConnection connection = null;
        String boundary = "----BOUNDARY-" + System.currentTimeMillis();
        try {
            URL url = new URL("http://localhost:" + Constants.PORT + "/" + Constants.API_VIDEOS + "/saveFile");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(Boolean.TRUE);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            final String AUTH = this.apiUser + ":" + this.apiKey;
            final String ENCODE_AUTH = Base64.getEncoder().encodeToString(AUTH.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + ENCODE_AUTH);

            String metadataJson = """
            {
                "idVideo": %d,
                "name": "%s",
                "length": %d,
                "email": "%s"
            }
            """.formatted(
                    videoBson.getIdVideo(),
                    videoBson.getName(),
                    videoBson.getLength(),
                    videoBson.getEmail()
            );
            
            OutputStream output = connection.getOutputStream();
            output.write(("--" + boundary + "\r\n").getBytes());
            output.write("Content-Disposition: form-data; name=\"metadata\"\r\n".getBytes());
            output.write("Content-Type: application/json\r\n\r\n".getBytes());
            output.write(metadataJson.getBytes(StandardCharsets.UTF_8));
            output.write("\r\n".getBytes());
            output.write(("--" + boundary + "\r\n").getBytes());
            output.write("Content-Disposition: form-data; name=\"file\"; filename=\"upload.bin\"\r\n".getBytes());
            output.write("Content-Type: application/octet-stream\r\n\r\n".getBytes());
            output.write(videoBson.getContent());
            output.write("\r\n".getBytes());
            output.write(("--" + boundary + "--\r\n").getBytes());
            output.flush();
            output.close();
            
            int status = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(status >= Constants.OK && status < Constants.MULTIPLE_CHOICES ? connection.getInputStream() : connection.getErrorStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();
            System.out.println("STATUS: " + status);
            System.out.println("RESPONSE: " + responseBuilder);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IOException(e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
