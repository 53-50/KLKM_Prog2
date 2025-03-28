package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

class MovieAPITest {

    @BeforeAll
    public static void init() {
        System.out.println("~~~~~~~~ Testing MovieAPI ~~~~~~~~");
    }

    @Test
    void check_if_ApiCall_Returns_Response() throws IOException {
        MovieAPI api = new MovieAPI();
        List<Movie> response = api.fetchAllMovies();

        assertNotNull(response, "The API-Response is null.");
        assertFalse(response.size() > 0, "The API-Response is empty.");
    }

    @Test
    void check_if_URL_gets_build_with_query() {
        String expected = "https://prog2.fh-campuswien.ac.at/movies?query=movie&";
        String result = MovieAPI.buildURL("movie", null, null, null);
        assertEquals(expected, result);
    }

    @Test
    void check_if_URL_gets_build_with_genre() {
        String expected = "https://prog2.fh-campuswien.ac.at/movies?genre=ACTION&";
        String result = MovieAPI.buildURL(null, Movie.Genre.ACTION, null, null);
        assertEquals(expected, result);
    }

    @Test
    public void check_if_URL_gets_build_with_more_parameters() {
        String expected = "https://prog2.fh-campuswien.ac.at/movies?query=movie&genre=ACTION&releaseYear=2022&ratingFrom=5&";
        String result = MovieAPI.buildURL("movie", Movie.Genre.ACTION, "2022", "5");
        assertEquals(expected, result);
    }

}
