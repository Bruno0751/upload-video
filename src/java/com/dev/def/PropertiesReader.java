package com.dev.def;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Bruno Gressler da Silveira
 * @since 16/11/2025
 * @version 1
 */
public class PropertiesReader {

    private static Properties properties = null;

    public static Properties getProperties() throws IOException {
        String pathFile = "com/dev/def/env.properties";
        properties = new Properties();
        try (InputStream in = PropertiesReader.class.getClassLoader().getResourceAsStream(pathFile)) {
            properties.load(in);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
        return properties;
    }

}
