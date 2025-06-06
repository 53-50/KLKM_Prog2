package at.ac.fhcampuswien.fhmdb.ui;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.data.MovieEntity;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

public class WatchlistCell extends ListCell<MovieEntity> {

    private final Label title = new Label();
    private final Label description = new Label();
    private final Label genre = new Label();

    private final JFXButton detailBtn = new JFXButton("Show Details");
    private final JFXButton removeBtn = new JFXButton("Remove");

    private final HBox header = new HBox(title, detailBtn, removeBtn); //horizontal container for title and buttons
    private final VBox layout = new VBox(header, description, genre);  //vertical container for movie cell-view

    private boolean collapsedDetails = true; //hides details view

    public WatchlistCell(ClickEventHandler<MovieEntity> removeFromWatchlistClick) {

        super();

        // color scheme
        detailBtn.setStyle("-fx-background-color: #f5c518;");
        removeBtn.setStyle("-fx-background-color: #f5c518;");

        HBox.setMargin(detailBtn, new Insets(0, 10, 0, 10));

        title.getStyleClass().add("text-yellow");
        description.getStyleClass().add("text-white");
        genre.getStyleClass().add("text-white");

        genre.setStyle("-fx-font-style: italic");

        layout.setBackground(new Background(new BackgroundFill(Color.web("#454545"), null, null)));

        header.setAlignment(Pos.CENTER_LEFT);

        title.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(title, Priority.ALWAYS);

        // layout
        title.fontProperty().set(title.getFont().font(20));
        description.setWrapText(true);
        layout.setPadding(new Insets(10));

        detailBtn.setOnMouseClicked(mouseEvent -> {
            if (collapsedDetails) {
                layout.getChildren().add(getDetails());
                collapsedDetails = false;
                detailBtn.setText("Hide Details");
            } else {
                layout.getChildren().remove(3);
                collapsedDetails = true;
                detailBtn.setText("Show Details");
            }
            setGraphic(layout);
        });

        removeBtn.setOnMouseClicked(mouseEvent -> {
            removeFromWatchlistClick.onClick(getItem());
        });
    }

    //builds detail box
    private VBox getDetails() {
        VBox details = new VBox();

        Label releaseYear = new Label("Release Year: " + getItem().getReleaseYear());
        Label length = new Label("Length: " + getItem().getLengthInMinutes() + " minutes");
        Label rating = new Label("Rating: " + getItem().getRating());

        releaseYear.getStyleClass().add("text-white");
        length.getStyleClass().add("text-white");
        rating.getStyleClass().add("text-white");

        details.getChildren().add(releaseYear);
        details.getChildren().add(rating);
        details.getChildren().add(length);

        return details;
    }

    protected void updateItem(MovieEntity movieEntity, boolean empty) {
        super.updateItem(movieEntity, empty);

        if (empty || movieEntity == null) {
            setGraphic(null);
            setText(null);
        } else {
            this.getStyleClass().add("movie-cell");

            title.setText(movieEntity.getTitle());
            description.setText(
                    movieEntity.getDescription() != null
                            ? movieEntity.getDescription() //if desc != null -> getDescription
                            : "No description available" //else "no description available"
            );

            if (this.getScene() != null) {
                description.setMaxWidth(this.getScene().getWidth() - 30);
            }

            String genres = movieEntity.getGenres()
                    .stream()
                    .map(Enum::toString)
                    .collect(Collectors.joining(", "));
            genre.setText(genres);

            setGraphic(layout);
        }
    }
}

