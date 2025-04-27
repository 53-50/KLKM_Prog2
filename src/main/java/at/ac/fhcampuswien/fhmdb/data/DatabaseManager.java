package at.ac.fhcampuswien.fhmdb.data;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseManager {

    // jdbc is the way how java connects to databases
    // sqlite specifies that we are using SQLite as database system
    // movies.db is the name of the database file
    // our program connects through JDBC to a local SQLite database file
    private static final String DB_URL = "jdbc:sqlite:movies.db";
    private static final String username = "";
    private static final String password = "";
    // ConnectionSource connects the program with the database
    private static ConnectionSource conn;
    private static Dao<MovieEntity, Long> movieDao;
    private static Dao<WatchlistMovieEntity, Long> watchlistDao;

    // create connection between this programm and the database (movie.db)
    public static void createConnectionsSource() throws SQLException {
        conn = new JdbcConnectionSource(DB_URL, username, password);
    }

    // getter for the connection
    public static ConnectionSource getConnectionSource() {
        return conn;
    }

    // create tables if not already existing
    public static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(conn, MovieEntity.class);
        TableUtils.createTableIfNotExists(conn, WatchlistMovieEntity.class);
    }

    // DAO (Date Acces Object) - get DAO for movies
    public static Dao<WatchlistMovieEntity, Long> getWatchlistDao() throws SQLException {
        if (watchlistDao == null) {
            watchlistDao = DaoManager.createDao(conn, WatchlistMovieEntity.class);
        }
        return watchlistDao;
    }

    // get DAO for watchlist
    public static Dao<MovieEntity, Long> getMovieDao() throws SQLException {
        if (movieDao == null) {
            movieDao = DaoManager.createDao(conn, MovieEntity.class);
        }
        return movieDao;
    }


    // for testing purpose
//    public static void main(String[] args) {
//        try {
//            DatabaseManager.createConnectionsSource();
//            DatabaseManager.createTables();
//            System.out.println("Datenbank und Tabellen erfolgreich erstellt!");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

}
