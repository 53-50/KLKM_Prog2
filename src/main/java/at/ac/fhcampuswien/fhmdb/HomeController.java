package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.data.*;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.DialogWindow;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HomeController implements Initializable {

    @FXML public JFXButton searchBtn;
    @FXML public TextField searchField;
    @FXML public JFXComboBox<Movie.Genre> genreComboBox;
    @FXML public JFXComboBox<Integer> releaseYearComboBox;
    @FXML public JFXComboBox<Integer> ratingComboBox;
    @FXML public JFXButton sortBtn;
    @FXML public JFXButton deleteBtn;


    @FXML private JFXButton homeButton;
    @FXML private JFXListView<Movie> movieListView;
    @FXML private JFXButton watchlistButton;  // passend zum neuen fx:id TODO

    @FXML
    public JFXListView Listview;

    public List<Movie> allMovies;

    protected static ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    // automatically updates corresponding UI elements when underlying data changes

    private MovieRepository movieRepository;
    private WatchlistRepository watchlistRepository;


    // ganz oben im Controller:
    private WatchlistRepository watchlistRepo;



    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
        // in initialize():
        watchlistRepo = new WatchlistRepository();

        try {
            System.out.println("Initializing HomeController...");

            // Repository Initialization
            movieRepository = new MovieRepository();
            watchlistRepository = new WatchlistRepository();
            System.out.println("Repositories initialized");

            // Load movies
            loadMovies();

            // UI Setup
            setupUI();

            System.out.println("Initialization complete");
        } catch (Exception e) {
            System.err.println("Initialization failed: " + e.getMessage());
            e.printStackTrace();
            showError("Initialization failed: " + e.getMessage());
        }
    }

    private void loadMovies() throws SQLException, IOException {
        System.out.println("Loading movies...");
        List<Movie> apiMovies = MovieAPI.fetchAllMovies();
        System.out.println("Fetched " + apiMovies.size() + " movies from API");

        if (!apiMovies.isEmpty()) {
            movieRepository.addAllMovies(apiMovies);
            System.out.println("Movies saved to database");
        }

        List<MovieEntity> entities = movieRepository.getAllMovies();
        System.out.println("Loaded " + entities.size() + " movies from database");



        ClickEventHandler<Movie> addToWatchlist = m -> {
            try {
                int count = watchlistRepo.addToWatchlist(m);
                String msg = count == 1
                        ? m.getTitle() + " wurde zur Watchlist hinzugefügt."
                        : m.getTitle() + " steht bereits auf der Watchlist.";
                new DialogWindow("Watchlist", msg).show();
            } catch (DatabaseException e) {
                new DialogWindow("Datenbank-Fehler",
                        "Konnte nicht zur Watchlist hinzufügen.").show();
                e.printStackTrace();
            }
        };
        movieListView.setCellFactory(lv -> new MovieCell(addToWatchlist));


        List<Movie> movies = MovieEntity.toMovies(entities);
        setMovies(movies);
        setMovieList(movies);
        sortMoviesDescending();


    }

    private void setupUI() {
        movieListView.setItems(observableMovies);

        // genre filter text + data
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.Genre.values()); //addToWatchlist Genres to ComboBox

        //releaseYear filter text + data
        releaseYearComboBox.setPromptText("Filter by Release Year");

        //creates Array for years 1900 to 2025
        Integer[] years = new Integer[126];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }

        //creates dropdown menu
        releaseYearComboBox.getItems().addAll(years);

        // rating filter text + data
        ratingComboBox.setPromptText("Filter by Rating");

        //crates array from 0-10
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }

        //ratings in dropbox
        ratingComboBox.getItems().addAll(ratings);


        // ------------------------------------  EVENT HANDLER:   ------------------------------------------------- //

        // Sort button:
        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort (asc)")) {

                // sort observableMovies ascending
                sortMoviesAscending();
                sortBtn.setText("Sort (desc)");
            } else {
                // sort observableMovies descending
                sortMoviesDescending();
                sortBtn.setText("Sort (asc)");
            }
        });

        // Search Button:
        searchBtn.setOnAction(actionEvent -> applyFilter());

        //Genre Dropdown:
        genreComboBox.setOnAction(actionEvent -> applyFilter());

        //ReleaseYear Dropdown:
        releaseYearComboBox.setOnAction(actionEvent -> applyFilter());

        //Rating Dropdown:
        ratingComboBox.setOnAction(actionEvent -> applyFilter());

        //Delete Button:
        deleteBtn.setOnAction(actionEvent -> deleteFilter());

    }

