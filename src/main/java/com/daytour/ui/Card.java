package com.daytour.ui;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import com.daytour.processing.DayTour;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Card extends AnchorPane {

    private final String CARDFXML = "/resources/com/daytour/ui/card.fxml";
    private final String IMGLIB   = "/resources/com/daytour/ui/assets/";

    @FXML
    private ImageView event_img;
    @FXML
    private ImageView rt;
    @FXML
    private Label rating;
    
    private final DayTour day;

    private final LocalDate dte;

    public Card(DayTour d, LocalDate date){
        this.day = d;
        this.dte = date;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(CARDFXML));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }

    @FXML
    private void opna_ferd() throws IOException {
        FXMLLoader fxmlLoader = App.setRoot("trip");
        Trip tripController = fxmlLoader.getController();
        tripController.whatTrip(this.day, dte);
    }

    public void setCard(){
        //
        ((Label)getChildren().get(1)).setText(day.getName());
        ((Label)getChildren().get(2)).setText(day.getTown());
        ((Label)getChildren().get(5)).setText(day.getPrice() + "");

        setRat(day.getRating());
        try
        {
            event_img.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMGLIB +day.getPictures()[0]))));
        }
        catch (NullPointerException e)
        {
            event_img.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMGLIB + "MyndVantar.png"))));
        }
    }

    private void setRat(double rat){
        int stars = (int) Math.round(rat);
        rt.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMGLIB + "star.png"))));
        if (stars != 0) ((Label)getChildren().get(9)).setText(stars + "");
        else ((Label)getChildren().get(9)).setText("-");
    }
}
