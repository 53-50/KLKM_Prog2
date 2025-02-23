package at.ac.fhcampuswien.fhmdb.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Movie {
    private String title;
    private String description;
    private List<Genre> genres; //added
    // TODO add more properties here - DONE

    public enum Genre { //Enumeration List? Hab gegooglet - Mal schauen, ob man damit arbeiten kann :)
        ACTION, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY,
        CRIME, DRAMA, DOCUMENTARY, FAMILY, FANTASY, HISTORY, HORROR,
        MUSICAL, MYSTERY, ROMANCE, SCIENCE_FICTION, SPORT, THRILLER, WAR,
        WESTERN
    }

    public Movie(String title, String description, List<Genre> genres) {
        this.title = title;
        this.description = description;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genres.stream()
                .map(Genre::toString) // ruft toString() auf jedem Genre-Element auf
                .collect(Collectors.joining(", ")); // macht Beistrich dazwischen
    }

    public static List<Movie> initializeMovies(){
        List<Movie> movies = new ArrayList<>();
        // TODO add some dummy data here - DONE

        Movie ironMan = new Movie("Iron Man", "After being held captive in an Afghan cave," +
                "billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.",
                List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.ADVENTURE));

        Movie avengers = new Movie("The Avengers", "When an unexpected enemy emerges and " +
                "threatens global safety and security, Nick Fury, director of the international" +
                "peacekeeping agency known as S.H.I.E.L.D., finds himself in need of a team to pull" +
                "the world back from the brink of disaster. Spanning the globe, a daring recruitment effort" +
                "begins!",
                List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.ADVENTURE));

        Movie thor = new Movie("Thor", "Against his father Odin's will, The Mighty Thor " +
                "- a powerful but arrogant warrior god - recklessly reignites an ancient war. Thor is " +
                "cast down to Earth and forced to live among humans as punishment. Once here, Thor learns " +
                "what it takes to be a true hero when the most dangerous villain of his world sends the " +
                "darkest forces of Asgard to invade Earth.",
                List.of(Genre.ACTION, Genre.FANTASY, Genre.ADVENTURE));

        Movie schindler = new Movie("Schindler's List", "The true story of how businessman Oskar" +
                " Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves" +
                " in his factory during World War II.",
                List.of(Genre.DRAMA, Genre.HISTORY, Genre.WAR));

        Movie godfather2 = new Movie("The Godfather Part II", "In the continuing saga of the Corleone " +
                "crime family, a young Vito Corleone grows up in Sicily and in 1910s New York. In the 1950s, Michael " +
                "Corleone attempts to expand the family business into Las Vegas, Hollywood and Cuba.",
                List.of(Genre.DRAMA, Genre.CRIME));

        Movie godfather1 = new Movie("The Godfather", "Spanning the years 1945 to 1955, " +
                "a chronicle of the fictional Italian-American Corleone crime family. When organized crime" +
                " family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son," +
                " Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge.",
                List.of(Genre.DRAMA, Genre.CRIME));

        Movie antman = new Movie("Ant-Man", "Armed with the astonishing ability to shrink in scale" +
                " but increase in strength, master thief Scott Lang must embrace his inner-hero and help his mentor," +
                " Doctor Hank Pym, protect the secret behind his spectacular Ant-Man suit from a new generation of " +
                "towering threats. Against seemingly insurmountable obstacles, Pym and Lang must plan and pull off a " +
                "heist that will save the world.",
                List.of(Genre.ACTION, Genre.SCIENCE_FICTION, Genre.ADVENTURE));

        Movie hellokitty = new Movie("Hello Kitty's Animation Theater", "Recreations of different " +
                "fairy tales starring Hello Kitty and other Sanrio characters.",
                List.of(Genre.ANIMATION, Genre.SCIENCE_FICTION, Genre.FANTASY, Genre.FAMILY));

        Movie oppenheimer = new Movie("Oppenheimer", "The story of J. Robert Oppenheimer's role in " +
                "the development of the atomic bomb during World War II.",
                List.of(Genre.DRAMA, Genre.BIOGRAPHY, Genre.HISTORY));

        Movie insideOut = new Movie("Oppenheimer", "Teenager Riley's mind headquarters is undergoing a "
                +
                "sudden demolition to make room for something entirely unexpected: new Emotions! Joy, Sadness, " +
                "Anger, Fear and Disgust, who’ve long been running a successful operation by all accounts, aren’t" +
                " sure how to feel when Anxiety shows up. And it looks like she’s not alone.",
                List.of(Genre.FAMILY, Genre.COMEDY, Genre.ADVENTURE, Genre.ANIMATION));

        Movie chucky = new Movie("Living with Chucky", "A filmmaker who grew up alongside Chucky the " +
                "killer doll seeks out the other families surrounding the Child's Play films as they recount their " +
                "experiences working on the ongoing franchise and what it means to be a part of the, Chucky family.",
                List.of(Genre.DOCUMENTARY, Genre.HORROR));

        Movie mamma = new Movie("Mamma Mia!", "A spirited young bride-to-be living with her single " +
                "mother on a small Greek island secretly invites three of her mother's ex-boyfriends in hope of " +
                "finding her biological father to walk her down the aisle.",
                List.of(Genre.COMEDY, Genre.ROMANCE, Genre.MUSICAL));

        Movie maze = new Movie("The Maze Runner", "Set in a post-apocalyptic world, young Thomas is " +
                "deposited in a community of boys after his memory is erased, soon learning they're all trapped in a " +
                "maze that will require him to join forces with fellow “runners” for a shot at escape.",
                List.of(Genre.ACTION, Genre.MYSTERY, Genre.SCIENCE_FICTION, Genre.THRILLER));

        Movie brokeback = new Movie("Brokeback Mountain", "In 1960s Wyoming, two men develop a strong " +
                "emotional and sexual relationship that endures as a lifelong connection complicating their lives as" +
                " they get married and start families of their own.",
                List.of(Genre.DRAMA, Genre.ROMANCE, Genre.WESTERN));

        Movie rush = new Movie("rush", "In the 1970s, a rivalry propels race car drivers Niki Lauda " +
                "and James Hunt to fame and glory — until a horrible accident threatens to end it all.",
                List.of(Genre.DRAMA, Genre.ACTION, Genre.SPORT));

        movies.add(ironMan);
        movies.add(avengers);
        movies.add(thor);
        movies.add(schindler);
        movies.add(godfather1);
        movies.add(godfather2);
        movies.add(antman);
        movies.add(hellokitty);
        movies.add(oppenheimer);
        movies.add(insideOut);
        movies.add(chucky);
        movies.add(mamma);
        movies.add(maze);
        movies.add(brokeback);
        movies.add(rush);

        return movies;
    }
}
