package com.codesoc.warwickmemo;

import java.time.LocalDateTime;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerts {
    

    static Alert invalidIntegerCastAlert = new Alert(AlertType.ERROR);
    static Alert itemScheduledAlert = new Alert(AlertType.INFORMATION);
    
    static { 
        invalidIntegerCastAlert.setHeaderText("Invalid Integer Cast!");
        invalidIntegerCastAlert.setContentText("You attempted to cast a non-integer text input to an integer. Item Creation Failed.");
    
        itemScheduledAlert.setHeaderText("Item Scheduled");

    }

    static void showInvalidIntegerCastAlert() {
        invalidIntegerCastAlert.showAndWait();
    }

    static void showItemScheduledAlert(LocalDateTime scheduleDate) {
        itemScheduledAlert.setContentText("Item has been scheduled for: " + scheduleDate.toLocalDate());
        itemScheduledAlert.show();
    }
}
