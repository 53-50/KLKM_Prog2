package at.ac.fhcampuswien.fhmdb.data;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException() {
    super();
  }

    public DatabaseException(Exception e) {
      super(e);
    }

}

