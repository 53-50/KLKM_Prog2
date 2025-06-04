package at.ac.fhcampuswien.fhmdb.ObserverInterfaces;

public interface WatchlistObservable {
    void addObserver(WatchlistObserver observer);
    void removeObserver(WatchlistObserver observer);
    void notifyObserver(String message);
}