/*
        // 1) Repository-Init
        try {
            System.out.println("HomeController Start");

            movieRepository     = new MovieRepository();
            watchlistRepository = new WatchlistRepository();
        } catch (DatabaseException e) {
            showError("Datenbank konnte nicht initialisiert werden:\n" + e.getMessage());
            return;
        }

        List<Movie> apiMovies = new ArrayList<>();
        try {
            apiMovies = MovieAPI.fetchAllMovies();
            if(!apiMovies.isEmpty()) {
                movieRepository.addAllMovies(apiMovies);
            }
        } catch (IOException e) {
            System.err.println("Error initializing movies " + e.getMessage());
            apiMovies = new ArrayList<>();
        }

     try {
         List<MovieEntity> entities = movieRepository.getAllMovies();
         List<Movie> movies = MovieEntity.toMovies(entities);
         setMovies(movies);
         setMovieList(movies);
         sortMoviesDescending();
     } catch (SQLException e) {
         showError("Error" + e.getMessage());
     }

     movieListView.setItems(observableMovies);
     movieListView.setCellFactory(lv -> new MovieCell(addToWatchlistClick));

     /*
        // lade aus DB – egal ob API neu oder leerer Fallback
        List<MovieEntity> entities;
        try {
            entities = movieRepository.getAllMovies();
        } catch (SQLException se) {
            entities = Collections.emptyList();
        }
        List<Movie> movies = MovieEntity.toMovies(entities);

        // in die ListView stecken
        setMovies(movies);
        setMovieList(movies);
        sortMoviesDescending();
        movieListView.setItems(observableMovies);
        movieListView.setCellFactory(lv -> new MovieCell(addToWatchlistClick));



        // genre filter text + data
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.Genre.values()); //addToWatchlist Genres to ComboBox

        //releaseYear filter text + data
        releaseYearComboBox.setPromptText("Filter by Release Year");

        //creates Array for years 1900 to 2025
        Integer[] years = new Integer[126];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }

        //creates dropdown menu
        releaseYearComboBox.getItems().addAll(years);

        // rating filter text + data
        ratingComboBox.setPromptText("Filter by Rating");

        //crates array from 0-10
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }

        //ratings in dropbox
        ratingComboBox.getItems().addAll(ratings);


        // ------------------------------------  EVENT HANDLER:   ------------------------------------------------- //

        // Sort button:
        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort (asc)")) {

                // sort observableMovies ascending
                sortMoviesAscending();
                sortBtn.setText("Sort (desc)");
            } else {
                // sort observableMovies descending
                sortMoviesDescending();
                sortBtn.setText("Sort (asc)");
            }
        });

        // Search Button:
        searchBtn.setOnAction(actionEvent -> applyFilter());

        //Genre Dropdown:
        genreComboBox.setOnAction(actionEvent -> applyFilter());

        //ReleaseYear Dropdown:
        releaseYearComboBox.setOnAction(actionEvent -> applyFilter());

        //Rating Dropdown:
        ratingComboBox.setOnAction(actionEvent -> applyFilter());

        //Delete Button:
        deleteBtn.setOnAction(actionEvent -> deleteFilter());
    }
    */

    public void setMovies(List<Movie> movies) {
        allMovies = movies;
    }

    public void setMovieList(List<Movie> movies) {
        observableMovies.clear();
        observableMovies.addAll(movies);
    }

    //methode to apply a filter based on search request and/or chosen genre
    public void applyFilter() {
        String query = getSearchQuery();
        Movie.Genre selectedGenre = getSelectedGenre();
        Integer selectedYear = releaseYearComboBox.getValue();
        Integer selectedRating = ratingComboBox.getValue();
        List<Movie> filteredMovies = filterMovies(query, selectedGenre, selectedYear, selectedRating);

        //System.out.println("Debugging - filtered movies: " + filteredMovies.stream().map(Movie::getTitle).collect(Collectors.joining(", ")));

        updateObservableList(filteredMovies); //-> API relevant (exercise 2)

    }

    public void deleteFilter() {
        searchField.clear();
        genreComboBox.setValue(null);
        releaseYearComboBox.setValue(null);
        ratingComboBox.setValue(null);
        updateObservableList(allMovies);
    }

    private String getSearchQuery() {
        return (searchField != null && searchField.getText() != null) //makes sure that searchField and searchField.getText() are not null before .trim() is called.
                ? searchField.getText().trim().toLowerCase()          // -> necessary for unit test with null
                : ""; //if search field does not exist or entered text is empty, empty string is returned
    }                 // ? : -> ternary operator -> short form for if-else



    private Movie.Genre getSelectedGenre() {
        return genreComboBox.getValue();
    }



    public List<Movie> filterMovies(String query, Movie.Genre selectedGenre, Integer selectedYear, Integer selectedRating) {
        if (allMovies == null) {
            throw new NullPointerException("Movie list is null");
        }
        return allMovies.stream()
                .filter(movie -> query == null || query.isBlank() ||
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(query.toLowerCase())))
                .filter(movie -> selectedGenre == null || movie.getGenres().contains(selectedGenre))
                .filter(movie -> selectedYear == null || movie.getReleaseYear() == selectedYear)
                .filter(movie -> selectedRating == null || movie.getRating() >= selectedRating)
                .collect(Collectors.toList());
    }

    private void updateObservableList(List<Movie> filteredMovies) {
        observableMovies.setAll(filteredMovies);
    }


    public void sortMoviesAscending(){
        FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle));
        allMovies.sort(Comparator.comparing(Movie::getTitle));
    }

    public void sortMoviesDescending() {
        FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle).reversed());
        allMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
    }


    public String getMostPopularActor (List<Movie> movies) {
        // Store all actors from movies in list
        List<String> allActors = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.toList());

        // Count how many times a specific actor is in list
        Map<String, Long> actorCountMap = allActors.stream()
        .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));

        // Find actor with most appearances
        Optional<Map.Entry<String, Long>> mostPopular = actorCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        // If present return actor, else null
        if (mostPopular.isPresent()) {
            return mostPopular.get().getKey();
        } else {
            return null;
        }
    }

    public int getLongestMovieTitle (List<Movie> movies) {
        // Count length of every movie and store in list
        List<Integer> titleLengths = movies.stream()
                .map(movie -> movie.getTitle().length())
                .collect(Collectors.toList());

        // Find maximum
        Optional<Integer> maxLength = titleLengths.stream()
                .max(Integer::compareTo);

        // If present return length, else 0
        if (maxLength.isPresent()) {
            return maxLength.get();
        } else {
            return 0;
        }
    }

    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    public List<Movie> getMoviesBetweenYears (List<Movie> movies, int startYear, int endYear) {
        // Filter all movies to specified year range
        Stream<Movie> filteredMoviesStream = movies.stream()
                .filter(movie -> {
                    int year = movie.getReleaseYear();
                    return year >= startYear && year <= endYear;
                });

        // Store filtered movies in list
        List<Movie> moviesInRange = filteredMoviesStream
                .collect(Collectors.toList());

        return moviesInRange;
    }
