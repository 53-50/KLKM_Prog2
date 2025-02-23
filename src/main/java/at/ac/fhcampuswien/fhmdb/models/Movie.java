package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String title;
    private String description;
    private List<Genre> genres; //added
    // TODO add more properties here

    public enum Genre { //Enumeration List? Hab gegooglet - Mal schauen, ob man damit arbeiten kann :)
        ACTION, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY,
        CRIME, DRAMA, DOCUMENTARY, FAMILY, FANTASY, HISTORY, HORROR,
        MUSICAL, MYSTERY, ROMANCE, SCIENCE_FICTION, SPORT, THRILLER, WAR,
        WESTERN
    }

    public Movie(String title, String description) {
        this.title = title;
        this.description = description;
        }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        // TODO add some dummy data here

        Movie ironMan = new Movie("Iron Man", "After being held captive in an Afghan cave," +
                "billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.");

        Movie avengers = new Movie("The Avengers", "When an unexpected enemy emerges and " +
                "threatens global safety and security, Nick Fury, director of the international" +
                "peacekeeping agency known as S.H.I.E.L.D., finds himself in need of a team to pull" +
                "the world back from the brink of disaster. Spanning the globe, a daring recruitment effort" +
                "begins!");

        Movie thor = new Movie("Thor", "Against his father Odin's will, The Mighty Thor " +
                "- a powerful but arrogant warrior god - recklessly reignites an ancient war. Thor is " +
                "cast down to Earth and forced to live among humans as punishment. Once here, Thor learns " +
                "what it takes to be a true hero when the most dangerous villain of his world sends the " +
                "darkest forces of Asgard to invade Earth.");

        Movie schindler = new Movie("Schindler's List", "The true story of how businessman Oskar" +
                " Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves" +
                " in his factory during World War II.");

        Movie godfather2 = new Movie("The Godfather Part II", "In the continuing saga of the Corleone " +
                "crime family, a young Vito Corleone grows up in Sicily and in 1910s New York. In the 1950s, Michael " +
                "Corleone attempts to expand the family business into Las Vegas, Hollywood and Cuba.");

        Movie godfather1 = new Movie("The Godfather", "Spanning the years 1945 to 1955, " +
                "a chronicle of the fictional Italian-American Corleone crime family. When organized crime" +
                " family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son," +
                " Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge.");

        Movie antman = new Movie("Ant-Man", "Armed with the astonishing ability to shrink in scale" +
                " but increase in strength, master thief Scott Lang must embrace his inner-hero and help his mentor," +
                " Doctor Hank Pym, protect the secret behind his spectacular Ant-Man suit from a new generation of " +
                "towering threats. Against seemingly insurmountable obstacles, Pym and Lang must plan and pull off a " +
                "heist that will save the world.");

        movies.add(ironMan);
        movies.add(avengers);
        movies.add(thor);
        movies.add(schindler);
        movies.add(godfather1);
        movies.add(godfather2);
        movies.add(antman);

        return movies;
    }
}
