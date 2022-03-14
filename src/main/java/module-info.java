module fr.askyna.treeapp.treeappbuilder {
    requires javafx.controls;
    requires javafx.fxml;


    opens fr.askyna.treeapp.treeappbuilder to javafx.fxml;
    exports fr.askyna.treeapp.treeappbuilder;
}