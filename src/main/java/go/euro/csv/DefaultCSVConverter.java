package go.euro.csv;

import go.euro.client.response.Result;
import go.euro.client.response.Results;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

public class DefaultCSVConverter implements CSVConverter {

    private final Logger logger = LoggerFactory.getLogger(DefaultCSVConverter.class);

    @Override
    public boolean convert(final Results results) {
        if (results == null) {
            logger.error("Results can not be null");
            return false;
        }

        final String fileName = "results.csv";
        try {
            final CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"), ';', '\'');
            try {
                writer.writeNext(new String[] { "_type", "_id", "name", "type", "latitude", "longitude" });

                for (final Result result : results.getResults()) {
                    writer.writeNext(new String[] { result.get_type(), String.valueOf(result.get_id()), result.getName(), result.getType(),
                            String.valueOf(result.getGeoPosition().getLatitude()), String.valueOf(result.getGeoPosition().getLongitude()) });
                }

                return true;
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            logger.error("Failed to convert results to csv", e);
        }
        return false;
    }
}
