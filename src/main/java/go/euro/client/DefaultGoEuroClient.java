package go.euro.client;

import go.euro.client.response.Results;
import go.euro.config.ConfigProvider;

import java.util.Properties;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class DefaultGoEuroClient implements GoEuroClient {

    private final Logger logger = LoggerFactory.getLogger(DefaultGoEuroClient.class);

    private final Properties properties;

    public DefaultGoEuroClient(final Properties properties) {
        this.properties = properties;
    }

    @Override
    public Optional<Results> get(final String argument) {
        if (Strings.isNullOrEmpty(argument)) {
            logger.error("Argument can not be empty");
            return Optional.absent();
        } else if (!argument.matches("^[\\d\\sa-zA-Z]+$")) {
            logger.error("Argument should not contain non word characters");
            return Optional.absent();
        }
        if (properties == null) {
            logger.error("Application MUST be provided");
            return Optional.absent();
        }

        try {
            final String resultsJson = ClientBuilder.newClient()
                    .target(properties.getProperty(ConfigProvider.TARGET) + ":" + properties.getProperty(ConfigProvider.PORT))
                    .path(properties.getProperty(ConfigProvider.PATH) + "/" + argument).request(MediaType.APPLICATION_JSON).get(String.class);

            final ObjectMapper objectMapper = new ObjectMapper();
            final Results results = objectMapper.readValue(resultsJson, Results.class);

            return Optional.of(results);
        } catch (Exception e) {
            logger.error("Failed to load remote resouces", e);
        }

        return Optional.absent();
    }
}
