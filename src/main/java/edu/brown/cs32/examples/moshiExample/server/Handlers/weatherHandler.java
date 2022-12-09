package edu.brown.cs32.examples.moshiExample.server.Handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import weatherTypes.ForecastEndpoint;
import weatherTypes.WeatherEndpoint;

/**
 * class that handles the weather requests
 */

public class weatherHandler extends genericHandler implements Route {

  private String lat;
  private String lon;

  public weatherHandler() {
    this.lat = lat;
    this.lon = lon;
  }

  /**
   * handles weather requests
   *
   * @param request, request of the weather at specific lat and lon
   * @param response
   * @return map that indicates weather at specific lat and lon
   * @throws IOException
   * @throws URISyntaxException
   */
  public Object handle(Request request, Response response) throws IOException, URISyntaxException {
    hashMap.clear();
    HttpResponse<String> forecastResponse;
    HttpResponse<String> weatherResponse;

    QueryParamsMap qpmLat = request.queryMap();
    this.lat = qpmLat.value("lat");
    System.out.println(this.lat);

    QueryParamsMap qpmLon = request.queryMap();
    this.lon = qpmLon.value("lon");


    try {
      Float latnum = Float.parseFloat(this.lat);
      Float lonnum = Float.parseFloat(this.lon);
      if ((latnum < -90 || latnum > 90) || (lonnum < -180 || lonnum > 180)
      || request.queryParams().size() > 2) {
        return BadRequestSerialize(hashMap);
      }
    } catch (NullPointerException | NumberFormatException e) {
      return BadRequestSerialize(hashMap);
    }


      try {
        // request
        URI weatherURI = new URI("https://api.weather.gov/points/" + this.lat + "," + this.lon);
        HttpRequest weatherRequest = HttpRequest.newBuilder().uri(weatherURI).GET().build();

        //sending request and getting response
        weatherResponse = HttpClient.newBuilder().build().
            send(weatherRequest, BodyHandlers.ofString());
        //some fromJson call to get the first properties
        WeatherEndpoint forecastCheck = deserializeW(weatherResponse.body());

        if (forecastCheck == null) {
          return DatasourceSerialize(hashMap);
        } else {
          String forecastLink = forecastCheck.properties.forecast;
          //forecast client
          HttpRequest forecastRequest = HttpRequest.newBuilder().uri(new URI(forecastLink)).GET()
              .build();

          forecastResponse = HttpClient.newBuilder().build().
              send(forecastRequest, BodyHandlers.ofString());

          ForecastEndpoint forecastProp = deserializeF(forecastResponse.body());

          // going through each level of the weather API
          Float temp = forecastProp.properties.periods.get(0).temperature;
          String tempUnit = forecastProp.properties.periods.get(0).temperatureUnit;
          this.hashMap.put("Result", "Success");
          this.hashMap.put("lat", this.lat);
          this.hashMap.put("lon", this.lon);
          this.hashMap.put("Temperature", temp + tempUnit);

        }
      } catch (URISyntaxException | InterruptedException e) { //are these the same thing?
        BadJsonSerialize(hashMap);

      }


    HashMap<String, Object> hashMapSuccessCopy = new HashMap(hashMap);
    return serialize(hashMapSuccessCopy);

  }

    /**
     * deserializes json forecast to forecast endpoint
     * @param forecastResponse, json of forecast
     * @return, list of periods
     * @throws IOException
     */

    public ForecastEndpoint deserializeF(String forecastResponse) throws IOException {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<ForecastEndpoint> jsonAdapter = moshi.adapter(ForecastEndpoint.class);
      return jsonAdapter.fromJson(forecastResponse);
    }

    /**
     * deserializes weather
     * @param weatherResponse, json of the weather
     * @return a weather endpoint of the weather
     * @throws IOException
     */
    public WeatherEndpoint deserializeW(String weatherResponse) throws IOException {
      if (weatherResponse.contains(
          "api.weather.gov/problems")) { // slow but im lazy / cant think of better way rn
        return null;
      } else {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<WeatherEndpoint> jsonAdapter = moshi.adapter(WeatherEndpoint.class);
        return jsonAdapter.fromJson(weatherResponse);
      }
    }

}