package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController testHomeController;
    private List<Movie> dummyMovies;

    @BeforeEach
    public void setUp() {
        testHomeController = new HomeController();

        dummyMovies = new ArrayList<>();
        dummyMovies.add(new Movie("1", "The Avengers", "Superhero movie",
                Arrays.asList(Movie.Genre.ACTION, Movie.Genre.ADVENTURE), 2012, "", 143, 8.0));
        dummyMovies.add(new Movie("2", "Thor", "God of Thunder",
                Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY), 2011, "", 115, 7.5));
        dummyMovies.add(new Movie("3", "Batman", "Dark knight",
                Arrays.asList(Movie.Genre.DRAMA, Movie.Genre.ACTION), 2005, "", 140, 7.8));
        dummyMovies.add(new Movie("4", "Iron Man", "Billionaire superhero",
                Arrays.asList(Movie.Genre.ACTION, Movie.Genre.SCIENCE_FICTION), 2008, "", 126, 7.9));
        dummyMovies.add(new Movie("5", "Inception", "Mind bending thriller",
                Arrays.asList(Movie.Genre.SCIENCE_FICTION, Movie.Genre.THRILLER), 2010, "", 148, 8.8));

        testHomeController.setMovies(dummyMovies);
        testHomeController.setMovieList(dummyMovies);
    }

    //--------------------------------------- Unit tests Exercise 1 ---------------------------------------//

/*

  //  @BeforeAll
  //  public static void init() {
  //      HomeController testHomeController = new HomeController();
  //  }
    @Test
    public void check_if_sorting_all_movies_ascending() {
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

    @Test
    public void when_initialized_allMovies_equal_to_observableMoviesList() {
        assertEquals(testHomeController.allMovies, testHomeController.observableMovies);
    }

    @Test
    public void check_if_sorting_all_movies_ascending() {
        List<Movie> unsorted = new ArrayList<>(dummyMovies);
        java.util.Collections.reverse(unsorted);
        testHomeController.setMovies(unsorted);
        testHomeController.setMovieList(unsorted);

        testHomeController.sortMoviesAscending();
        List<Movie> sorted = testHomeController.observableMovies;
        List<String> titles = new ArrayList<>();
        for (Movie movie : sorted) {
            titles.add(movie.getTitle());
        }
        List<String> expected = new ArrayList<>(titles);
        java.util.Collections.sort(expected);
        assertEquals(expected, titles);
    }


    @Test
    public void check_if_sorting_all_movies_descending() {
        List<Movie> unsorted = new ArrayList<>(dummyMovies);
        testHomeController.setMovies(unsorted);
        testHomeController.setMovieList(unsorted);

        testHomeController.sortMoviesDescending();
        List<Movie> sorted = testHomeController.observableMovies;
        List<String> titles = new ArrayList<>();
        for (Movie movie : sorted) {
            titles.add(movie.getTitle());
        }
        List<String> expected = new ArrayList<>(titles);
        expected.sort(java.util.Collections.reverseOrder());
        assertEquals(expected, titles);
    }

/*
    @Test
    void check_if_movie_filtered_desc_afterwards_apply_genre() {

    }

    @Test
    void check_if_movie_filtered_asc_afterwards_apply_genre() {

    }

 */

    //--------------------------------------- Unit tests Query Filter ---------------------------------------//


    @Test
    public void test_specific_Search_for_Movie_Thor() {
        List<Movie> filtered = testHomeController.filterMovies("Thor", null, null, null);
        assertFalse(filtered.isEmpty());

        boolean found = filtered.stream()
                .anyMatch(m -> m.getTitle().equals("Thor") ||
                (m.getDescription() != null && m.getDescription().toLowerCase().contains("thor")));
                assertTrue(found);
    }

    @Test
    public void test_if_Query_and_Genre_Null_or_Empty_returns_unfiltered_List() { //TODO
            List<Movie> allMovies = testHomeController.allMovies;

            List<Movie> filteredNull = testHomeController.filterMovies(null, null, null, null);
            List<Movie> filteredEmpty = testHomeController.filterMovies("", null, null, null);

            assertEquals(allMovies.size(), filteredNull.size(), "Null query should return unfiltered list");
            assertEquals(allMovies.size(), filteredEmpty.size(), "Empty query should return unfiltered list");
        }

    @Test
    public void test_if_ApplyFilter_Ignores_CaseSensitivity() {
        List<Movie> filtered = testHomeController.filterMovies("tHoR", null, null, null);
        boolean found = filtered.stream()
                .anyMatch(m -> m.getTitle().equalsIgnoreCase("Thor"));
        assertTrue(found);
    }

    @Test
    public void test_for_specific_Genre() {
        List<Movie> filtered = testHomeController.filterMovies("", Movie.Genre.ACTION, null, null);
        for (Movie m : filtered) {
            assertTrue(m.getGenres().contains(Movie.Genre.ACTION));
        }
    }

    @Test
    public void test_for_specific_Query_and_Genre() {
        List<Movie> filtered = testHomeController.filterMovies("iron", Movie.Genre.ACTION, null, null);
        for (Movie m : filtered) {
            assertTrue(m.getTitle().toLowerCase().contains("iron"));
            assertTrue(m.getGenres().contains(Movie.Genre.ACTION));
        }
    }

    @Test
    public void test_filter_null_list_throws_exception() {
        testHomeController.setMovies(null);
        Exception e = assertThrows(NullPointerException.class, () ->
                testHomeController.filterMovies("anything", null, null, null));
        assertNotNull(e);
    }


}