//-------------------------------------   Info Services -------------------------------------------------------//
private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


//-------------------------------------   Watchlist Services -------------------------------------------------------//



    @FXML
    private void onWatchlistButtonClick(ActionEvent ev) throws IOException {
        Parent watchlistRoot = FXMLLoader.load(
                getClass().getResource("/at/ac/fhcampuswien/fhmdb/watchlist.fxml")
        );
        // Wir tauschen die gesamte Scene-Root aus
        watchlistButton.getScene().setRoot(watchlistRoot);
    }

    @FXML
    private void onHomeButtonClick(ActionEvent ev) throws IOException {
        Parent watchlistRoot = FXMLLoader.load(
                getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml")
        );
        homeButton.getScene().setRoot(watchlistRoot);
    }

/*
    private static final List<Movie> watchlist = new ArrayList<>();

    public static void addToWatchlist(Movie movie) {
        if (!watchlist.contains(movie)) {
            watchlist.add(movie);
        }
    }

    public static void removeFromWatchlist(Movie movie) {
        watchlist.remove(movie);
    }

    public static List<Movie> getWatchlist() {
        return new ArrayList<>(watchlist);
    }


    public void onWatchlistClicked(ActionEvent event) {
        sortBtn.setVisible(false);
        deleteBtn.setVisible(false);
        ratingComboBox.setVisible(false);
        genreComboBox.setVisible(false);
        searchField.setVisible(false);
        searchBtn.setVisible(false);
        releaseYearComboBox.setVisible(false);
    }

    public void onHomeClicked(ActionEvent event) {
        sortBtn.setVisible(true);
        deleteBtn.setVisible(true);
        ratingComboBox.setVisible(true);
        genreComboBox.setVisible(true);
        searchField.setVisible(true);
        searchBtn.setVisible(true);
        releaseYearComboBox.setVisible(true);

        observableMovies.setAll(allMovies);
    }
 */

}