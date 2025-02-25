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
    public JFXListView movieListView;

    @FXML
    public JFXComboBox<Movie.Genre> genreComboBox;

    @FXML
    public JFXButton sortBtn;

    public List<Movie> allMovies = Movie.initializeMovies();

    //was private final beforehand
    public ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        observableMovies.addAll(allMovies);         // add dummy data to observable list

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        // TODO add genre filter items with genreComboBox.getItems().addAll(...)
        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(Movie.Genre.values()); //add Genres to ComboBox

        // TODO add event handlers to buttons and call the regarding methods
        // either set event handlers in the fxml file (onAction) or add them here
        /*
        EventHandlers for EveryButton
        fx:id="sortBtn"
        fx:id="searchField"
        fx:id="genreComboBox"
        fx:id="searchBtn"
         */

        // Sort button example:
        sortBtn.setOnAction(actionEvent -> {
            if(sortBtn.getText().equals("Sort (asc)")) {
                // TODO sort observableMovies ascending
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
    }

    //methode to apply a filter based on search request and/or chosen genre
    public void applyFilter() {
        String query = searchField.getText().toLowerCase(); //gets text from search request + transforms into lower cases (-> case insensitive)
        Movie.Genre selectedGenre = genreComboBox.getValue(); //saves chosen genre

        List<Movie> filteredMovies = allMovies.stream() //creates stream of movie list
                //search request:
                .filter(movie -> (query.isEmpty() || //search request is empty -> no search-filter

                        //checks filmtitle and description -> filter by query
                        movie.getTitle().toLowerCase().contains(query) ||
                        //movie.getDescription().toLowerCase().contains(query)))
                        (movie.getDescription() != null && movie.getDescription().toLowerCase().contains(query))))

                //chosen genre:
                .filter(movie -> (selectedGenre == null || //no selected genre -> no genre-filter
                        movie.getGenre().contains(selectedGenre))) //checks if film contains genre

                .collect(Collectors.toList()); //transforms filtered stream back into List

        observableMovies.setAll(filteredMovies); //
    }

    public void sortMoviesAscending(){
        FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle));
    }

    public void sortMoviesDescending() {
        FXCollections.sort(observableMovies, Comparator.comparing(Movie::getTitle).reversed());
    }

}