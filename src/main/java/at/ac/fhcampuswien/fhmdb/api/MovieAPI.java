package at.ac.fhcampuswien.fhmdb.api;

import okhttp3.OkHttpClient;    //is loading the main class for http-requests
import okhttp3.Request;         // to generate HTTP-Requests
import okhttp3.Response;        // to process the answer
import java.io.IOException;

// TO-DO
//- Logik zum Senden und Empfangen von Requests/Responses - DONE
//- Erstellung von URLs je Endpunktabfrage
//- MovieAPI soll mit untersch. Parametern und Endpunkten aufgerufen werden
//- API erwartet gesetzten User-Agent-Header im Request (User-Agent http.agent) - DONE

public class MovieAPI {
    // a client object that sends HTTP-Requests
    private final OkHttpClient client = new OkHttpClient();
    private static final String URL = "https://prog2.fh-campuswien.ac.at/movies";

    // TODO - Erstellung von URLs je Endpunktabfrage
    // TODO - MovieAPI soll mit untersch. Parametern und Endpunkten aufgerufen werden

    // Get a URL - This program downloads a URL and prints its contents as a string.
    // https://square.github.io/okhttp/
    public String run(String url) throws IOException {

        // generating a new HTTP-Request
        Request request = new Request.Builder()
                .url(url)
                // Setting User-Agent
                .addHeader("User-Agent", "http.agent")
                .build();

        // sending the request to the server
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected Code " + response);
            return response.body().string();
        }
    }

}
