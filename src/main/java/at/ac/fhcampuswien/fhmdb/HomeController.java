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
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

        // TODO add genre filter items with genreComboBox.getItems().addAll(...)
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.Genre.values()); //add Genres to ComboBox

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort (asc)")) {
                // TODO sort observableMovies ascending - Done
                sortMoviesAscending();
                sortBtn.setText("Sort (desc)");
            } else {
                // TODO sort observableMovies descending - DONE
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

        System.out.println("Gefilterte Filme: " + filteredMovies.stream().map(Movie::getTitle).collect(Collectors.joining(", ")));

        updateObservableList(filteredMovies);
    }

    public void deleteFilter() {
        searchField.clear();
        genreComboBox.setValue(null);

        updateObservableList(allMovies);
    }

    /*private String getSearchQuery() {
        return searchField.getText().trim().toLowerCase();
    }*/

    private String getSearchQuery() {
        return (searchField != null && searchField.getText() != null) //stellt sicher, dass searchField und searchField.getText() nicht null sind, bevor .trim() aufgerufen wird.
                ? searchField.getText().trim().toLowerCase()          // -> notwenidg für unit test mit null
                : "";
    }

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

}