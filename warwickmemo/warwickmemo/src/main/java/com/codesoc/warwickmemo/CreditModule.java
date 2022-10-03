package com.codesoc.warwickmemo;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;


class CreditModule extends SM2Item {

    public int creditAmount;
    private String reviewNotes;

    @ConstructorParameters (
        parameterNames = {"Module Name", "Credit Amount"},
        parameterTypes = {String.class, Integer.class}
    )
    public CreditModule(String moduleName, int creditAmount){
        super(moduleName);
        this.creditAmount = creditAmount;
        this.reviewNotes = "";
    }

    @Override
    public Node generateModuleNode(){
        VBox mainLayout = new VBox(15);
        Label moduleCreditLabel = new Label("Module Credits: " + Integer.toString(creditAmount));
        moduleCreditLabel.setId("moduleCredits");
        Label reviewNotesLabel = new Label(!reviewNotes.isEmpty() ? "Previous Review Notes: " + reviewNotes : "No Review Notes");
        reviewNotesLabel.setWrapText(true);

        TextArea reviewNotesText = new TextArea(reviewNotes.isEmpty() ? "Enter Review Notes" : reviewNotes);
        reviewNotesText.setWrapText(true);

        mainLayout.getChildren().addAll(moduleCreditLabel, reviewNotesText);
        mainLayout.setAlignment(Pos.TOP_CENTER);

        return super.generateModuleNode(mainLayout, () -> {
            reviewNotes = reviewNotesText.getText();
        });
    }

    public String getReviewNotes() {
        return this.reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public int getCreditAmount() {
        return this.creditAmount;
    }

    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }

}
