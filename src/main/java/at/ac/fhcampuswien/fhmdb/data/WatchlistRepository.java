package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class WatchlistRepository {

    Dao<WatchlistMovieEntity, Long> dao;

    public WatchlistRepository() throws DatabaseException {
        try {
            this.dao = DatabaseManager.getInstance().getWatchlistDao();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<WatchlistMovieEntity> getWatchlist() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Error reading watchlist: " + e.getMessage());
        }
    }


    // add movie to the watchlist - used when working with UI or domain model Movie
    public int addToWatchlist(WatchlistMovieEntity movie) throws DatabaseException {
        try {
            // Check if movie already exists
            List<WatchlistMovieEntity> existing = dao.queryForEq("apiId", movie.getApiId());
            if (!existing.isEmpty()) {
                return 0; // already exists
            }
            dao.create(movie);
            return 1;
        } catch (SQLException se) {
            throw new DatabaseException("Error adding to watchlist: " + se.getMessage());
        }
    }

    // Used when already working with database entity
    public int addToWatchlist(Movie movie) throws DatabaseException {
        if (movie == null || movie.getId() == null) {
            throw new DatabaseException("Invalid movie or missing ID");
        }
        // Movie.getId() liefert die API-ID als String
        return addToWatchlist(new WatchlistMovieEntity(movie.getId()));
    }

    public int removeFromWatchlist(String apiId) throws DatabaseException {
        try {
            List<WatchlistMovieEntity> movies = dao.queryForEq("apiId", apiId);
            if (!movies.isEmpty()) {
                dao.delete(movies);
                return movies.size();
            }
            return 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error removing from watchlist: " + e.getMessage());
        }
    }
}
