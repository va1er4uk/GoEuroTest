package go.euro.client.response;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResultsTest {

    @Test
    public void resultsAreMappedCorrectly() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Results results = objectMapper.readValue(ClassLoader.getSystemResourceAsStream("content.json"), Results.class);
        assertNotNull(results);
        assertNotNull(results.getResults());
        assertThat(results.getResults().length, equalTo(2));

        Result result1 = results.getResults()[0];
        assertNotNull(result1);
        assertThat(result1.get_type(), equalTo("Position"));
        assertThat(result1.get_id(), equalTo(410978L));
        assertThat(result1.getName(), equalTo("Potsdam, USA"));
        assertThat(result1.getType(), equalTo("location"));
        assertThat(result1.getGeoPosition().getLatitude(), equalTo(44.66978));
        assertThat(result1.getGeoPosition().getLongitude(), equalTo(-74.98131));
        
        Result result2 = results.getResults()[1];
        assertNotNull(result2);
        assertThat(result2.get_type(), equalTo("Position"));
        assertThat(result2.get_id(), equalTo(377078L));
        assertThat(result2.getName(), equalTo("Potsdam, Deutschland"));
        assertThat(result2.getType(), equalTo("location"));
        assertThat(result2.getGeoPosition().getLatitude(), equalTo(52.39886));
        assertThat(result2.getGeoPosition().getLongitude(), equalTo(13.06566));
    }
}
