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
        List<WatchlistMovieEntity> watchlist = new ArrayList<>();
        try {
            watchlistRepository = new WatchlistRepository();
            watchlist = watchlistRepository.getWatchlist();

            MovieRepository movieRepository = new MovieRepository();
            List<MovieEntity> movies = new ArrayList<>();

            for(WatchlistMovieEntity movie : watchlist) {
                movies.add(movieRepository.getMovie(movie.getApiId()));
            }

            observableWatchlist.addAll(movies);
            watchlistListView.setItems(observableWatchlist);
            watchlistListView.setCellFactory(movieListView -> new WatchlistCell(RemoveFromWatchlistClicked));

        } catch (DatabaseException e) {
            DialogWindow dialog = new DialogWindow("Database Error", "Could not read movies from DB");
            dialog.show();
            e.printStackTrace();
        }

        if(watchlist.size() == 0) {
            watchlistListView.setPlaceholder(new javafx.scene.control.Label("Watchlist is empty"));
        }

        System.out.println("WatchlistController initialized");
    }

    @FXML
    public void onHomeButtonClick(ActionEvent ev) throws IOException {
        Parent homeRoot = FXMLLoader.load(
                getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml")
        );
        homeButton.getScene().setRoot(homeRoot);
    }

}