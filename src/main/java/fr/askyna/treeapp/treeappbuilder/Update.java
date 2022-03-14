package fr.askyna.treeapp.treeappbuilder;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Update {

    public static void setText(Label label, String text){
        Platform.runLater(() -> label.setText(text));
    }

}
