package at.ac.fhcampuswien.fhmdb.data;

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

    // get all movies that are in the watchlist
    public List<WatchlistMovieEntity> getWatchlist() throws SQLException {
        return dao.queryForAll();
    }

    // add movie to the watchlist
    public int addToWatchlist (WatchlistMovieEntity movie) {
        try {
            dao.create(movie);
            return 1;
        } catch (SQLException se) {
            System.err.println("Exception during adding one movie to the watchlist:" + se.getMessage());
            return 0;
        }
    }

    public int removeFromWatchlist (String apiId) {
        try {
            // the list is to check if there are any movies or more movies and just deletes ONE if there is one
            List<WatchlistMovieEntity> movies = dao.queryForEq("apiId", apiId);
            if (movies != null && !movies.isEmpty()) {
                WatchlistMovieEntity movieToRemove = movies.get(0);
                dao.delete(movieToRemove);

                List<WatchlistMovieEntity> checkRemoval = dao.queryForEq("apiId", apiId);
                if (checkRemoval.isEmpty()) {
                    return 1;
                }
            }
            return 0;
        } catch (SQLException e) {
            System.err.println("Exception removing movie from watchlist: " + e.getMessage());
            return 0;
        }
    }

}
