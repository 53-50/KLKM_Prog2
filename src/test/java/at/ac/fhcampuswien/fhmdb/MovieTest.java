package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class MovieTest {

    @BeforeAll
    public static void init() {
        System.out.println("Testing Movies");
    }

    @Test
    void check_if_movie_data_is_here() {
        List<Movie> movies = Movie.initializeMovies();
        assertFalse(movies.isEmpty(), "The list shouldn't be empty.");
    }

}
