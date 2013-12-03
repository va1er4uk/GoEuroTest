package go.euro.config;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

public class ConfigProviderTest {

    private ConfigProvider configProvider;

    @Before
    public void before() {
        configProvider = new DefaultConfigProvider();
    }

    @After
    public void after() {
        configProvider = new DefaultConfigProvider();
    }

    @Test
    public void provideLoadInternalConfigSuccessCase() {
        Optional<Properties> resultConfig = configProvider.getConfig(null);
        assertTrue(resultConfig.isPresent());

        Properties config = resultConfig.get();
        assertThat(config.getProperty(ConfigProvider.TARGET), equalTo("http://pre.dev.goeuro.de"));
        assertThat(config.getProperty(ConfigProvider.PATH), equalTo("api/v1/suggest/position/en/name/"));
        assertThat(config.getProperty(ConfigProvider.PORT), equalTo("12345"));
    }

    @Test
    public void providerLoadExternalConfofSuccessCase() {
        Optional<Properties> resultConfig = configProvider.getConfig("src/test/resources/fake-external.properties");
        assertTrue(resultConfig.isPresent());
        
        Properties config = resultConfig.get();
        assertThat(config.getProperty(ConfigProvider.TARGET), equalTo("http://fake.com"));
        assertThat(config.getProperty(ConfigProvider.PATH), equalTo("fake/api/lang/name"));
        assertThat(config.getProperty(ConfigProvider.PORT), equalTo("4455"));
    }
    
    @Test
    public void providerDoesNotLoadIncorrectProperties() {
        Optional<Properties> resultConfig = configProvider.getConfig("src/test/resources/fake-external-incorrect-empty.properties");
        assertFalse(resultConfig.isPresent());

        resultConfig = configProvider.getConfig("src/test/resources/fake-external-incorrect-target.properties");
        assertFalse(resultConfig.isPresent());

        resultConfig = configProvider.getConfig("src/test/resources/fake-external-incorrect-path.properties");
        assertFalse(resultConfig.isPresent());

        resultConfig = configProvider.getConfig("src/test/resources/fake-external-incorrect-port.properties");
        assertFalse(resultConfig.isPresent());
    }
}
