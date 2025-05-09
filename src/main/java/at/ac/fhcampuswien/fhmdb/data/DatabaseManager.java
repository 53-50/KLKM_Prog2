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
    // Data Access Object – provides methods to save, delete, find, ... Data
    private static Dao<MovieEntity, Long> movieDao;
    private static Dao<WatchlistMovieEntity, Long> watchlistDao;
    private static DatabaseManager instance;



    // create connection between this program and the database (movie.db)
    public static void createConnectionsSource() throws DatabaseException {
        try {
            conn = new JdbcConnectionSource(DB_URL, username, password);
        } catch (SQLException e) {
            throw new DatabaseException("Could not create database connection" + e);
        }
    }

    // getter for the connection
    public static ConnectionSource getConnectionSource() {
        return conn;
    }

    // create tables if not already existing
    public static void createTables() throws DatabaseException {
        try {
            TableUtils.createTableIfNotExists(conn, MovieEntity.class);
            TableUtils.createTableIfNotExists(conn, WatchlistMovieEntity.class);
        } catch (SQLException e) {
            throw new DatabaseException("Could not create database tables or tables already exist" + e);
        }
    }


    // DAO (Date Acces Object) - get DAO for movies to use SQL Operations
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


    // get singleton database instance
    public static DatabaseManager getInstance() throws DatabaseException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private DatabaseManager() throws DatabaseException {
        try {
            System.out.println("Creating database connection...");
            createConnectionsSource();
            System.out.println("Creating tables...");
            createTables();
            System.out.println("Database initialized successfully");
        } catch (DatabaseException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            throw new DatabaseException("DB init failed: " + e.getMessage());
        }
    }

    public static void closeConnectionSource() throws DatabaseException {
        if (conn != null) {
            System.out.println("Trying to close database connection...");

            try {
                conn.close();
                System.out.println("Database closed successfully");
            } catch (Exception e) {
                System.out.println("An error occurred while closing the connection.");
                e.printStackTrace();

                throw new DatabaseException("Failed to close database connection: " + e.getMessage());
            }
        } else {
            System.out.println("Database connection is already null. No need to close.");
        }
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
