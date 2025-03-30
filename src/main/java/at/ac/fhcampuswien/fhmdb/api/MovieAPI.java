package at.ac.fhcampuswien.fhmdb.api;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;    //is loading the main class for http-requests
import okhttp3.Request;         // to generate HTTP-Requests
import okhttp3.Response;        // to process the answer
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

// TO-DO
//- Logik zum Senden und Empfangen von Requests/Responses - DONE
//- Erstellung von URLs je Endpunktabfrage
//- MovieAPI soll mit untersch. Parametern und Endpunkten aufgerufen werden
//- API erwartet gesetzten User-Agent-Header im Request (User-Agent http.agent) - DONE

public class MovieAPI {
    // a client object that sends HTTP-Requests
    private final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "https://prog2.fh-campuswien.ac.at/movies";

    // Which Endpoints are availabe?
    // query, genre, releaseYear, ratingFrom
    // ? is first symbol when parameters are following
    // = ist after every parameter
    // & is between the parameters

    public static String buildURL(String query, Movie.Genre genre, String releaseYear, String ratingFrom) {
        StringBuilder url = new StringBuilder(BASE_URL);

        if ( (query != null && !query.isEmpty()) ||
                genre != null || releaseYear != null || ratingFrom != null) {

            url.append("?");

            if (query != null && !query.isEmpty()) {
                url.append("query=").append(query).append("&");
            }

            if (genre != null) {
                url.append("genre=").append(genre).append("&");
            }

            if (releaseYear != null) {
                url.append("releaseYear=").append(releaseYear).append("&");
            }

            if (ratingFrom != null) {
                url.append("ratingFrom=").append(ratingFrom).append("&");
            }

        }

        return url.toString();
    }

    // when API gets called by ID
    private static String buildURL(UUID id) {
        StringBuilder url = new StringBuilder(BASE_URL);
        if (id != null) {
            url.append("/").append("id");
        }
        return url.toString();
    }

    // Get a URL - This program downloads a URL and prints its contents as List<Movie>
    // https://square.github.io/okhttp/
    public List<Movie> fetchAllMovies(String query, Movie.Genre genre, String releaseYear, String ratingFrom) throws IOException {
        // build the link together with different parameters
        String URL = buildURL(query,genre, releaseYear, ratingFrom);

        // generating a new HTTP-Request
        Request request = new Request.Builder()
                .url(URL)
                // Setting User-Agent
                .addHeader("User-Agent", "http.agent")
                .build();

        // sending the request to the server
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String responseBody = response.body().string();
            // JSON-String gets converted into a Movie-Object
            Gson gson = new Gson();
            // It gets converted into an Array of Movie-Objects
            Movie[] movies = gson.fromJson(responseBody, Movie[].class);
            return Arrays.asList(movies);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        // if something goes wrong return an empty list
        return new ArrayList<>();
    }

    public List<Movie> fetchAllMovies() throws IOException {
        return fetchAllMovies(null, null, null, null);
    }

    public Movie fetchMovieByID(UUID id) {
        // build the link together with the ID
        String URL = buildURL(id);

        // generating a new HTTP-Request
        Request request = new Request.Builder()
                .url(URL)
                // Setting User-Agent
                .addHeader("User-Agent", "http.agent")
                .build();

        // sending the request to the server
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String responseBody = response.body().string();
            // JSON-String gets converted into a Movie-Object
            Gson gson = new Gson();
            return gson.fromJson(responseBody, Movie.class);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        // if something goes wrong return null
        return null;

    }

}
