package manager;

// ConfigManager.java
import android.content.Context;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.properties";
    private Properties properties;

    public ConfigManager(Context context) {
        properties = new Properties();
        try {
            InputStream inputStream = context.getAssets().open(CONFIG_FILE);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getApiKey() {
        return properties.getProperty("DEEPSEEK_API_KEY", "");
    }

    public String getBaseUrl() {
        return properties.getProperty("API_BASE_URL", "https://api.deepseek.com/v1/");
    }

    // Use to print the config
    public void printConfig() {
        System.out.println("API: " + getApiKey());
        System.out.println("Base: " + getBaseUrl());
    }
}
