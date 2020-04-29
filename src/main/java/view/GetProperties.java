package view;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetProperties {
    private static final String fullName = "config.properties";
    private final Properties properties = new Properties();

    private static GetProperties getProperties;

    public static GetProperties getInstanse(){
        if(getProperties == null)
            getProperties = new GetProperties();
        return getProperties;
    }

    private GetProperties(){
        try(InputStream is = GetProperties.class.getClassLoader().getResourceAsStream(fullName)) {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String name){
        return properties.getProperty(name);
    }
}
