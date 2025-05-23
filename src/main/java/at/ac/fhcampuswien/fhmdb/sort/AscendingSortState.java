package at.ac.fhcampuswien.fhmdb.sort;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AscendingSortState implements SortState {
    @Override
    public List<Movie> sort(List<Movie> movies) {
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .collect(Collectors.toList());
    }
}
