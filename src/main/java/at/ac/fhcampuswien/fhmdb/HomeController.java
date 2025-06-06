package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.ObserverInterfaces.WatchlistObserver;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.api.MovieAPIException;
import at.ac.fhcampuswien.fhmdb.data.DatabaseException;
import at.ac.fhcampuswien.fhmdb.data.MovieEntity;
import at.ac.fhcampuswien.fhmdb.data.MovieRepository;
import at.ac.fhcampuswien.fhmdb.data.WatchlistRepository;
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
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.ac.fhcampuswien.fhmdb.sort.SortContext;
import at.ac.fhcampuswien.fhmdb.sort.UnsortedState;
import at.ac.fhcampuswien.fhmdb.sort.AscendingSortState;
import at.ac.fhcampuswien.fhmdb.sort.DescendingSortState;


public class HomeController implements Initializable, WatchlistObserver {

    @FXML public JFXButton searchBtn;
    @FXML public TextField searchField;
    @FXML public JFXComboBox<Movie.Genre> genreComboBox;
    @FXML public JFXComboBox<Integer> releaseYearComboBox;
    @FXML public JFXComboBox<Integer> ratingComboBox;
    @FXML public JFXButton sortBtn;
    @FXML public JFXButton deleteBtn;

    //watchlist + homelist buttons
    @FXML private JFXButton homeButton;
    @FXML private JFXListView<Movie> movieListView;
    @FXML private JFXButton watchlistButton;

    //gets all Movies without filter
    public List<Movie> allMovies;

    // automatically updates corresponding UI elements when underlying data changes
    protected static ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    //connections to database-sources
    private MovieRepository movieRepository;
    private WatchlistRepository watchlistRepository;

    private SortContext sortContext = new SortContext();

    // ganz oben im Controller:
    private WatchlistRepository watchlistRepo;

    public static int instanceCount = 0;


    //initializes repos, loads movies, stages UI
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        watchlistRepo = WatchlistRepository.getInstance(); //repo for watchlist -> loadMovies -> now singleton instance of repository
        watchlistRepo.addObserver(this);

        try {
            //DEBUG
            System.out.println("Initializing HomeController...");

            // Repository Initialization - one for movies, one for watchlist
            movieRepository = MovieRepository.getInstance(); //now singleton instance of repository
            watchlistRepository = WatchlistRepository.getInstance(); //now singleton instance of repository

            //DEBUG
            System.out.println("Repositories initialized");

            // Load movies - from API and DB and watchlistHanlder
            loadMovies();

            // UI Setup - ListView, Buttons, Placeholders
            setupUI();

            //DEBUG
            System.out.println("Initialization complete");

        } catch (MovieAPIException | DatabaseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HomeController() {
        //empty HomeController Constructor for the MyFactory() Class

        //DEBUGGING - if it appears once it's correct
        System.out.println("HomeController wurde erzeugt");
        instanceCount++;
        System.out.println("HomeController Instance: " + instanceCount);

    }

    private void loadMovies() throws MovieAPIException, SQLException {
        //DEBUG
        System.out.println("Loading movies...");

        //getting movies from API
        List<Movie> apiMovies = MovieAPI.fetchAllMovies();
        System.out.println("Fetched " + apiMovies.size() + " movies from API");

        //if new movies, save them to DB
        if (!apiMovies.isEmpty()) {
            movieRepository.addAllMovies(apiMovies);
            System.out.println("Movies saved to database");
        }

        //loading movies from database
        List<MovieEntity> entities = movieRepository.getAllMovies();
        System.out.println("Loaded " + entities.size() + " movies from database");

        /*Adding to watchlist when watchlist-click ----------------> ersetzt durch Observer pattern anbei
        ClickEventHandler<Movie> addToWatchlist = m -> { //anonymous class using ClickHandler
            try {
                int count = watchlistRepo.addToWatchlist(m);
                //if count == 1 movie is new, else movie is already in watchlist
                String msg = count == 1
                        ? m.getTitle() + " was added to watchlist." //if count == 1
                        : m.getTitle() + " is already in watchlist."; // else
                new DialogWindow("Watchlist", msg).show();
            } catch (DatabaseException e) {
                new DialogWindow("Database error",
                        "Couldn't be added to watchlist.").show();
                e.printStackTrace();
            }
        };*/

        ClickEventHandler<Movie> addToWatchlist = m -> {
            try {
                watchlistRepo.addToWatchlist(m);  // only calls repository
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        };

        //ListView for custom MovieCell
        movieListView.setCellFactory(lv -> new MovieCell(addToWatchlist));

        //convert entities in domaine-objects and put them into a list
        List<Movie> movies = MovieEntity.toMovies(entities);
        //internal list for logic
        setMovies(movies);
        //List which is seen in  UI
        setMovieList(movies);

        //sort movies descending
        //sortMoviesDescending();

        sortContext.setState(new UnsortedState());
        List<Movie> sorted = sortContext.sort(movies);
        setMovieList(sorted);

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
            /*if(sortBtn.getText().equals("Sort (asc)")) {

                // sort observableMovies ascending
                sortMoviesAscending();
                sortBtn.setText("Sort (desc)");
            } else {
                // sort observableMovies descending
                sortMoviesDescending();
                sortBtn.setText("Sort (asc)");
            }*/
            if (sortContext.getState() instanceof UnsortedState || sortContext.getState() instanceof DescendingSortState) {
                sortContext.setState(new AscendingSortState());
                sortBtn.setText("Sort (desc)");
            } else {
                sortContext.setState(new DescendingSortState());
                sortBtn.setText("Sort (asc)");
            }
            updateObservableList(new ArrayList<>(observableMovies));
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


//-------------------------------------   Watchlist Services -------------------------------------------------------//

    @FXML
    private void onWatchlistButtonClick(ActionEvent ev) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/fhcampuswien/fhmdb/watchlist.fxml")
        );
        loader.setControllerFactory(new MyFactory());
        Parent watchlistRoot = loader.load();

        watchlistButton.getScene().setRoot(watchlistRoot);
    }

    @FXML
    private void onHomeButtonClick(ActionEvent ev) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml")
        );
        loader.setControllerFactory(new MyFactory());
        Parent homeRoot = loader.load();

