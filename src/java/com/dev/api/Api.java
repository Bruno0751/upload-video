package com.dev.api;

import com.dev.def.Constants;
import com.dev.documents.VideoBson;
import java.io.BufferedOutputStream;
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
public final class Api {

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

    public static String requestPUT(int id) throws IOException {
        return null;
    }

    public String requestDELETE(String id) throws IOException {
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

    public void save(VideoBson videoBson, InputStream inputStream) throws IOException {
        HttpURLConnection connection = null;
        String boundary = "----BOUNDARY-" + System.currentTimeMillis();

        try {
            URL url = new URL("http://localhost:" + Constants.PORT + "/" + Constants.API_VIDEOS);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            connection.setChunkedStreamingMode(8192);

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
            BufferedOutputStream bos = new BufferedOutputStream(output);
            byte[] CRLF = "\r\n".getBytes(StandardCharsets.UTF_8);

            // =========================
            // 🔹 METADATA
            // =========================
            bos.write(("--" + boundary).getBytes(StandardCharsets.UTF_8));
            bos.write(CRLF);
            bos.write("Content-Disposition: form-data; name=\"metadata\"".getBytes(StandardCharsets.UTF_8));
            bos.write(CRLF);
            bos.write("Content-Type: application/json".getBytes(StandardCharsets.UTF_8));
            bos.write(CRLF);
            bos.write(CRLF);
            bos.write(metadataJson.getBytes(StandardCharsets.UTF_8));
            bos.write(CRLF);
        
//            bos.write(("--" + boundary + "\r\n").getBytes());
//            bos.write("Content-Disposition: form-data; name=\"metadata\"\r\n".getBytes());
//            bos.write("Content-Type: application/json\r\n\r\n".getBytes());
//            bos.write(metadataJson.getBytes(StandardCharsets.UTF_8));
//            bos.write("\r\n".getBytes());

            // =========================
            // 🔹 FILE (STREAM)
            // =========================
            bos.write(("--" + boundary).getBytes(StandardCharsets.UTF_8));
            bos.write(CRLF);
            bos.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" 
                    + videoBson.getName() + "\"").getBytes(StandardCharsets.UTF_8));
            bos.write(CRLF);
            bos.write("Content-Type: video/mp4".getBytes(StandardCharsets.UTF_8));
            bos.write(CRLF);
            bos.write("Content-Transfer-Encoding: binary".getBytes(StandardCharsets.UTF_8));
            bos.write(CRLF);
            bos.write(CRLF);
//            bos.write(("--" + boundary + "\r\n").getBytes());
//            bos.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + videoBson.getName() + "\"\r\n").getBytes());
//            bos.write("Content-Type: video/mp4\r\n\r\n".getBytes());
//            bos.write("Content-Type: application/octet-stream\r\n\r\n".getBytes());

            // 🔥 STREAM REAL (CORRETO)
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            bos.write(CRLF);

            // =========================
            // 🔹 FINAL
            // =========================
            bos.write(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
            bos.write(CRLF);

            bos.flush();
            bos.close();

            int status = connection.getResponseCode();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        status >= Constants.OK && status < Constants.MULTIPLE_CHOICES
                                ? connection.getInputStream()
                                : connection.getErrorStream()
                )
            );

            StringBuilder responseBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            reader.close();

            System.out.println("STATUS: " + status);
            System.out.println("RESPONSE: " + responseBuilder);
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
