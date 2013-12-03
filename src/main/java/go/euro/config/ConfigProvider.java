package go.euro.config;

import java.util.Properties;

import com.google.common.base.Optional;

public interface ConfigProvider {

    String LOCATION = "location";
    
    String TARGET = "target";
    String PATH = "path";
    String PORT = "port";

    Optional<Properties> getConfig(String location);
}
