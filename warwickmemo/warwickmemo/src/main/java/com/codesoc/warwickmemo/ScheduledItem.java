package com.codesoc.warwickmemo;
import java.time.LocalDateTime;
import java.util.Comparator;

import javafx.scene.Node;


interface ScheduledItem{    

    public String getItemName();
    
    public LocalDateTime getNextReviewDate();

    public Node generateModuleNode();

    static Comparator<ScheduledItem> scheduledItemComparator = new Comparator<ScheduledItem>() { //Anonymous inner class
        public int compare(ScheduledItem itemOne, ScheduledItem itemTwo){
            if(itemOne.getNextReviewDate().isAfter(itemTwo.getNextReviewDate())){
                return 1;
            } else if(itemOne.getNextReviewDate().isBefore(itemTwo.getNextReviewDate())){
                return -1;
            } else {
                return 0;
            }
        }
    };

}
