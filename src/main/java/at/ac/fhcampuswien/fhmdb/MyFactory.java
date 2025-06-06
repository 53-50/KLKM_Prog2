package at.ac.fhcampuswien.fhmdb;

// usually FXMLLoader generates the controller on it's own (every time)
// Now -> no I want to decide myself how and when the Controller gets generated.
// Now we can also: manage singleton-instance and add dependencies

import at.ac.fhcampuswien.fhmdb.controllers.WatchlistController;
import javafx.util.Callback;

// implements the JavaFX-Interface Callback<Class<?>, Object>
// makes it possible to manage Controller instances (Factory Pattern)
public class MyFactory implements Callback<Class<?>, Object> {

    // save the singleton-instance - makes sure their exists only one instance
    private static HomeController homeControllerInstance;
    private static WatchlistController watchlistControllerInstance;

    // gets called by FXMLLoader when he wants to generate a Controller
    @Override
    public Object call(Class<?> aClass) {
        try {
            // check: is the class the HomeController Class?
            if (aClass == HomeController.class) {
                if (homeControllerInstance == null) {
                    homeControllerInstance = new HomeController();
                }
                return homeControllerInstance;
            }

            if (aClass == WatchlistController.class) {
                if (watchlistControllerInstance == null) {
                    watchlistControllerInstance = new WatchlistController();
                }
                return watchlistControllerInstance;
            }

            // aClass is the class we want to instantiate
            // getDeclaredConstructor() gets the default constructor
            // generic way of writing for example: return new WatchlistController();
            return aClass.getDeclaredConstructor().newInstance();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Controller couldn't be generated: " + aClass.getName(), e);
        }
    }
}