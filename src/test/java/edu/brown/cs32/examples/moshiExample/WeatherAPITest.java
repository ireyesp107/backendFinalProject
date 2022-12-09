package edu.brown.cs32.examples.moshiExample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.brown.cs32.examples.moshiExample.server.Handlers.weatherHandler;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;
import weatherTypes.ForecastEndpoint;
import weatherTypes.WeatherEndpoint;

public class WeatherAPITest {

    /**
     * testing output for weatherAPI and its different results;
     * begin by making separate port location with call to weatherHandler
     */
    @BeforeAll
    public static void setup_before_everything() {

        // Set the Spark port number. This can only be done once, and has to
        // happen before any route maps are added. Hence using @BeforeClass.
        // Setting port 0 will cause Spark to use an arbitrary available port.
        Spark.port(0);
        // Don't try to remember it. Spark won't actually give Spark.port() back
        // until route mapping has started. Just get the port number later. We're using
        // a random _free_ port to remove the chances that something is already using a
        // specific port on the system used for testing.

        // Remove the logging spam during tests
        //   This is surprisingly difficult. (Notes to self omitted to avoid complicating things.)

        // SLF4J doesn't let us change the logging level directly (which makes sense,
        //   given that different logging frameworks have different level labels etc.)
        // Changing the JDK *ROOT* logger's level (not global) will block messages
        //   (assuming using JDK, not Log4J)
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
    }

    @BeforeEach
    public void setup() {
        Spark.get("/weather", new weatherHandler());
        Spark.init();
        Spark.awaitInitialization();
    }

    @AfterEach
    public void teardown() {
        Spark.unmap("/weather");
        Spark.awaitStop();
    }

    /**
     * testing a few valid lat and lon values - checking result kvp and if temp is a key
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testValidRequests() throws URISyntaxException, IOException, InterruptedException {
        List<String> success = new ArrayList<>();
        success.add("weather?lat=39.7456&lon=-97.0892");
        success.add("weather?lat=41.8267&lon=-71.4029");
        success.add("weather?lat=37&lon=-120");

        for (String output : success
             ) {
            HttpRequest weatherRequest =
                    HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:" + Spark.port() + "/" + output))
                            .GET()
                            .build();
            HttpResponse<String> weatherResponse = HttpClient.newBuilder().build().
                    send(weatherRequest, HttpResponse.BodyHandlers.ofString());
            String tempCheck = weatherResponse.body();
            assertTrue(tempCheck.contains("\"Result\":\"Success\"}"));
            assertTrue(tempCheck.contains("Temperature"));
        }
    }

    /**
     * checking outputs that return errors
     * most common is datasource error - weather api returns error json
     * bad_result is in the event lat and lon have no '=' - as in, not registered as a value and are null
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testDatasourceError() throws URISyntaxException, IOException, InterruptedException {
        List<String> bad_request = new ArrayList<>();
        List<String> datasource = new ArrayList<>();

        datasource.add("weather?lat=0&lon=0");
        bad_request.add("weather?");
        datasource.add("weather?lat=30&lon=10");
        bad_request.add("weather?lat=-91&lon=181");

        bad_request.add("weather?lat=word&lon=nonint");
        bad_request.add("weather?lat=&lon=");
        bad_request.add("weather?lat&lon");

        for (String output : datasource
        ) {
            HttpRequest weatherRequest =
                HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + Spark.port() + "/" + output))
                    .GET()
                    .build();
            HttpResponse<String> weatherResponse = HttpClient.newBuilder().build().
                send(weatherRequest, HttpResponse.BodyHandlers.ofString());
            String result = weatherResponse.body();
            assertEquals(result, "{\"Result\":\"error_datasource\"}");
        }
        for (String output : bad_request) {
            HttpRequest weatherRequest =
                HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:" + Spark.port() + "/" + output))
                    .GET()
                    .build();
            HttpResponse<String> weatherResponse = HttpClient.newBuilder().build().
                send(weatherRequest, HttpResponse.BodyHandlers.ofString());
            String resultds = weatherResponse.body();
            assertEquals(resultds, "{\"Result\":\"error_bad_request\"}");
        }
    }


    /**
     * testing deserializers - unit testing - to make sure we parse jsons correctly
     * @throws IOException
     * @throws URISyntaxException
     * @throws InterruptedException
     */
    @Test
    public void testUnitDeserializer() throws IOException {
            String weather = """
                {"properties":{"forecast": "https://api.weather.gov/gridpoints/BOX/64,64/forecast"}}
                """;
            WeatherEndpoint weatherEndpoint = new weatherHandler().deserializeW(weather);
            assertEquals(
                "https://api.weather.gov/gridpoints/BOX/64,64/forecast",
                weatherEndpoint.properties.forecast);
            String invalidWeather = """
                {"properties":{"forecast": "https://api.weather.gov/problems"}}
                """;
            WeatherEndpoint invalidEndpoint = new weatherHandler().deserializeW(invalidWeather);
            assertTrue(invalidEndpoint == null);
            String forecast = """
                {"properties":{"periods": [{"temperature": 38, "temperatureUnit": "F"}]
                }}
                """;
            ForecastEndpoint forecastEndpoint = new weatherHandler().deserializeF(forecast);
            assertEquals(38, forecastEndpoint.properties.periods.get(0).temperature);
            assertEquals("F", forecastEndpoint.properties.periods.get(0).temperatureUnit);

        }
    }
