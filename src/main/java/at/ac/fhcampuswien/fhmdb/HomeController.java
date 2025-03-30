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

    public List<Movie> allMovies;

    //was private final beforehand
    protected ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //observableMovies.addAll(allMovies); //-> adds dummy data to observable list -> in Exercise2 Data from API

        List<Movie> result = null;
        try {
            result = MovieAPI.fetchAllMovies();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setMovies(result);
        setMovieList(result);


        sortMoviesDescending();

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // genre filter text + data
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.Genre.values()); //add Genres to ComboBox

        //releaseYear filter text + data
        releaseYearComboBox.setPromptText("Filter by Release Year");

        //erstellt Array für Jahre von 1900 bis 2025
        Integer[] years = new Integer[126];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }

        //gibt die Jahre zur Auswahl in Dropdown
        releaseYearComboBox.getItems().addAll(years);

        // rating filter text + data
        ratingComboBox.setPromptText("Filter by Rating");

        //erstellt Array für Bewertungen von 0-10
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }

        //gibt die Bewertungen zur Auswahl in Dropdown
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


    public void fetchMoviesFromAPI() throws IOException{

        try {
            allMovies = MovieAPI.fetchAllMovies();

            // Filme ausgeben
            if (!allMovies.isEmpty()) {
                for (Movie movie : allMovies) {
                    System.out.println("Title: " + movie.getTitle());
                }
            } else {
                System.out.println("No movies found.");
            }

            observableMovies.setAll(allMovies);

        } catch (IOException e) {
            System.err.println("Error fetching movies: " + e.getMessage());
        }
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

        //System.out.println("Debugging - Gefilterte Filme: " + filteredMovies.stream().map(Movie::getTitle).collect(Collectors.joining(", ")));

        updateObservableList(filteredMovies); //-> wird ab Exercise 2 von API abgerufen

    }

    public void deleteFilter() {
        searchField.clear();
        genreComboBox.setValue(null);
        releaseYearComboBox.setValue(null);
        ratingComboBox.setValue(null);

        updateObservableList(allMovies);

    }


    private String getSearchQuery() {
        return (searchField != null && searchField.getText() != null) //stellt sicher, dass searchField und searchField.getText() nicht null sind, bevor .trim() aufgerufen wird.
                ? searchField.getText().trim().toLowerCase()          // -> notwenidg für unit test mit null
                : ""; //falls search field nicht existiert oder eingegeber Text leer ist, wird leerer String zurück gegeben
    }                 // ? : -> ternärer Operator -> Kurzform für if-else

    private Movie.Genre getSelectedGenre() {
        return genreComboBox.getValue();
    }

    private List<Movie> filterMovies(String query, Movie.Genre selectedGenre, Integer selectedYear, Integer selectedRating) {
        return allMovies.stream()
                .filter(movie -> query.isEmpty() || matchesQuery(movie, query))
                .filter(movie -> selectedGenre == null || movie.getGenres().contains(selectedGenre))
                .filter(movie -> selectedYear == null || movie.getReleaseYear() == selectedYear)
                .filter(movie -> selectedRating == null || movie.getRating() >= selectedRating)
                .collect(Collectors.toList());
    }

    private void updateObservableList(List<Movie> filteredMovies) {
        observableMovies.setAll(filteredMovies);
    }

    private boolean matchesQuery(Movie movie, String query) {
        return movie.getTitle().toLowerCase().contains(query) ||
                (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(query));
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

}