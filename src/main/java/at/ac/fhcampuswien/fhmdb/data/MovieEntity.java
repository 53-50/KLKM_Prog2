package at.ac.fhcampuswien.fhmdb.data;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "movies")
public class MovieEntity {

    @DatabaseField(generatedId = true)
    long id;

    @DatabaseField
    String apiId;

    @DatabaseField
    String title;

    @DatabaseField
    String description;

    @DatabaseField
    String genres;

    @DatabaseField
    int releaseYear;

    @DatabaseField
    String imgUrl;

    @DatabaseField
    int lengthInMinutes;

    @DatabaseField
    double rating;

    public MovieEntity() {
        // ORMLite needs the empty constructor
    }

    public MovieEntity(String title, String description, List<Movie.Genre> genres, String apiId, int releaseYear, String imgUrl, int lengthInMinutes, double rating) {
        this.title = title;
        this.description = description;
        this.genres = genresToString(genres);
        this.apiId = apiId;
        this.releaseYear = releaseYear;
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }


    String genresToString(List<Movie.Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Movie.Genre genre : genres) {
            sb.append(genre.name()).append(", ");
        }
        return !sb.isEmpty() ? sb.substring(0, sb.length() - 2): "";
    }

    /*
    List<Movie.Genre> stringToGenres(String genres) {
        List<Movie.Genre> genreList = new ArrayList<>();

        if (genres != null && !genres.isEmpty()) {
            String[] genreArray = genres.split(", ");
            for (String genreStr : genreArray) {
                try {
                    genreList.add(Movie.Genre.valueOf(genreStr));
                } catch (IllegalArgumentException iae) {
                    System.err.println("Invalid genre: " + genreStr);
                }
            }
        }

        return genreList;
    }

     */

    List<Movie.Genre> stringToGenres(String genres) {
        if (genres == null || genres.isBlank()) return Collections.emptyList();

        return Arrays.stream(genres.split(",\\s*"))  // Komma + beliebig viele Leerzeichen
                .map(String::trim)             // ganz sicher alle Ränder abschneiden
                .map(Movie.Genre::valueOf)
                .collect(Collectors.toList());
    }

    public List<MovieEntity> fromMovies (List<Movie> movies) {
        List<MovieEntity> entities = new ArrayList<>();

        for (Movie movie : movies)  {
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
            entities.add(entity);
        }
        return entities;
    }

    public static List<Movie> toMovies(List<MovieEntity> movieEntities) {
        List<Movie> movies = new ArrayList<>();

        for (MovieEntity entity : movieEntities)  {
            Movie movie = new Movie(
                    entity.getApiId(),
                    entity.getTitle(),
                    entity.getDescription(),
                    entity.getGenres(),
                    entity.getReleaseYear(),
                    entity.getImgUrl(),
                    entity.getLengthInMinutes(),
                    entity.getRating()
            );
            movies.add(movie);
        }
        return movies;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
/*
    public String getGenres() {
        return genres;
    }

    //Getter und Setter für Genre
    public List<Movie.Genre> getGenres() {
        return Arrays.stream(genres.split(",")).map(Movie.Genre::valueOf).toList();
    }
*/
    public List<Movie.Genre> getGenres() {
        return stringToGenres(this.genres);
    }

    public void setGenres(List<Movie.Genre> genres) {
        this.genres = genresToString(genres);
    }

    public long getId() {
        return id;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getLengthInMinutes() {
        return lengthInMinutes;
    }

    public double getRating() {
        return rating;
    }

    public String getApiId() {
        return apiId;
    }
}
