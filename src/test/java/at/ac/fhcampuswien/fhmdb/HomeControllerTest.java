package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HomeControllerTest {

    private static HomeController testHomeController;

    @BeforeAll
    public static void init() {
        System.out.println("~~~~~~~~ Testing HomeController ~~~~~~~~");
        testHomeController = new HomeController();

        // Initialisiert JavaFX für Tests
        Platform.startup(() -> {
        });
    }

    @Test
    public void when_initialized_allMovies_equal_to_observableMoviesList() {
        assertEquals(testHomeController.allMovies, testHomeController.observableMovies);
    }


    @Test
    public void check_if_sorting_all_movies_ascending() {
        // given
        testHomeController.setMovieList(Movie.initializeMovies());

        // when
        testHomeController.sortMoviesAscending();

        // then
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Movie.Genre.DRAMA, Movie.Genre.ROMANCE, Movie.Genre.BIOGRAPHY)),
                new Movie(
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Movie.Genre.CRIME, Movie.Genre.DRAMA, Movie.Genre.MYSTERY)),
                new Movie(
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Movie.Genre.COMEDY, Movie.Genre.FAMILY, Movie.Genre.ANIMATION))
        );

        assertEquals(expected, testHomeController.observableMovies);

    }

    @Test
    public void check_if_sorting_all_movies_descending() {
        // given
        testHomeController.setMovieList(Movie.initializeMovies());

        // when
        testHomeController.sortMoviesDescending();

        // then
        List<Movie> expected = Arrays.asList(
                new Movie(
                        "Puss in Boots",
                        "An outlaw cat, his childhood egg-friend, and a seductive thief kitty set out in search for the eggs of the fabled Golden Goose to clear his name, restore his lost honor, and regain the trust of his mother and town.",
                        Arrays.asList(Movie.Genre.COMEDY, Movie.Genre.FAMILY, Movie.Genre.ANIMATION)),
                new Movie(
                        "The Usual Suspects",
                        "A sole survivor tells of the twisty events leading up to a horrific gun battle on a boat, which begin when five criminals meet at a seemingly random police lineup.",
                        Arrays.asList(Movie.Genre.CRIME, Movie.Genre.DRAMA, Movie.Genre.MYSTERY)),
                new Movie(
                        "The Wolf of Wall Street",
                        "Based on the true story of Jordan Belfort, from his rise to a wealthy stock-broker living the high life to his fall involving crime, corruption and the federal government.",
                        Arrays.asList(Movie.Genre.DRAMA, Movie.Genre.ROMANCE, Movie.Genre.BIOGRAPHY))
        );

        assertEquals(expected, testHomeController.observableMovies);

    }

    /*
        List<Movie> ExpectedMoviesList = Arrays.asList(
                new Movie("Ant-Man", 2021),
                new Movie("Iron Man", 2022),
                new Movie("The Godfather", 2023)
        );

        ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                new Movie("The Godfather", 2021),
                new Movie("Iron Man", 2022),
                new Movie("Ant-Man", 2023)
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
                new Movie("The Godfather", 2021),
                new Movie("Iron Man", 2022),
                new Movie("Ant-Man", 2023)
        );

        ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                new Movie("The Godfather", 2021),
                new Movie("Ant-Man", 2022),
                new Movie("Iron Man", 2023)
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
                new Movie("C-Movie", "Whats happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("A-Movie", "Whats happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY))
        );

        ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                new Movie("A-Movie", "Whats happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("B-Movie", "Whats happening", 0, List.of(Movie.Genre.HISTORY)),
                new Movie("C-Movie", "Whats happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY))
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
                new Movie("A-Movie", "Whats happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("C-Movie", "Whats happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY))
        );

        ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                new Movie("C-Movie", "Whats happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("A-Movie", "Whats happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("B-Movie", "Whats happening", 0, List.of(Movie.Genre.HISTORY))
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
    void test_checks_if_ApplyFilter_MethodExists() { //test generally not needed - method already implemented - usage was only for the exercise
        try {
            HomeController testHomeController = new HomeController();
            Method method = testHomeController.getClass().getDeclaredMethod("applyFilter");
            assertNotNull(method, "The method 'applyFilter' should exist");
        } catch (NoSuchMethodException e) {
            fail("The Method 'applyFilter' does not exist");
        }
    }

    //--------------------------------------- Unit tests Query Filter ---------------------------------------//

    /*
    @Test
    public void test_specific_Search_for_Movie_Thor() {
        HomeController testHomeController = new HomeController();

        Movie thor = new Movie("Thor", "A powerfull but arrogant god is cast down to Earth", 0, List.of(Movie.Genre.ACTION));
        Movie ironMan = new Movie("Iron Man", "A billionaire builds a high-tech suit", 0, List.of(Movie.Genre.ACTION));
        Movie theAvengers = new Movie("The Avengers", "Superheros team up to save the world", 0, List.of(Movie.Genre.ACTION));

        testHomeController.allMovies = List.of(thor, ironMan, theAvengers);

        testHomeController.searchField = new TextField("Thor");
        testHomeController.genreComboBox = new JFXComboBox<>();

        testHomeController.applyFilter();

        assertEquals(1, testHomeController.observableMovies.size(), "Only one movie should be left");
        assertEquals("Thor", testHomeController.observableMovies.get(0).getTitle(), "The filtered movie should be Thor.");
    }

    @Test
    public void test_specific_Search_for_Movie_TheAvengers() {
        HomeController testHomeController = new HomeController();

        Movie thor = new Movie("Thor", "A powerfull but arrogant god is cast down to Earth", 0, List.of(Movie.Genre.ACTION));
        Movie ironMan = new Movie("Iron Man", "A billionaire builds a high-tech suit", 0, List.of(Movie.Genre.ACTION));
        Movie theAvengers = new Movie("The Avengers", "Superheros team up to save the world", 0, List.of(Movie.Genre.ACTION));

        testHomeController.allMovies = List.of(thor, ironMan, theAvengers);

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
*/
    /*
    @Test
    public void test_if_ApplyFilter_Ignores_CaseSensitivity() {
        HomeController testHomeController = new HomeController();

        Movie thor = new Movie("Thor", "A powerfull but arrogant god is cast down to Earth", 0, List.of(Movie.Genre.ACTION));
        Movie ironMan = new Movie("Iron Man", "A billionaire builds a high-tech suit", 0, List.of(Movie.Genre.ACTION));
        Movie theAvengers = new Movie("The Avengers", "Superheros team up to save the world", 0, List.of(Movie.Genre.ACTION));

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

        Movie thor = new Movie("Thor", "A powerfull but arrogant god is cast down to Earth", 0, List.of(Movie.Genre.DRAMA));
        Movie ironMan = new Movie("Iron Man", "A billionaire builds a high-tech suit", 0, List.of(Movie.Genre.ACTION));
        Movie theAvengers = new Movie("The Avengers", "Superheros team up to save the world", 0, List.of(Movie.Genre.ACTION));

        testHomeController.allMovies = List.of(thor, ironMan, theAvengers);

        testHomeController.searchField = new TextField(null);

        testHomeController.genreComboBox = new JFXComboBox<>();
        testHomeController.genreComboBox.setValue(Movie.Genre.DRAMA);

        testHomeController.applyFilter();

        assertFalse(testHomeController.observableMovies.isEmpty(), "Filtered list should not be empty.");

        assertTrue(testHomeController.observableMovies.stream()
                        .allMatch(movie -> movie.getGenre().contains(Movie.Genre.DRAMA)),
                "All filtered movies should be of genre DRAMA.");
    }


    /*
    @Test
    public void test_for_specific_Query_and_Genre() {
        HomeController testHomeController = new HomeController();

        Movie thor = new Movie("Thor", "A powerfull but arrogant god is cast down to Earth", 0, List.of(Movie.Genre.ACTION));
        Movie ironMan = new Movie("Iron Man", "A billionaire builds a high-tech suit", 0, List.of(Movie.Genre.ACTION));
        Movie theAvengers = new Movie("The Avengers", "Superheros team up to save the world", 0, List.of(Movie.Genre.ACTION));

        testHomeController.allMovies = List.of(thor, ironMan, theAvengers);

        testHomeController.searchField = new TextField("Iron Man");

        testHomeController.genreComboBox = new JFXComboBox<>();
        testHomeController.genreComboBox.setValue(Movie.Genre.ACTION);

        testHomeController.applyFilter();

        assertEquals("Iron Man", testHomeController.observableMovies.get(0).getTitle(), "The filtered movie should be Iron Man.");
    }
     */



    //--------------------------------------- Unit tests Exercise 2 ---------------------------------------//


}
