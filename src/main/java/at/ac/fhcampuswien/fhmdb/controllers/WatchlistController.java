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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WatchlistController implements Initializable {

    @FXML public JFXButton watchlistButton;
    @FXML private JFXButton homeButton;
    @FXML private JFXListView<MovieEntity> watchlistListView;

    private WatchlistRepository watchlistRepository;
    private MovieRepository movieRepository;
    protected ObservableList<MovieEntity> observableWatchlist = FXCollections.observableArrayList();



    private final ClickEventHandler RemoveFromWatchlistClicked = (o) -> {
        if (o instanceof MovieEntity) {
            MovieEntity movieEntity = (MovieEntity) o;

            try {
                WatchlistRepository watchlistRepository = new WatchlistRepository();
                watchlistRepository.removeFromWatchlist(movieEntity.getApiId());
                observableWatchlist.remove(movieEntity);
            } catch (DatabaseException e) {
                DialogWindow dialog = new DialogWindow("Database Error", "Could not remove movie from watchlist");
                dialog.show();
                e.printStackTrace();
            }
        }
    };

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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(watchlist.size() == 0) {
            watchlistListView.setPlaceholder(new javafx.scene.control.Label("Watchlist is empty"));
        }

        System.out.println("WatchlistController initialized");
    }

/*
    private void loadWatchlist() {
        try {
            List<WatchlistMovieEntity> entries = watchlistRepository.getWatchlist();
            // 1) ObservableList mit den gleichen Typen
            ObservableList<WatchlistMovieEntity> items =
                    FXCollections.observableArrayList(entries);
            // 2) Items setzen
            watchlistListView.setItems(items);
            // 3) CellFactory liefert ListCell<WatchlistMovieEntity>
            watchlistListView.setCellFactory(
                    lv -> new WatchlistCell(removeFromWatchlistClick)
            );
        } catch (SQLException e) {
            e.getMessage();
        }
    }

 */

    @FXML
    public void onHomeButtonClick(ActionEvent ev) throws IOException {
        Parent homeRoot = FXMLLoader.load(
                getClass().getResource("/at/ac/fhcampuswien/fhmdb/home-view.fxml")
        );
        homeButton.getScene().setRoot(homeRoot);
    }

    /*
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
            watchlistView.setItems(observableWatchlist);
            watchlistView.setCellFactory(movieListView -> new WatchlistCell(RemoveFromWatchlistClicked));

        } catch (DatabaseException | SQLException e) {
            DialogWindow dialog = new DialogWindow("Database Error", "Could not read movies from DB");
            dialog.show();
            e.printStackTrace();
        }

        if(watchlist.size() == 0) {
            watchlistView.setPlaceholder(new javafx.scene.control.Label("Watchlist is empty"));
        }

        System.out.println("WatchlistController initialized");
    }

     */
}