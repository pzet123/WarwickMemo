package com.codesoc.warwickmemo;
import java.time.LocalDateTime;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

//Code by Piotr Zychlinski: https://github.com/pzet123

public class WarwickMemo extends Application{

    private static List<ScheduledItem> scheduledItems;
    private static int stageHeight;
    private static int stageWidth;
    private static BorderPane rootLayout;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        scheduledItems = SItemFileManager.readScheduledItems();
        stageHeight = 480;
        stageWidth = 760;
    }
    
    @Override
    public void start(Stage primaryStage) {
        ItemCreationWindow.setScheduledItems(scheduledItems);

        primaryStage.setTitle("Warwick Memo");
        rootLayout = new BorderPane();

        Scene scene = new Scene(rootLayout, stageWidth, stageHeight);
        scene.getStylesheets().add(WarwickMemo.class.getResource("stylesheet.css").toExternalForm());
        

        Label title = new Label("Warwick Memo");
        title.setAlignment(Pos.CENTER);
        title.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        title.setPadding(new Insets(10, 0, 20, 0));
        title.setId("title");

        Node moduleScrollPane = itemScrollPane();
        Label introText = scheduledItems.size() > 0 ? 
                            new Label("Select an Item") : 
                            new Label("Add an Item");

        rootLayout.setTop(title);
        rootLayout.setLeft(moduleScrollPane);
        rootLayout.setCenter(introText);

        BorderPane.setAlignment(introText, Pos.CENTER);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        SItemFileManager.writeScheduledItems(scheduledItems);
    }

    private static Node itemScrollPane() {
        VBox sidebarLayout = new VBox();
        ScrollPane itemScrollPane = new ScrollPane();
        itemScrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        itemScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        VBox items = new VBox(10);

        scheduledItems.sort(ScheduledItem.scheduledItemComparator);
        for(ScheduledItem item : scheduledItems) {
            if(item.getNextReviewDate().isBefore(LocalDateTime.now())){
                items.getChildren().add(generateSidebarNode(item, items));
            }
        }

        itemScrollPane.setContent(items);
        itemScrollPane.setFitToWidth(true);

        Button addItemButton = new Button("Add New Item");
        addItemButton.setMaxWidth(10000);
        addItemButton.setOnAction((event) -> {
            ItemCreationWindow.show();
        });
        addItemButton.getStyleClass().add("sharpButton");

        sidebarLayout.getChildren().addAll(itemScrollPane, addItemButton);
        VBox.setVgrow(itemScrollPane, Priority.ALWAYS);
        return sidebarLayout;
    }

    public static void refreshSideBar(){
        rootLayout.setLeft(itemScrollPane());
        rootLayout.setCenter(new Label("Select an Item"));
    }

    public static Node generateSidebarNode(ScheduledItem item, VBox sideBarLayout) {
        Button itemButton = new Button(item.getItemName());
        itemButton.getStyleClass().add("sharpButton");
        itemButton.setMaxWidth(200);
        itemButton.setOnAction((event) -> {
            rootLayout.setCenter(item.generateModuleNode());
        });

        MenuItem deleteItem = new MenuItem("Delete Item");
        deleteItem.setOnAction((ae) -> {
            sideBarLayout.getChildren().remove(itemButton);
            scheduledItems.remove(item);
        });
        
        ContextMenu itemMenu = new ContextMenu(deleteItem);
        itemButton.setContextMenu(itemMenu);
        return itemButton;
    }
}
