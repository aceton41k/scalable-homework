package scalable.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    static Properties properties = new Properties();

    static {
        try {
            InputStream inputStream = new FileInputStream("src/test/resources/test.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Properties getProperties() {
        return properties;
    }
}