        homeButton.getScene().setRoot(homeRoot);
    }


    //-------------------------------------   Other Services -------------------------------------------------------//

    //set movies
    public void setMovies(List<Movie> movies) {
        allMovies = movies;
    }

    //add all movies to Movie-list
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

        //update UI-list with filtered movies
        updateObservableList(filteredMovies); //-> API relevant (exercise 2)

    }

    //resetting filter
    public void deleteFilter() {
        searchField.clear();
        genreComboBox.setValue(null);
        releaseYearComboBox.setValue(null);
        ratingComboBox.setValue(null);
        updateObservableList(allMovies);
    }

    //get search query for query filter
    private String getSearchQuery() {
        return (searchField != null && searchField.getText() != null) //makes sure that searchField and searchField.getText() are not null before .trim() is called.
                ? searchField.getText().trim().toLowerCase()          // -> necessary for unit test with null
                : ""; //if search field does not exist or entered text is empty, empty string is returned
    }                 // ? : -> ternary operator -> short form for if-else

    //get value from genre box filter
    private Movie.Genre getSelectedGenre() {
        return genreComboBox.getValue();
    }

    //filters movies by all filter choices if selected
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

    //updating UI list
    private void updateObservableList(List<Movie> filteredMovies) {
        List<Movie> sorted = sortContext.sort(filteredMovies);
        observableMovies.setAll(sorted);
        //observableMovies.setAll(filteredMovies);
    }

    //method to sort movies - ascending
    /*public void sortMoviesAscending(){
        FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle));
        allMovies.sort(Comparator.comparing(Movie::getTitle));
    }*/

    //method to sort movies - descending
    /*public void sortMoviesDescending() {
        FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle).reversed());
        allMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
    }*/

    //method to get most popular actor
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

    //method to get longest movie title
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

    //method to get movies from same director
    public long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    //method to get movies between years
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
//------------------------------------Observer Pattern ------------------------------------------------//

    @Override
    public void update(String message) {
    // UI-Aktualisierungen immer über Platform.runLater!
    javafx.application.Platform.runLater(() -> {
        new DialogWindow("Watchlist Update", message).show();
    });
}

}