package at.ac.fhcampuswien.fhmdb;

import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HomeController implements Initializable {

    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox<Movie.Genre> genreComboBox;

    @FXML
    public JFXComboBox<Integer> releaseYearComboBox;

    @FXML
    public JFXComboBox<Integer> ratingComboBox;

    @FXML
    public JFXButton sortBtn;

    @FXML
    public JFXButton deleteBtn;

    @FXML
    public JFXButton Watchlist;

    public List<Movie> allMovies;

    protected static ObservableList<Movie> observableMovies = FXCollections.observableArrayList();
    // automatically updates corresponding UI elements when underlying data changes

    private static ClickEventHandler<Movie> addWatchlistClickHandler;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<Movie> result;
        try {
            result = MovieAPI.fetchAllMovies();
        } catch (IOException e) {
            System.err.println("Error initializing movies " + e.getMessage());
            result = new ArrayList<>();
        }

        setMovies(result);
        setMovieList(result);

        sortMoviesDescending();

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell(HomeController.addWatchlistClickHandler)); // use custom cell factory to display data

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
//-------------------------------------   Watchlist Services -------------------------------------------------------//
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