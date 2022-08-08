package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public static String getProperty (String fileName, String propertyName) throws IOException {
        try(InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream(fileName)){
            if (input == null){
                throw new IOException("The file \"" + fileName + "\" is empty or epson");
            }
            Properties propertiesFile = new Properties();
            propertiesFile.load(input);
            propertyName = propertyName.toLowerCase().replace(" ", "_");
            return propertiesFile.getProperty(propertyName);
        }
    }
}