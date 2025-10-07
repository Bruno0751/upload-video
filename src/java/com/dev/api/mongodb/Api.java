/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dev.api.mongodb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Bruno
 */
public class Api {

    private final static String PROTOCOL = "http";
    private final static String HOST = "localhost";
    private final static String PORT = "1010";
    private final static String ENDPOINT = "v1/video";

    public static String requestGET() throws IOException {
        try {
            URL url = new URL(Api.PROTOCOL + "://" + Api.HOST + ":" + Api.PORT + "/" + Api.ENDPOINT);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(Boolean.FALSE); // Permite enviar body
            connection.setDoInput(Boolean.TRUE);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            StringBuilder response;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                response = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            connection.disconnect();

            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
                return response.toString();
            }
            return null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IOException(e.getMessage());
        }
    }

    public static String requestPOST() throws IOException {
        return null;
    }

    public static String requestPUT(int id) throws IOException {
        return null;
    }

    public static String requestDELETE(int id) throws IOException {
//        try {
//            URL url = new URL(Api.PROTOCOL + "://" + Api.HOST + ":" + Api.PORT + "/" + Api.ENDPOINT);
//
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("DELETE");
//            connection.setDoOutput(Boolean.FALSE); // Permite enviar body
//            connection.setDoInput(Boolean.TRUE);
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.connect();
//            StringBuilder response;
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                response = new StringBuilder();
//                String inputLine;
//                while ((inputLine = reader.readLine()) != null) {
//                    response.append(inputLine);
//                }
//            }
//            connection.disconnect();
//            
//            if (connection.getResponseCode() == 200) {
//                return response.toString();
//            }
//            return null;
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            throw new IOException(e.getMessage());
//        }
        return null;
    }
}
