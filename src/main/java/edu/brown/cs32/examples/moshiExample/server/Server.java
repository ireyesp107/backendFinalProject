package edu.brown.cs32.examples.moshiExample.server;

import static spark.Spark.after;

import edu.brown.cs32.examples.moshiExample.server.Handlers.*;

import java.io.IOException;
import java.util.HashMap;
import spark.Spark;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various handlers.
 *
 */


public class Server {
    public static void main(String[] args) throws IOException {
        csvData csvContent = new csvData();
        HashMap<String, HashMap> songMapData = new HashMap<>();
        HashMap<String, HashMap> easySongData = new HashMap<>();
        songMapData = GetCategories.createSongMap();
        easySongData = GetCategories.createEasyMap();
        System.out.println(easySongData);
        Spark.port(3131);
        /*
            Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
            be able to make requests to the server.

            By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
            This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
            from any website. Instead, you should set this header to the origin of your client, or a list of origins
            that you trust.

            By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
            Again, it's generally better to be more specific here and only allow the methods you need, but for
            this demo we'll allow all methods.

            We recommend you learn more about CORS with these resources:
                - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
                - https://portswigger.net/web-security/cors
         */
        after((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methodsc", "*");
        });

        // Setting up the handler for the GET /order endpoint
        Spark.get("loadcsv", new loadCSVHandler(csvContent));
        Spark.get("getcsv", new getCSVHandler(csvContent));
        Spark.get("weather", new weatherHandler());
        Spark.get("stats", new statsHandler(csvContent));
        Spark.get("json", new geoJsonHandler());
        Spark.get("songs", new GetCategories(songMapData));
        Spark.get("allsongs", new getSongNames());
        //Spark.get("weather", new weatherHandler)
        Spark.init();
        Spark.awaitInitialization();
        System.out.println("Server started.");
    }
}
