package com.codesoc.warwickmemo;

import java.lang.reflect.Constructor;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


class ItemCreationWindow {

    //Make sure to add any new ScheduledItem subclasses to this array
    private static Class<?>[] scheduledItemTypes = {CreditModule.class, SM2Item.class};
    private static List<ScheduledItem> scheduledItems;

    static Stage stage;

    static { //Static "constructor"
        VBox rootLayout = new VBox(15);
        VBox itemInputLayout = new VBox(5);
        itemInputLayout.setAlignment(Pos.CENTER);
        itemInputLayout.setPadding(new Insets(10));
        rootLayout.setAlignment(Pos.TOP_CENTER);
        Label itemCreationLabel = new Label("Add a new scheduled item");
        rootLayout.getChildren().addAll(itemCreationLabel);
        itemCreationLabel.setPadding(new Insets(15, 0, 0, 0));
        ComboBox<String> itemClassOptions = new ComboBox<>();

        for(Class<?> item : scheduledItemTypes){
            itemClassOptions.getItems().add(item.getSimpleName());
        }

        itemClassOptions.setOnAction((event) -> {
            itemInputLayout.getChildren().clear();
            Class<?> selectedClass = scheduledItemTypes[itemClassOptions.getSelectionModel().getSelectedIndex()];
            updateScheduledItemInput(selectedClass, itemInputLayout);
        });

        rootLayout.getChildren().add(itemClassOptions);
        rootLayout.getChildren().add(itemInputLayout);
        Scene scene = new Scene(rootLayout, 250, 400);
        scene.getStylesheets().add(WarwickMemo.class.getResource("stylesheet.css").toExternalForm());
        stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
    }

    static void createItem(TextField[] paramInputs, Class<?>[] parameterTypes, Class<? extends ScheduledItem> newItemClass) throws Exception{
        Object[] params = new Object[paramInputs.length];
        for(int i = 0; i < paramInputs.length; i++){
            if(parameterTypes[i] == String.class){
                params[i] = paramInputs[i].getText();
            } else if(parameterTypes[i] == Integer.class || parameterTypes[i] == int.class){
                try {
                    params[i] = Integer.parseInt(paramInputs[i].getText());
                } catch (java.lang.NumberFormatException e){
                    Alerts.showInvalidIntegerCastAlert();
                    return;
                }
            } else {
                throw new Exception("Unsupported type cast, You must implement the cast in itemCreationWindow.createItem");
            }
        } 
        Constructor<?>[] itemConstructors = newItemClass.getConstructors();
        Constructor<?> itemConstructor = itemConstructors[0];
        for(Constructor<?> constructor : itemConstructors) {
            if(constructor.isAnnotationPresent(ConstructorParameters.class)){
                itemConstructor = constructor;
                break;
            }
        }
        ScheduledItem newItem = (ScheduledItem) itemConstructor.newInstance(params);
        scheduledItems.add(newItem);
        WarwickMemo.refreshSideBar();
        hide();
    }

    static void show(){
        stage.show();
    }

    static void hide(){
        stage.hide();
    }

    static void setScheduledItems(List<ScheduledItem> newScheduledItems){
        scheduledItems = newScheduledItems;
    }

    static void updateScheduledItemInput(Class<?> selectedClass, VBox itemInputLayout){
        Class<?>[] parameterTypes = ((ConstructorParameters) selectedClass.getConstructors()[0].getAnnotation(ConstructorParameters.class)).parameterTypes();
        String[] parameterNames = ((ConstructorParameters) selectedClass.getConstructors()[0].getAnnotation(ConstructorParameters.class)).parameterNames();
        TextField[] paramInputs = new TextField[parameterNames.length];
        for (int i = 0; i < parameterNames.length; i++){
            TextField paramInput = new TextField();
            Label paramLabel = new Label(parameterNames[i]);
            paramInputs[i] = paramInput;
            itemInputLayout.getChildren().addAll(paramLabel, paramInput);
        }
        Button createItemButton = new Button("Add Item");
        createItemButton.setOnAction((ae) -> {
            try {
                createItem(paramInputs, parameterTypes, (Class<ScheduledItem>) selectedClass);
            } catch (Exception e) {
                System.err.println("Item Creation Failed: " + e);
            }
        });
        itemInputLayout.getChildren().add(createItemButton);
    }

}