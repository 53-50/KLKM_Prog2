package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeControllerTest {

    @BeforeAll
    public static void init() {
        System.out.println("~~~~~~~~ Testing HomeController ~~~~~~~~");
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

        HomeController testHomeController = new HomeController();

        testHomeController.observableMovies = observableMoviesList;

        testHomeController.sortMoviesAscending();

        for (int i = 0; i < ExpectedMoviesList.size(); i++) {
            assertEquals(ExpectedMoviesList.get(i).getTitle(),
                    observableMoviesList.get(i).getTitle());
        }
    }


    @Test
    public void test(){
        int x = 10;
        int y = 15;

        assertEquals(x, y);
    }

}
