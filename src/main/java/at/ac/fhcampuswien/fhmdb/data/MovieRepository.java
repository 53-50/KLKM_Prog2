package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class MovieRepository {

    // DAO - Data Access Object
    private Dao<MovieEntity, Long> dao;

    public MovieRepository() throws DatabaseException {
        try {
            this.dao = DatabaseManager.getInstance().getMovieDao();
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public List<MovieEntity> getAllMovies() throws SQLException {
        return dao.queryForAll();
    }

    public void addAllMovies(List<Movie> movies) {
        try {
            dao.deleteBuilder().delete();
        } catch (SQLException e) {
            System.err.println("Could not clear table: " + e.getMessage());
        }
        for (Movie movie : movies) {
            MovieEntity entity = new MovieEntity(
                    movie.getTitle(),
                    movie.getDescription(),
                    movie.getGenres(),
                    movie.getId(),
                    movie.getReleaseYear(),
                    movie.getImgUrl(),
                    movie.getLengthInMinutes(),
                    movie.getRating()
            );
            try {
                dao.create(entity);  // create statt createOrUpdate, weil immer neu
            } catch (SQLException se) {
                System.err.println("Failed to insert " + movie.getTitle() + ":");
                se.printStackTrace();  // ganze Ursache sehen!
            }
        }
    }

    // removes all movies and gives back the number of deleted movies
    public int removeAll() {
        try {
            return dao.deleteBuilder().delete();
        } catch (SQLException se) {
            System.err.println("Exception during removing all movies:" + se.getMessage());
            return 0;
        }
    }

    // should it just get the first movie? without input how should it work?
    public MovieEntity getMovie() {
        try {
            List<MovieEntity> movies = dao.queryForAll();
            if (!movies.isEmpty()) {
                return movies.get(0);
            }
            return null;
        } catch (SQLException se) {
            System.err.println("Exception during getting one movie:" + se.getMessage());
            return null;
        }
    }

    // possibility if you want to get by specific id:
    public MovieEntity getMovie(String apiId) {
        try {
            List<MovieEntity> movies = dao.queryForEq("apiId", apiId);
            if (!movies.isEmpty()) {
                return movies.get(0);
            }
            return null;
        } catch (SQLException se) {
            System.err.println("Exception during getting movie by API ID:" + se.getMessage());
            return null;
        }
    }

}
