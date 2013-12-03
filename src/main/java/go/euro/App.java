package go.euro;

import java.util.Properties;

import go.euro.client.DefaultGoEuroClient;
import go.euro.client.GoEuroClient;
import go.euro.client.response.Results;
import go.euro.config.ConfigProvider;
import go.euro.config.DefaultConfigProvider;
import go.euro.csv.CSVConverter;
import go.euro.csv.DefaultCSVConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(final String[] args) {
        if (args.length > 0) {
            logger.info("Application started with argument: {}", args[0]);

            final ConfigProvider configProvider = new DefaultConfigProvider();
            final Optional<Properties> configResult = configProvider.getConfig(System.getProperty(ConfigProvider.LOCATION, null));

            if (configResult.isPresent()) {
                final GoEuroClient goEuroClient = new DefaultGoEuroClient(configResult.get());
                final Optional<Results> results = goEuroClient.get(args[0]);

                if (results.isPresent()) {
                    final CSVConverter converter = new DefaultCSVConverter();
                    converter.convert(results.get());
                } else {
                    logger.error("Failed to fetch results from remote service");
                }
            } else {
                logger.error("Config not found, application stopped");
            }
        } else {
            logger.error("Argument must be specified. Example: java -jar GoEuroTest.jar \"STRING\"");
        }
    }
}
