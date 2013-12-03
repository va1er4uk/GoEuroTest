package go.euro.client;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import go.euro.client.response.Result;
import go.euro.client.response.Results;
import go.euro.config.ConfigProvider;
import go.euro.config.DefaultConfigProvider;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.MockedServer;

import com.google.common.base.Optional;

public class GoEuroClientTest {

    private GoEuroClient goEuroClient;

    @BeforeClass
    public static void setUp() throws Exception {
        MockedServer.startServer();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        MockedServer.stopServer();
    }

    @Before
    public void before() {
        ConfigProvider configProvider = new DefaultConfigProvider();
        goEuroClient = new DefaultGoEuroClient(configProvider.getConfig("src/test/resources/test-config.properties").get());
    }

    @Test
    public void clientReturnsNothingWhenQueryStringIsEmptyOrNull() {
        assertFalse("Result for empty string must be absent", new DefaultGoEuroClient(new Properties()).get("").isPresent());
        assertFalse("Result for null string must be absent", new DefaultGoEuroClient(new Properties()).get(null).isPresent());
    }

    @Test
    public void clientReturnsCorrectResultFromRemoteService() {
        Optional<Results> queryResult = goEuroClient.get("someString");
        assertTrue(queryResult.isPresent());
        Results results = queryResult.get();

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

    @Test
    public void clientReturnsNothingWhenSearchStringIsNullOrEmpty() {
        assertFalse(goEuroClient.get("").isPresent());
        assertFalse(goEuroClient.get(null).isPresent());
        assertFalse(goEuroClient.get("##dff").isPresent());
        assertFalse(goEuroClient.get("dff&@!").isPresent());
    }

    @Test
    public void clientReturnNothingWhenConfigNotLoadedCorrectly() {
        goEuroClient = new DefaultGoEuroClient(null);
        assertFalse(goEuroClient.get("someString").isPresent());

        goEuroClient = new DefaultGoEuroClient(null);
        assertFalse(goEuroClient.get(null).isPresent());
    }
}
