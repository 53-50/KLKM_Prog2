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

    public List<MovieEntity> getAllMovies() throws DatabaseException {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new DatabaseException("Error while reading movies" + e);
        }
    }


    public void addAllMovies(List<Movie> movies) {
        // counter for the manually ID
        int count = 0;

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

            //DEBUGGING - still useful can be deleted when finished
            System.out.println(">>> INSERTING MOVIE <<<");
            System.out.println("Title: " + movie.getTitle());
            System.out.println("Description: " + movie.getDescription());
            System.out.println("Genres: " + movie.getGenres());
            System.out.println("ReleaseYear: " + movie.getReleaseYear());
            System.out.println("ImgUrl: " + movie.getImgUrl());
            System.out.println("Length: " + movie.getLengthInMinutes());
            System.out.println("Rating: " + movie.getRating());
            System.out.println("------------------------");

            try {

                // Set unique ID, because id = true
                long generatedId = count;
                entity.setPrimaryId(generatedId);

                dao.create(entity);  // create statt createOrUpdate, weil immer neu
                count++;

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
