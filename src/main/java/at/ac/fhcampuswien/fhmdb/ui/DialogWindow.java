package at.ac.fhcampuswien.fhmdb.ui;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class DialogWindow {
    Dialog<String> dialog;

    public DialogWindow(String title, String msg){
        dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(title);
        dialog.getDialogPane().setContent(new javafx.scene.control.Label(msg));

        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(type);
    }

    public void show() {
        dialog.showAndWait();
    }
}

