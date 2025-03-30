package at.ac.fhcampuswien.fhmdb;

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
    public JFXButton sortBtn;

    @FXML
    public JFXButton deleteBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    //was private final beforehand
    public ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list
        sortMoviesDescending();

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // TODO add genre filter items with genreComboBox.getItems().addAll(...) - Done
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.Genre.values()); //add Genres to ComboBox

        // TODO add event handlers to buttons and call the regarding methods - Done
        // either set event handlers in the fxml file (onAction) or add them here

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort (asc)")) {
                // TODO sort observableMovies ascending - Done
                sortMoviesAscending();
                sortBtn.setText("Sort (desc)");
            } else {
                // TODO sort observableMovies descending - Done
                sortMoviesDescending();
                sortBtn.setText("Sort (asc)");
            }
        });

        // Event handler search button and genreBox dropdown using apply filter methode
        searchBtn.setOnAction(actionEvent -> applyFilter());
        genreComboBox.setOnAction(actionEvent -> applyFilter());
        deleteBtn.setOnAction(actionEvent -> deleteFilter());
    }

    //methode to apply a filter based on search request and/or chosen genre
    public void applyFilter() {
        String query = getSearchQuery();
        Movie.Genre selectedGenre = getSelectedGenre();
        List<Movie> filteredMovies = filterMovies(query, selectedGenre);

        //Debugging
        //System.out.println("Debugging - Gefilterte Filme: " + filteredMovies.stream().map(Movie::getTitle).collect(Collectors.joining(", ")));

        updateObservableList(filteredMovies);
    }


    public void deleteFilter() {
        searchField.clear();
        genreComboBox.setValue(null);

        updateObservableList(allMovies);
    }


    private String getSearchQuery() {
        return (searchField != null && searchField.getText() != null) //stellt sicher, dass searchField und searchField.getText() nicht null sind, bevor .trim() aufgerufen wird.
                ? searchField.getText().trim().toLowerCase()          // -> notwenidg für unit test mit null
                : ""; //falls search field nicht existiert oder eingegeber Text leer ist, wird leerer String zurück gegeben
    } // ? : -> ternärer Operator -> Kurzform für if-else


    private Movie.Genre getSelectedGenre() {
        return genreComboBox.getValue();
    }


    private List<Movie> filterMovies(String query, Movie.Genre selectedGenre) {
        return allMovies.stream()
                .filter(movie -> query.isEmpty() || matchesQuery(movie, query))
                .filter(movie -> selectedGenre == null || movie.getGenre().contains(selectedGenre))
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