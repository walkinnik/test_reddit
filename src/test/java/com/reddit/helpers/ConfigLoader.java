package com.reddit.helpers;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ConfigLoader {
    public static Map<String, Object> loadConfig(String configFileName) {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream(configFileName)) {
            return yaml.load(inputStream);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
