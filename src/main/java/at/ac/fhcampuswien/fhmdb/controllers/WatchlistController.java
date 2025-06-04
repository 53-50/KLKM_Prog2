package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.data.*;
import at.ac.fhcampuswien.fhmdb.ui.DialogWindow;
import at.ac.fhcampuswien.fhmdb.ui.WatchlistCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable {

    @FXML public JFXButton watchlistButton;
    @FXML private JFXButton homeButton;
    @FXML private JFXListView<MovieEntity> watchlistListView;

    private WatchlistRepository watchlistRepository;
    protected ObservableList<MovieEntity> observableWatchlist = FXCollections.observableArrayList();

    //Remove Entity Function
    private final ClickEventHandler<MovieEntity> RemoveFromWatchlistClicked = movieEntity -> {
        try {
            watchlistRepository.removeFromWatchlist(movieEntity.getApiId());
            observableWatchlist.remove(movieEntity);
        } catch (DatabaseException e) {
            new DialogWindow("Database Error", "Could not remove movie from watchlist").show();
            e.printStackTrace();
        }
    };


    //Initialize
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<WatchlistMovieEntity> watchlist = new ArrayList<>(); //initialize empty list

        try {
            watchlistRepository = WatchlistRepository.getInstance(); // initialize instance of repository
            watchlist = watchlistRepository.getWatchlist(); //read watchlist from DB

            MovieRepository movieRepository = MovieRepository.getInstance(); //new repo
            List<MovieEntity> movies = new ArrayList<>(); // list for movie-entities

            for(WatchlistMovieEntity movie : watchlist) { //for each movie-entity, load and add to list
                movies.add(movieRepository.getMovie(movie.getApiId()));
            }

            observableWatchlist.addAll(movies); //adds to observable
            watchlistListView.setItems(observableWatchlist); // observableList as data for listView
            watchlistListView.setCellFactory(movieListView -> new WatchlistCell(RemoveFromWatchlistClicked));
            //^ how cells in ListView will be rendered, incl. remove button

        } catch (DatabaseException e) { //catch exception
            DialogWindow dialog = new DialogWindow("Database Error", "Could not read movies from DB");
            dialog.show();
            e.printStackTrace();
        }

        if(watchlist.isEmpty()) { //if list is empty, show placeholder text
            watchlistListView.setPlaceholder(new javafx.scene.control.Label("Watchlist is empty"));
        }
    }

    @FXML
    public void onHomeButtonClick(ActionEvent ev) throws IOException {
        Parent homeRoot = FXMLLoader.load(
                getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml")
        );
        homeButton.getScene().setRoot(homeRoot);
    }

}