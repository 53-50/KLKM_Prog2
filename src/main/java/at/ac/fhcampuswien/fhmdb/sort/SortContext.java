package at.ac.fhcampuswien.fhmdb.sort;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import java.util.List;

public class SortContext {
    private SortState currentState;

    public SortContext() {
        this.currentState = new UnsortedState(); // Initialzustand
    }

    public void setState(SortState state) {
        this.currentState = state;
    }

    public List<Movie> sort(List<Movie> movies) {
        return currentState.sort(movies);
    }

    public SortState getState() {
        return currentState;
    }
}
