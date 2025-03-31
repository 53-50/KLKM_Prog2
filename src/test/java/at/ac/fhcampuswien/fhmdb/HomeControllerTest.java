package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class HomeControllerTest {

    private HomeController testHomeController;
    private List<Movie> dummyMovies;

    @BeforeAll
    public static void init() {
        System.out.println("~~~~~~~~ Testing MovieAPI ~~~~~~~~");
    }

    @BeforeEach
    public void setUp() {
        //initialize new home controller before each UT
        testHomeController = new HomeController();

        //Dummy-Set for UT
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

        //Initialize dummy set for each UT
        testHomeController.setMovies(dummyMovies);
        testHomeController.setMovieList(dummyMovies);
    }

    //--------------------------------------- Unit tests Exercise 1 ---------------------------------------//
 /*
    @BeforeAll
    public static void init() {
        HomeController testHomeController = new HomeController();
    }
    @Test
    public void check_if_sorting_all_movies_ascending() {
        List<Movie> ExpectedMoviesList = Arrays.asList(
                new Movie("2", "Thor", "God of Thunder",
                        Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY), 2011, "", 115, 7.5),
                new Movie("3", "Batman", "test",
                        Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY), 2011, "", 115, 7.5),
                new Movie("4", "Iron Man", "test",
                        Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY), 2011, "", 115, 7.5)
        );

        ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                new Movie("3", "Batman", "test",
                        Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY), 2011, "", 115, 7.5),
                new Movie("4", "Iron Man", "test",
                        Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY), 2011, "", 115, 7.5),
                new Movie("2", "Thor", "God of Thunder",
                        Arrays.asList(Movie.Genre.ACTION, Movie.Genre.FANTASY), 2011, "", 115, 7.5)
        );

        // HomeController testHomeController = new HomeController();

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
                new Movie("C-Movie", "What's happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("A-Movie", "What's happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY))
        );

        ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                new Movie("A-Movie", "What's happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("B-Movie", "What's happening", 0, List.of(Movie.Genre.HISTORY)),
                new Movie("C-Movie", "What's happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY))
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
                new Movie("A-Movie", "What's happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("C-Movie", "What's happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY))
        );

        ObservableList<Movie> observableMoviesList = FXCollections.observableArrayList(
                new Movie("C-Movie", "What's happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("A-Movie", "What's happening", 0, List.of(Movie.Genre.DRAMA, Movie.Genre.COMEDY)),
                new Movie("B-Movie", "What's happening", 0, List.of(Movie.Genre.HISTORY))
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

        Movie thor = new Movie("Thor", "A powerful but arrogant god is cast down to Earth", 0, List.of(Movie.Genre.ACTION));
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

        Movie thor = new Movie("Thor", "A powerful but arrogant god is cast down to Earth", 0, List.of(Movie.Genre.ACTION));
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

        Movie thor = new Movie("Thor", "A powerful but arrogant god is cast down to Earth", 0, List.of(Movie.Genre.ACTION));
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

        Movie thor = new Movie("Thor", "A powerful but arrogant god is cast down to Earth", 0, List.of(Movie.Genre.DRAMA));
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

        Movie thor = new Movie("Thor", "A powerful but arrogant god is cast down to Earth", 0, List.of(Movie.Genre.ACTION));
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
    void when_initialized_allMovies_equal_to_observableMoviesList() {
        //given - setup()
        //when already setup
        //then
        assertEquals(testHomeController.allMovies, testHomeController.observableMovies);
    }

    @Test
    void check_if_sorting_all_movies_ascending() {

        //given
        List<String> expTitle = dummyMovies.stream()
                .map(Movie::getTitle)
                .sorted()
                .collect(Collectors.toList());

        //when
        testHomeController.sortMoviesAscending();

        //then
        List<String> actTitle = testHomeController.observableMovies.stream()
                .map(Movie::getTitle)
                .collect(Collectors.toList());

        Assertions.assertEquals(expTitle, actTitle);

        /*
        System.out.println("Expected: " + expTitle);
        System.out.println("Actual: " + actTitle);
        */
    }

    @Test
    public void test_sortMoviesDescending() {
        //given
        List<String> expTitles = dummyMovies.stream()
                .map(Movie::getTitle)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        //when
        testHomeController.sortMoviesDescending();

        //then
        List<String> actTitles = testHomeController.observableMovies.stream()
                .map(Movie::getTitle)
                .collect(Collectors.toList());


        assertEquals(expTitles, actTitles);

        //System.out.println("Expected: " + expTitles);
        //System.out.println("Actual: " + actTitles);
    }

    @Test
    void if_last_sort_descending_next_sort_should_be_ascending() {
        //System.out.println("Debug: OG movie list");
        //dummyMovies.forEach(movie -> System.out.println(movie.getTitle()));

        //given
        List<String> expectedTitles = dummyMovies.stream()
                .map(Movie::getTitle)
                .sorted()
                .collect(Collectors.toList());

        //when
        testHomeController.sortMoviesDescending();
        //System.out.println("\nDebug: Movies descending");
        //testHomeController.observableMovies.forEach(movie -> System.out.println(movie.getTitle()));

        testHomeController.sortMoviesAscending();
        //System.out.println("\nDebug: Movies ascending");
        //testHomeController.observableMovies.forEach(movie -> System.out.println(movie.getTitle()));

        //then
        List<String> actualTitles = testHomeController.observableMovies.stream()
                .map(Movie::getTitle)
                .collect(Collectors.toList());

        //System.out.println("\nExpected: sorted");
        //expectedTitles.forEach(System.out::println);

        assertEquals(expectedTitles, actualTitles);
    }


    //--------------------------------------- Unit tests Query Filter ---------------------------------------//


    @Test
    void test_specific_Search_for_Movie_Thor() {
        //given
        String query = "Thor";

        //when
        List<Movie> filtered = testHomeController.filterMovies(query, null, null, null);

        //then
        assertFalse(filtered.isEmpty(), "There are no movies");
        boolean found = filtered.stream()
                .anyMatch(m -> m.getTitle().equals("Thor") ||
                        (m.getDescription() != null && m.getDescription().toLowerCase().contains("thor")));
        assertTrue(found, "There are movies");
    }

    @Test
    void query_filter_with_null_value_returns_unfiltered_list() {
        //given
        List<Movie> expected = new ArrayList<>(dummyMovies);

        //when
        List<Movie> actual = testHomeController.filterMovies(null, null, null, null);

        //then
        assertEquals(expected, actual, "If query null it returns unfiltered list");
    }

    @Test
    void query_filter_with_no_input_value_returns_unfiltered_list() {
        //given
        List<Movie> expected = new ArrayList<>(dummyMovies);
        String query = "";

        //when
        List<Movie> actual = testHomeController.filterMovies(query, null, null, null);

        //then
        assertEquals(expected, actual, "If genre null it returns unfiltered list");
    }

    @Test
    void test_if_ApplyFilter_Ignores_CaseSensitivity() {
        //given
        String query = "tHoR";

        //when
        List<Movie> filtered = testHomeController.filterMovies(query, null, null, null);

        //then
        boolean found = filtered.stream()
                .anyMatch(m -> m.getTitle().equalsIgnoreCase("Thor"));
        assertTrue(found, "Thor is found");
    }

    @Test
    void test_for_specific_Genre() {
        //given
        Movie.Genre genre = Movie.Genre.ACTION;

        //when
        List<Movie> filtered = testHomeController.filterMovies(null, genre, null, null);

        //then
        assertFalse(filtered.isEmpty(), "Genre should not be empty");
        for (Movie m : filtered) {
            assertTrue(m.getGenres().contains(Movie.Genre.ACTION), "Genre is found");
        }
    }

    @Test
    void test_for_specific_Year() {
        //given
        int year = 2011;

        //when
        List<Movie> filtered = testHomeController.filterMovies(null, null, year, null);

        //then
        assertFalse(filtered.isEmpty(), "Year should not be empty");
        for (Movie m : filtered) {
            assertEquals(year, m.getReleaseYear(), "Year is found");
        }
    }

    @Test
    void test_for_specific_Rating() {
        //given
        int rating = 8;

        //when
        List<Movie> filtered = testHomeController.filterMovies(null, null, null, rating);

        //then
        assertFalse(filtered.isEmpty(), "Rating should not be empty");
        for (Movie m : filtered) {
            assertTrue(m.getRating() >= 8, "Rating is found");
        }
    }

    @Test
    void test_for_specific_Query_and_Genre() {
        //given
        String query = "iron";
        Movie.Genre genre = Movie.Genre.ACTION;

        //when
        List<Movie> filtered = testHomeController.filterMovies(query, genre, null, null);

        //then
        assertFalse(filtered.isEmpty(), "Query and Genre should not be empty");
        for (Movie m : filtered) {
            assertTrue(m.getTitle().toLowerCase().contains("iron"));
            assertTrue(m.getGenres().contains(Movie.Genre.ACTION));
        }
    }

    @Test
    void test_filter_null_list_throws_exception() {
        //given
        testHomeController.setMovies(null);

        //when
        Exception e = assertThrows(NullPointerException.class, () ->
                testHomeController.filterMovies("anything", null, null, null));

        //then
        assertNotNull(e);
    }


    //--------------------------------------------------------- UNIT TESTS - Streams -------------------------------------------------------//
    @Test
    void test_to_get_Movies_Between_Years() {
        List<Movie> filteredMovies = testHomeController.getMoviesBetweenYears(dummyMovies, 2008, 2012);

        assertEquals(4, filteredMovies.size(), "Should return exactly 4 movies within the year range.");
        assertTrue(filteredMovies.stream().anyMatch(movie -> movie.getTitle().equals("The Avengers")), "Should include 'The Avengers'.");
        assertTrue(filteredMovies.stream().anyMatch(movie -> movie.getTitle().equals("Thor")), "Should include 'Thor'.");
        assertTrue(filteredMovies.stream().anyMatch(movie -> movie.getTitle().equals("Iron Man")), "Should include 'Iron Man'.");
        assertTrue(filteredMovies.stream().anyMatch(movie -> movie.getTitle().equals("Inception")), "Should include 'Inception'.");

        //no movies in the given range
        List<Movie> noMovies = testHomeController.getMoviesBetweenYears(dummyMovies, 1990, 2000);
        assertTrue(noMovies.isEmpty(), "Should return no movies as none are within the range 1990-2000.");

        //all movies included
        List<Movie> allMoviesInRange = testHomeController.getMoviesBetweenYears(dummyMovies, 2005, 2012);
        assertEquals(5, allMoviesInRange.size(), "Should return all 5 movies as all fall within the range 2005-2012.");
    }

    @Test
    void countMoviesFrom_should_return_correct_number_of_movies_by_director_with_setter() {
        // given
        Movie movieA = new Movie("1", "Movie A", "Description", List.of(Movie.Genre.ACTION), 2020, "", 120, 7.5);
        movieA.getDirectors().add("Director X");

        Movie movieB = new Movie("2", "Movie B", "Description", List.of(Movie.Genre.COMEDY), 2021, "", 90, 6.5);
        movieB.getDirectors().add("Director Y");

        Movie movieC = new Movie("3", "Movie C", "Description", List.of(Movie.Genre.DRAMA), 2022, "", 110, 8.0);
        movieC.getDirectors().add("Director X");

        List<Movie> testMovies = Arrays.asList(movieA, movieB, movieC);

        // when
        long moviesByDirectorX = testHomeController.countMoviesFrom(testMovies, "Director X");
        long moviesByDirectorY = testHomeController.countMoviesFrom(testMovies, "Director Y");
        long moviesByDirectorZ = testHomeController.countMoviesFrom(testMovies, "Director Z");

        // then
        assertEquals(2, moviesByDirectorX);
        assertEquals(1, moviesByDirectorY);
        assertEquals(0, moviesByDirectorZ);
    }

    @Test
    void test_to_get_Longest_Movie_Title() {

        //regular test - longest title
        int longestTitleLength = testHomeController.getLongestMovieTitle(dummyMovies);
        assertEquals("The Avengers".length(), longestTitleLength, "Longest movie title should be 'The Avengers'.");

        //empty movie list
        List<Movie> emptyMoviesList = new ArrayList<>();
        assertEquals(0, testHomeController.getLongestMovieTitle(emptyMoviesList), "Should return 0 for empty movie list.");

        //all titles are the same length
        List<Movie> equalLengthMovies = Arrays.asList(
                new Movie("6", "ABCD", "Description", List.of(Movie.Genre.ACTION), 2020, "", 120, 7.0),
                new Movie("7", "WXYZ", "Description", List.of(Movie.Genre.DRAMA), 2021, "", 130, 8.0));
        assertEquals(4, testHomeController.getLongestMovieTitle(equalLengthMovies), "Should correctly return the length when all titles are equal.");
    }

    @Test
    void test_to_get_most_popular_actor() {
        Movie movieA = new Movie("1", "Movie A", "Description", List.of(Movie.Genre.ACTION), 2020, "", 120, 7.5);
        movieA.getMainCast().add("Actor1");

        Movie movieB = new Movie("2", "Movie B", "Description", List.of(Movie.Genre.COMEDY), 2021, "", 90, 6.5);
        movieB.getMainCast().add("Actor2");

        Movie movieC = new Movie("3", "Movie C", "Description", List.of(Movie.Genre.DRAMA), 2022, "", 110, 8.0);
        movieC.getMainCast().add("Actor1");

        List<Movie> testMoviesActors = Arrays.asList(movieA, movieB, movieC);

        // when
        String popularActor = testHomeController.getMostPopularActor(testMoviesActors);
        //System.out.println("DEBUG - Most popular actor found: " + popularActor);

        // then
        assertEquals("Actor1", popularActor,
                "Actor1 should be the most popular.");
    }
}
