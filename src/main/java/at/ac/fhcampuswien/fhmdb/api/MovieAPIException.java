package at.ac.fhcampuswien.fhmdb.api;

public class MovieAPIException extends Exception {
    public MovieAPIException() {
        super();
    }

    public MovieAPIException(String message) {
        super(message);
    }
}
