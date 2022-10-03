package com.codesoc.warwickmemo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SItemFileManager {

    static List<ScheduledItem> readScheduledItems(){
        try (FileInputStream serFile = new FileInputStream("scheduledItems.ser");
            ObjectInputStream serItems = new ObjectInputStream(serFile)){
            return (List<ScheduledItem>) (serItems.readObject());
        } catch (Exception e) {
            System.err.println("Error reading scheduledItems.ser into memory: " + e);
            return new ArrayList<>();
        }
    }

    static void writeScheduledItems(List<ScheduledItem> scheduledItems){
        if(scheduledItems != null){
            try (FileOutputStream serFile = new FileOutputStream("scheduledItems.ser");
                ObjectOutputStream serItems = new ObjectOutputStream(serFile)){
                serItems.writeObject(scheduledItems);
                serItems.flush();
            } catch (Exception e) {
                System.err.println("Error writing scheduled items to file: " + e);
            }
        }
    } 
}
