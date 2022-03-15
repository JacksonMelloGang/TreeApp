package fr.askyna.treeapp.treeappbuilder;

import fr.askyna.treeapp.treeappbuilder.views.MainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class TreeApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /**FXMLLoader fxmlLoader = new FXMLLoader(TreeApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);**/

        VBox mainView = new MainView().getScreen();

        Scene mainScene = new Scene(mainView, 900, 600);

        stage.setTitle("TreeApp");
        stage.setScene(mainScene);
        //stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}