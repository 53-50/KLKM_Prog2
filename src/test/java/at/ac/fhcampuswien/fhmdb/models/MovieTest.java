package at.ac.fhcampuswien.fhmdb.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieTest {

    @BeforeAll
    public static void init() {
        System.out.println("Testing Movies");
    }

    @Test
    public void check_if_sorting_all_movies_ascending() {
            List<Movie> ExpectedMoviesList = Arrays.asList(
                    new Movie("Ant-Man"),
                    new Movie("Iron Man"),
                    new Movie("The Godfather")
            );

            ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                    new Movie("The Godfather"),
                    new Movie("Iron Man"),
                    new Movie("Ant-Man")
            );

            FXCollections.sort(observableMoviesList, Comparator.comparing(Movie::getTitle));

            for (int i = 0; i < ExpectedMoviesList.size(); i++) {
                assertEquals(ExpectedMoviesList.get(i).getTitle(),
                        observableMoviesList.get(i).getTitle());
            }
    }
}
