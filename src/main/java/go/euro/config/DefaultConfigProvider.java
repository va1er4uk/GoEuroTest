package go.euro.config;

import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class DefaultConfigProvider implements ConfigProvider {

    private final Logger logger = LoggerFactory.getLogger(DefaultConfigProvider.class);

    @Override
    public Optional<Properties> getConfig(final String location) {
        final Properties properties = new Properties();
        try {
            if (Strings.isNullOrEmpty(location)) {
                logger.info("External application config not set, falling back to internal");
                properties.load(ClassLoader.getSystemResourceAsStream("app.properties"));
            } else {
                logger.info("Loading external application config: {}", location);
                properties.load(new FileReader(location));
            }
        } catch (Exception e) {
            logger.error("Failed to load application config", e);
            return Optional.absent();
        }

        return hasErrors(properties) ? Optional.<Properties> absent() : Optional.of(properties);
    }

    private boolean hasErrors(final Properties properties) {
        final String[] props = { TARGET, PATH, PORT };
        
        for (final String prop : props) {
            if (!properties.containsKey(prop) || Strings.isNullOrEmpty(properties.getProperty(prop))) {
                logger.error("Property {} {}", prop, "Is required");
                return true;
            }
        }

        try {
            Integer.valueOf(properties.getProperty(PORT));
        } catch (Exception e) {
            logger.error("Port MUST be number");
            return true;
        }

        final StringBuilder stringBuilder = new StringBuilder().
                append(properties.getProperty(TARGET)).append(":").
                append(properties.getProperty(PORT)).append("/").
                append(properties.getProperty(PATH));
        try {
            new URL(stringBuilder.toString());
        } catch (MalformedURLException e) {
            logger.error("Resulting URL is malformed");
            return true;
        }

        return false;
    }
}
