package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.ObserverInterfaces.WatchlistObservable;
import at.ac.fhcampuswien.fhmdb.ObserverInterfaces.WatchlistObserver;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WatchlistRepository implements WatchlistObservable {

    //Singleton-instance
    private static WatchlistRepository instance = null;

    //dao = object, that allows access to database table -> WME = type of saved object; Long = type of primary key
    private final Dao<WatchlistMovieEntity, Long> dao;
    private final List<WatchlistObserver> observers = new ArrayList<>();

   //Constructor to get dao-instance -> made private to prevent external instancing
    private WatchlistRepository() throws DatabaseException {
        try {
            this.dao = DatabaseManager.getInstance().getWatchlistDao();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public static synchronized WatchlistRepository getInstance(){
        if (instance==null){
            instance = new WatchlistRepository();
        }
        return instance;
    }

    //reads all movies from watchlist database
    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Error reading watchlist: " + e.getMessage());
        }
    }

    @Override
    public void addObserver(WatchlistObserver observer){
        if(!observers.contains(observer)){
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(WatchlistObserver observer){
        observers.remove(observer);
    }

    public void notifyObserver(String message){
        for (WatchlistObserver o : observers){
            o.update(message);
        }
    }


    // add movie to the watchlist - used when working with UI or domain model Movie
    public int addToWatchlist(WatchlistMovieEntity movie) throws DatabaseException {
        try {
            // Check if movie already exists by API ID
            List<WatchlistMovieEntity> existing = dao.queryForEq("apiId", movie.getApiId());
            if (!existing.isEmpty()) {
                notifyObserver("Film is already in watchlist.");
                return 0; //already exists - no action
            }
            dao.create(movie);
            notifyObserver("Film was added to watchlist.");
            return 1; //successfully added
        } catch (SQLException se) {
            throw new DatabaseException("Error adding to watchlist: " + se.getMessage());
        }
    }

    // Used when already working with database entity
    public int addToWatchlist(Movie movie) throws DatabaseException {
        //Checks if movie ID is valid
        if (movie == null || movie.getId() == null) {
            throw new DatabaseException("Invalid movie or missing ID");
        }
        // Movie.getId() gives back API-ID as String
        return addToWatchlist(new WatchlistMovieEntity(movie.getId()));
    }

    public int removeFromWatchlist(String apiId) throws DatabaseException {
        try {
            List<WatchlistMovieEntity> movies = dao.queryForEq("apiId", apiId);
            if (!movies.isEmpty()) {
                dao.delete(movies);
                return movies.size(); //number of deleted movies
            }
            return 0; //no movie with ID
        } catch (SQLException e) {
            throw new DatabaseException("Error removing from watchlist: " + e.getMessage());
        }
    }
}
