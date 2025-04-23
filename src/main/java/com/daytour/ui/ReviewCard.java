package com.daytour.ui;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class ReviewCard extends AnchorPane
{
    @FXML
    private ImageView pic;
    private final String STARFXML = "/resources/com/daytour/ui/review.fxml";
    private final String IMGLIB   = "/resources/com/daytour/ui/assets/";

    public ReviewCard(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(STARFXML));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }

    public void makeReview(String t, String n, String d, String i){

        ((Label)getChildren().get(0)).setText(t);
        ((Label)getChildren().get(1)).setText(n);
        ((TextArea)getChildren().get(2)).setText(d);
        pic.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMGLIB + i))));
    }
    
}
