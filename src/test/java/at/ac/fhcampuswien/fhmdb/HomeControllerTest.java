package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    @BeforeAll
    public static void init() {
        System.out.println("~~~~~~~~ Testing HomeController ~~~~~~~~");
    }

    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {
        }); // Initialisiert JavaFX für Tests
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
    public void check_if_sorting_all_movies_descending() {
        List<Movie> ExpectedMoviesList = Arrays.asList(
                new Movie("The Godfather"),
                new Movie("Iron Man"),
                new Movie("Ant-Man")
        );

        ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                new Movie("The Godfather"),
                new Movie("Ant-Man"),
                new Movie("Iron Man")
        );

        HomeController testHomeController = new HomeController();

        testHomeController.observableMovies = observableMoviesList;

        testHomeController.sortMoviesDescending();

        for (int i = 0; i < ExpectedMoviesList.size(); i++) {
            assertEquals(ExpectedMoviesList.get(i).getTitle(),
                    observableMoviesList.get(i).getTitle());
        }
    }

    @Test
    void check_if_movie_filtered_desc_afterwards_apply_genre() {
        List<Movie> ExpectedMoviesList = Arrays.asList(
                new Movie("C-Movie", "Whats happening", List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("A-Movie", "Whats happening", List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY))
        );

        ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                new Movie("A-Movie", "Whats happening", List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("B-Movie", "Whats happening", List.of(Movie.Genre.HISTORY)),
                new Movie("C-Movie", "Whats happening", List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY))
        );

        HomeController testHomeController = new HomeController();

        testHomeController.allMovies = new ArrayList<>(observableMoviesList);

        testHomeController.observableMovies = observableMoviesList;

        testHomeController.sortMoviesDescending();

        testHomeController.genreComboBox = new JFXComboBox<>();
        testHomeController.genreComboBox.setValue(Movie.Genre.DRAMA);

        testHomeController.applyFilter();

        for (int i = 0; i < ExpectedMoviesList.size(); i++) {
            assertEquals(ExpectedMoviesList.get(i).getTitle(),
                    observableMoviesList.get(i).getTitle());
        }

    }

    @Test
    void check_if_movie_filtered_asc_afterwards_apply_genre() {
        List<Movie> ExpectedMoviesList = Arrays.asList(
                new Movie("A-Movie", "Whats happening", List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("C-Movie", "Whats happening", List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY))
        );

        ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                new Movie("C-Movie", "Whats happening", List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("A-Movie", "Whats happening", List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("B-Movie", "Whats happening", List.of(Movie.Genre.HISTORY))
        );

        HomeController testHomeController = new HomeController();

        testHomeController.allMovies = new ArrayList<>(observableMoviesList);

        testHomeController.observableMovies = observableMoviesList;

        testHomeController.sortMoviesAscending();

        testHomeController.genreComboBox = new JFXComboBox<>();
        testHomeController.genreComboBox.setValue(Movie.Genre.COMEDY);

        testHomeController.applyFilter();

        for (int i = 0; i < ExpectedMoviesList.size(); i++) {
            assertEquals(ExpectedMoviesList.get(i).getTitle(),
                    observableMoviesList.get(i).getTitle());
        }

    }

    @Test
    void test_checks_if_ApplyFilter_MethodExists() {
        try {
            HomeController testHomeController = new HomeController();
            Method method = testHomeController.getClass().getDeclaredMethod("applyFilter");
            assertNotNull(method, "The method 'applyFilter' should exist");
        } catch (NoSuchMethodException e) {
            fail("The Method 'applyFilter' does not exist");
        }
    }

    //--------------------------------------- Unit tests Query Filter ---------------------------------------//

    @Test
    public void test_specific_Search_for_Movie_Thor() {
        HomeController testHomeController = new HomeController();

        Movie thor = new Movie("Thor", "A powerfull but arrogant god is cast down to Earth", List.of(Movie.Genre.ACTION));
        Movie ironMan = new Movie("Iron Man", "A billionaire builds a high-tech suit", List.of(Movie.Genre.ACTION));
        Movie theAvengers = new Movie("The Avengers", "Superheros team up to save the world", List.of(Movie.Genre.ACTION));

        testHomeController.searchField = new TextField("Thor");
        testHomeController.genreComboBox = new JFXComboBox<>();

        testHomeController.applyFilter();

        assertEquals(1, testHomeController.observableMovies.size(), "Only one movie should be left");
        assertEquals("Thor", testHomeController.observableMovies.get(0).getTitle(), "The filtered movie should be Thor.");
    }

    @Test
    public void test_specific_Search_for_Movie_TheAvengers() {
        HomeController testHomeController = new HomeController();

        Movie thor = new Movie("Thor", "A powerfull but arrogant god is cast down to Earth", List.of(Movie.Genre.ACTION));
        Movie ironMan = new Movie("Iron Man", "A billionaire builds a high-tech suit", List.of(Movie.Genre.ACTION));
        Movie theAvengers = new Movie("The Avengers", "Superheros team up to save the world", List.of(Movie.Genre.ACTION));

        testHomeController.searchField = new TextField("The Avengers");
        testHomeController.genreComboBox = new JFXComboBox<>();

        testHomeController.applyFilter();

        assertEquals(1, testHomeController.observableMovies.size(), "Only one movie should be left");
        assertEquals("The Avengers", testHomeController.observableMovies.get(0).getTitle(), "The filtered movie should be The Avengers.");
    }

    @Test
    public void test_if_Query_and_Genre_Null_returns_unfiltered_List() {
        HomeController testHomeController = new HomeController();

        testHomeController.allMovies = Movie.initializeMovies();

        testHomeController.searchField = new TextField(null);

        testHomeController.genreComboBox = new JFXComboBox<>();

        testHomeController.applyFilter();

        assertEquals(testHomeController.allMovies, testHomeController.observableMovies, "If QueryFilter is null, return should be unfiltered list.");
    }

    @Test
    public void test_if_Query_and_Genre_empty_returns_unfiltered_List() {
        HomeController testHomeController = new HomeController();

        testHomeController.allMovies = Movie.initializeMovies();

        testHomeController.searchField = new TextField("");

        testHomeController.genreComboBox = new JFXComboBox<>();
        testHomeController.genreComboBox.setValue(null);

        testHomeController.applyFilter();

        assertEquals(testHomeController.allMovies, testHomeController.observableMovies, "If QueryFilter is empty, return should be unfiltered list.");

    }

    @Test
    public void test_if_ApplyFilter_Ignores_CaseSensitivity() {
        HomeController testHomeController = new HomeController();

        Movie thor = new Movie("Thor", "A powerfull but arrogant god is cast down to Earth", List.of(Movie.Genre.ACTION));
        Movie ironMan = new Movie("Iron Man", "A billionaire builds a high-tech suit", List.of(Movie.Genre.ACTION));
        Movie theAvengers = new Movie("The Avengers", "Superheros team up to save the world", List.of(Movie.Genre.ACTION));

        testHomeController.allMovies = List.of(thor, ironMan, theAvengers);

        testHomeController.genreComboBox = new JFXComboBox<>();
        testHomeController.genreComboBox.getItems().addAll(Movie.Genre.ACTION);
        testHomeController.searchField = new TextField();

        String[] testQueries = {"thor", "THOR", "ThOr", "tHoR"};

        for (String query : testQueries) {
            testHomeController.searchField.setText(query);

            testHomeController.applyFilter();

            assertFalse(testHomeController.observableMovies.isEmpty(), "Filtered list should not be empty for query " + query);
            assertEquals("Thor", testHomeController.observableMovies.get(0).getTitle(),
                    "Search result should be 'Thor' regardless of case sensitivity.");
        }
    }

    //--------------------------------------- Unit tests Genre Filter ---------------------------------------//

    @Test
    public void test_for_specific_Genre() {
        HomeController testHomeController = new HomeController();

        testHomeController.allMovies = Movie.initializeMovies();

        testHomeController.searchField = new TextField(null);

        testHomeController.genreComboBox = new JFXComboBox<>();
        testHomeController.genreComboBox.setValue(Movie.Genre.DRAMA);

        testHomeController.applyFilter();

        assertFalse(testHomeController.observableMovies.isEmpty(), "Filtered list should not be empty.");

        assertTrue(testHomeController.observableMovies.stream()
                        .allMatch(movie -> movie.getGenre().contains(Movie.Genre.DRAMA)),
                "All filtered movies should be of genre DRAMA.");
    }

    @Test
    public void test_for_specific_Query_and_Genre() {
        HomeController testHomeController = new HomeController();

        testHomeController.allMovies = Movie.initializeMovies();

        testHomeController.searchField = new TextField("Iron Man");

        testHomeController.genreComboBox = new JFXComboBox<>();
        testHomeController.genreComboBox.setValue(Movie.Genre.ACTION);

        testHomeController.applyFilter();

        assertEquals("Iron Man", testHomeController.observableMovies.get(0).getTitle(), "The filtered movie should be Iron Man.");
    }

}
