package com.codesoc.warwickmemo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//SuperMemo 2 algorithm by Piotr Wozniak: https://www.supermemo.com/en/archives1990-2015/english/ol/sm2

public class SM2Item implements ScheduledItem, Serializable{

    enum Difficulty {

        EASY    (5), //Perfect Response
        MEDIUM  (3), //Correct response recalled with serious difficulty
        HARD    (1); //Inocrrect response, the correct one remembered
    
        public final int resQualityVal;
    
        Difficulty(int resQualityVal){
            this.resQualityVal = resQualityVal;
        }
    
    }

    private double eFactor;
    private int interval;
    private int numOfRepetitions;
    private LocalDateTime nextReviewDate;
    private String itemName;

    @ConstructorParameters (
        parameterNames = {"Item Name"},
        parameterTypes = {String.class}
    )
    public SM2Item(String itemName){
        this.itemName = itemName;
        this.nextReviewDate = LocalDateTime.now();
        this.eFactor = 2.5;
        this.interval = 1;
        this.numOfRepetitions = 1;
    }

    public void scheduleItem(Difficulty responseQuality){
        if(responseQuality.resQualityVal < 3) {
            numOfRepetitions = 1;
        }
        modifyInterval();
        modifyEFactor(responseQuality);
        nextReviewDate = LocalDateTime.now().plusDays(interval);
        Alerts.showItemScheduledAlert(nextReviewDate);
    }

    private void modifyEFactor(Difficulty responseQuality){
        int q = responseQuality.resQualityVal;
        double newEFactor = eFactor + (0.1 - (5 - q) * (0.08 + (5 - q) * 0.02)); //SM2 Function for calculating EF
        eFactor = (newEFactor < 1.3) ? 1.3 : newEFactor;
    }

    private void modifyInterval() {
        switch (numOfRepetitions){
            case 1:
                interval = 1;
                break;
            case 2:
                interval = 6;
                break;
            default:
                interval = (int) Math.ceil(interval * eFactor);
        }
        numOfRepetitions++;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public LocalDateTime getNextReviewDate() {
        return nextReviewDate;
    }

    @Override
    public Node generateModuleNode() {
        return generateModuleNode(null, null);
    }

    public Node generateModuleNode(Node content, Runnable callback) {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));
        Label moduleNameLabel = new Label(itemName);
        moduleNameLabel.setId("itemName");
        moduleNameLabel.setPadding(new Insets(0,0,50,0));
        
        // Difficulty buttons
        Button easyButton = new Button("Easy");
        Button mediumButton = new Button("Medium");
        Button hardButton = new Button("Hard");

        easyButton.setPrefWidth(Integer.MAX_VALUE);
        mediumButton.setPrefWidth(Integer.MAX_VALUE);
        hardButton.setPrefWidth(Integer.MAX_VALUE);

        easyButton.getStyleClass().add("difficultyButton"); 
        mediumButton.getStyleClass().add("difficultyButton");
        hardButton.getStyleClass().add("difficultyButton");

        HBox reviewButtonRow = new HBox(25);

        easyButton.setOnAction((ae) -> {
            scheduleItem(Difficulty.EASY);
            WarwickMemo.refreshSideBar();
            if(callback != null) callback.run();
        });
        mediumButton.setOnAction((ae) -> {
            scheduleItem(Difficulty.MEDIUM);
            WarwickMemo.refreshSideBar();
            if(callback != null) callback.run();
        });
        hardButton.setOnAction((ae) -> {
            scheduleItem(Difficulty.HARD);
            WarwickMemo.refreshSideBar();
            if(callback != null) callback.run();
        });

   
        reviewButtonRow.getChildren().addAll(easyButton, mediumButton, hardButton);
        BorderPane.setAlignment(moduleNameLabel, Pos.CENTER);
        mainLayout.setTop(moduleNameLabel);
        mainLayout.setCenter(content);
        mainLayout.setBottom(reviewButtonRow);
        return mainLayout;
    }
}
