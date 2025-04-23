package com.daytour.ui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import com.daytour.processing.Query;
import com.daytour.processing.DayTour;
import com.daytour.processing.DayTourDetails;
import com.daytour.processing.Review;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Trip implements Initializable {

    @FXML
    private AnchorPane pane;

    // Upplýsingar um ferð

    @FXML
    private ImageView trip_pic;

    // Ummæli

    @FXML
    private ImageView smiley;

    @FXML
    private ImageView dislike;

    @FXML
    private Label notValid;

    @FXML
    private TextArea review;

    @FXML
    private TextField nameReview;

    @FXML
    private TextField titleReview;

    @FXML
    private Rectangle smileyRec;

    @FXML
    private Rectangle dislikeRec;

    // Sýna ummælin
    @FXML
    private GridPane listReivew;

    @FXML
    private Button fxBtnBoka;

    private final String IMGLIB = "/resources/com/daytour/ui/assets/";
    private DayTourDetails day;
    private LocalDate date;
    private String reviewPic;
    private int i = 0;
    private int j = 0;

    private Query q;

    // private DayTour day;

    public void whatTrip(DayTour d, LocalDate date) {
        this.day = q.searchTourDetails(d.getID() + "", date, date, 'd', null)[0];
        this.date = date;
        getReivew(this.day);
        ((Label) this.pane.getChildren().get(4)).setText(day.getName());
        ((Label) this.pane.getChildren().get(5)).setText(day.getTown());
        ((Label) this.pane.getChildren().get(23)).setText(day.getCompany());
        ((Label) this.pane.getChildren().get(26)).setText(day.getOpeningHours());
        ((Label) this.pane.getChildren().get(28)).setText(day.getPrice() + "");
        ((Label) this.pane.getChildren().get(24)).setText(day.getLength() + "");
        ((TextArea) this.pane.getChildren().get(8)).setText(day.getDescription());
        try {
            ((ImageView) this.pane.getChildren().get(3)).setImage(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMGLIB + day.getPictures()[0]))));
        } catch (NullPointerException ignored) {
            ((ImageView) this.pane.getChildren().get(3)).setImage(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMGLIB + "MyndVantar.png"))));
        }

        initializeLikable();
    }

    public void initializeLikable() {
        dislikeRec.setFill(Color.web("#31CED4"));
        smileyRec.setFill(Color.web("#31CED4"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        q = new Query();
    }

    @FXML
    private void back_to_primary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void book_trip() throws IOException {
        FXMLLoader fxmlLoader = App.setRoot("book");
        Book bookController = fxmlLoader.getController();
        bookController.tripBook(this.day, this.date);
    }

    @FXML
    private void like_dislike(MouseEvent event) {
        String l = event.getPickResult().getIntersectedNode().getId();
        if (l.equals("smiley")) {
            reviewPic = "smiley.png";
            smileyRec.setFill(Color.web("#D2FACE"));
            dislikeRec.setFill(Color.web("#31CED4"));
        } else {
            reviewPic = "dislike.png";
            dislikeRec.setFill(Color.web("#D2FACE"));
            smileyRec.setFill(Color.web("#31CED4"));
        }
    }

    @FXML
    private void post_review() {
        String picString = reviewPic;
        boolean pic = picString.equals("smiley.png");

        Review r = new Review(nameReview.getText(), titleReview.getText(), review.getText(), pic);
        String v = isReviewValid(r);
        if (v != null) {
            notValid.setText(v);
        } else {
            ReviewCard s = new ReviewCard();
            s.makeReview(r.getTitle(), r.getName(), r.getReview(), picString);
            putReviewCard(s);
            clearReviewCard();

            q.addReview(day, r);
        }
    }

    private String isReviewValid(Review r) {
        String errorText = "Villa í ummælum";
        if (r.getTitle().equals("") || r.getName().equals("") || r.getReview().equals("")) {
            return errorText;
        }
        return null;
    }

    private void clearReviewCard() {
        review.clear();
        nameReview.clear();
        titleReview.clear();
        smileyRec.setFill(Color.web("#31CED4"));
        dislikeRec.setFill(Color.web("#31CED4"));
    }

    private void putReviewCard(ReviewCard s) {
        if (i >= 2) {
            i = 0;
            j++;
        }
        listReivew.add(s, i, j, 1, 1);
        i++;
    }

    private void getReivew(DayTourDetails dtt) {
        Review[] reviews = dtt.getReviews();
        for (Review value : reviews) {
            ReviewCard s = new ReviewCard();
            String pic;
            if (value.getLiked()) {
                pic = "smiley.png";
            } else {
                pic = "dislike.png";
            }
            s.makeReview(value.getTitle(), value.getName(), value.getReview(), pic);
            putReviewCard(s);
        }
    }
}
