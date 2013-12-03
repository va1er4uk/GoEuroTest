package go.euro;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import go.euro.config.ConfigProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.MockedServer;

public class AppTest {

    @BeforeClass
    public static void setUp() throws Exception {
        MockedServer.startServer();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        MockedServer.stopServer();
    }

    @Test
    public void appGeneratesCorrectReport() throws Exception {
        System.setProperty(ConfigProvider.LOCATION, "src/test/resources/test-config.properties");
        final String[] args = { "someString" };
        App.main(args);

        final File results = new File("./results.csv");
        assertTrue(results.exists());

        final BufferedReader bufferedReader = new BufferedReader(new FileReader(results));
        try {
            assertThat(bufferedReader.readLine(), equalTo("'_type';'_id';'name';'type';'latitude';'longitude'"));
            assertThat(bufferedReader.readLine(), equalTo("'Position';'410978';'Potsdam, USA';'location';'44.66978';'-74.98131'"));
            assertThat(bufferedReader.readLine(), equalTo("'Position';'377078';'Potsdam, Deutschland';'location';'52.39886';'13.06566'"));
        } finally {
            bufferedReader.close();
            results.delete();
        }
    }

    @Test
    public void applicationDoesNotCreateReportWhenConfigIsWrong() {
        System.setProperty(ConfigProvider.LOCATION, "");
        final String[] args = { "someString" };
        App.main(args);
        final File results = new File("./results.csv");
        assertFalse(results.exists());
    }
}