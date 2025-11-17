package configFactory;

import java.io.InputStream;
import java.util.Properties;

public class ConfigFactory {
    private final Properties properties;

    public ConfigFactory(String env) {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config-" + env + ".properties")) {
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
