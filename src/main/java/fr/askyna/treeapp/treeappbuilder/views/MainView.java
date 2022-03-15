package fr.askyna.treeapp.treeappbuilder.views;

import fr.askyna.treeapp.treeappbuilder.Update;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

public class MainView {

    private static VBox vbox;
    private List<String> folderList = new ArrayList<>();


    public MainView(){
        vbox = buildView();
    }

    public VBox buildView(){
        VBox view = new VBox();

        // Context Menu
        ContextMenu treemenu = new ContextMenu();
        MenuItem treemenucreatfile = new MenuItem("Create File");
        MenuItem treemenucreatfolder = new MenuItem("Create Folder");
        SeparatorMenuItem treemenusep = new SeparatorMenuItem();
        MenuItem treemenudelete = new MenuItem("Delete");
        treemenu.getItems().addAll(treemenucreatfile, treemenucreatfolder, treemenusep, treemenudelete);

        // Menu Bar (Top bar // Action bar)
        MenuBar menuBar = new MenuBar();
            Menu fileMenu = new Menu("File");
                MenuItem newItem = new MenuItem("New");
                MenuItem openItem = new MenuItem("Open...");
                Menu recentOpenedFileItem = new Menu("Recent Files");
                Menu exportItem = new Menu("Export");
                MenuItem closeItem = new MenuItem("Close");
                MenuItem saveItem = new MenuItem("Save");
                MenuItem saveAsItem = new MenuItem("Save As...");
                SeparatorMenuItem separatorMenu1 = new SeparatorMenuItem();
                MenuItem quitItem = new MenuItem("Quit");

                // Event for filemenu's MenuItem
                newItem.setOnAction((e) -> {

                });


            // Adding MenuItem into Menu (fileMenu)
            fileMenu.getItems().addAll(newItem, openItem, recentOpenedFileItem, exportItem, closeItem, saveItem, saveAsItem, separatorMenu1, quitItem);


            Menu editMenu = new Menu("Edit");


            Menu helpMenu = new Menu("Help");

        // Adding Menus into MenuBar (menuBar)
        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        // Tree Side Pane
        SplitPane splitPane = new SplitPane();
            VBox treebox = new VBox();
                Label treelabel = new Label("Current Tree:");

                TreeItem<String> rootItem = new TreeItem<>("/");
                rootItem.setExpanded(true);
                for(int i = 0; i < 6; i++){
                    TreeItem<String> treeItem = new TreeItem<>("Message " + i);
                    folderList.add(treeItem.getValue());
                    rootItem.getChildren().add(treeItem);
                    treeItem.getChildren().add(new TreeItem<>("TT"));
                }

                TreeView<String> tree = new TreeView<>(rootItem);
                rootItem.addEventHandler(EventType.ROOT, new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        System.out.println(event.getTarget().toString());
                        System.out.println(event.getEventType().toString());
                    }
                });

                treelabel.setPrefSize(215, 27);
                tree.setPrefSize(215, 524);
                tree.setContextMenu(treemenu);
                tree.setEditable(true);
                tree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            treebox.setPrefSize(215, 551);
            treebox.getChildren().addAll(treelabel, tree);


            // Center
            VBox viewbox = new VBox();
                Label viewlabel = new Label("View");
                viewlabel.setAlignment(Pos.CENTER);
                viewlabel.setFont(new Font("System", 18));

                Label viewcontent = new Label("View-Content");
                viewcontent.setAlignment(Pos.CENTER);

                viewlabel.setPrefSize(474, 27);
                viewcontent.setPrefSize(474, 524);
            viewbox.setPrefSize(474, 551);
            viewbox.getChildren().addAll(viewlabel, viewcontent);

            // End, Details of folder / file
            VBox detailsbox = new VBox();
                Label detaillabel = new Label("Detail");
                detaillabel.setAlignment(Pos.CENTER);
                detaillabel.setFont(new Font("System", 18));

                Label viewdetail = new Label("Detail-Content");
                viewdetail.setAlignment(Pos.TOP_LEFT);

                detaillabel.setPrefSize(197, 27);
                viewdetail.setPrefSize(197, 524);
            detailsbox.setPrefSize(197, 551);
            detailsbox.getChildren().addAll(detaillabel, viewdetail);

        // Adding every components into SplitPane
        splitPane.getItems().addAll(treebox, viewbox, detailsbox);
        splitPane.setPrefSize(900, 553);
        // final render
        view.getChildren().addAll(menuBar, splitPane);


        treemenucreatfile.setOnAction((e) -> {
            //String filename = AskDialogString("new File", "Name of the new file");



            for(TreeItem<String> ti : tree.getSelectionModel().getSelectedItems()){
                int position = ti.getChildren().size() > 0 ? ti.getChildren().size() : 0; // Si la taille du tree est sup a 1, treesize = max otherwise create treeitem at first position
                if(ti.getValue().contains("File")) return;
                ti.getChildren().add(position, new TreeItem<>("Filename.txt"));
                ti.setExpanded(true);
                tree.getSelectionModel().select(ti);
                tree.edit(ti);
            }
        });

        treemenucreatfolder.setOnAction((e) -> {
            for(TreeItem<String> ti : tree.getSelectionModel().getSelectedItems()){
                int position = ti.getChildren().size() > 0 ? ti.getChildren().size() : 0; // Si la taille du tree est sup a 1, treesize = max otherwise create treeitem at first position
                ti.getChildren().add(position, new TreeItem<>("Folder"));
                ti.setExpanded(true);
                tree.getSelectionModel().select(ti);
                tree.edit(ti);
            }
        });

        treemenudelete.setOnAction((e) -> {
            if(tree.getSelectionModel().getSelectedItems().size() > 0){
                if(AskConfirmationDialog("Delete Confirmation", "Are you sure you want to delete selected file ?")){

                   for(TreeItem<String> ti : tree.getSelectionModel().getSelectedItems()){
                       Alert error = new Alert(Alert.AlertType.ERROR);
                       error.setTitle("Error");
                       error.setContentText("You can't delete the root !");
                       if(ti ==tree.getRoot()){
                           error.showAndWait();
                           return;
                       }
                       ti.getParent().getChildren().remove(ti);
                   }
                }
            }
        });


        tree.getSelectionModel().selectedItemProperty().addListener((observable, oldvalue, newValue) -> {
            if(newValue != null && newValue != oldvalue){

                int i = 0;

                StringBuilder sb = new StringBuilder();
                sb.append("Name: " + newValue.getValue() + "\n\n");
                sb.append("Selected Item(s): \n");
                // for each elements in selected treeitem
                for(TreeItem<String> ti : tree.getSelectionModel().getSelectedItems()){
                    // Append Treeitem in string builder
                    sb.append("\t -> " + ti.getValue() + "\n");
                    // As long as selected treeItem contains treeitems, append in stringbuilder.
                    if(ti.getChildren().size() != 0){
                        for(TreeItem<String> tichild : ti.getChildren()){
                            sb.append("\t\t ->" + tichild.getValue() + "\n");
                        }
                    }
                }
                System.out.println(observable.getValue());


                Update.setText(viewdetail, sb.toString());
            }
        });

        return view;
    }

    public static VBox getScreen() {
        return vbox;
    }

    private String AskDialogString(String title, String content){
        TextInputDialog dialog = new TextInputDialog("Default");
        dialog.setTitle(title);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        return result.orElseGet(() -> AskDialogString(title, content));
    }

    private boolean AskConfirmationDialog(String title, String content){
        boolean result = false;
        /**
        ButtonType okbutton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType nobutton = new ButtonType("Cancer", ButtonBar.ButtonData.CANCEL_CLOSE);

        Dialog<String> dialog = new Dialog();
        dialog.setTitle(title);
        dialog.setContentText(content);
        dialog.getDialogPane().getButtonTypes().addAll(okbutton, nobutton);
        **/
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        Optional<ButtonType> operationresult = alert.showAndWait();

        if(operationresult.isPresent() && operationresult.get().getButtonData().isDefaultButton()){
            result = true;
        }
        return result;
    }

}
