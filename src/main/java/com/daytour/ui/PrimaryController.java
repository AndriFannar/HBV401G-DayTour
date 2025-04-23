package com.daytour.ui;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import com.daytour.processing.Query;
import com.daytour.processing.DayTour;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class PrimaryController implements Initializable {
    @FXML
    private TextField tf;

    @FXML
    private GridPane grid;

    @FXML
    private Menu menu;

    @FXML
    private Label error;

    @FXML
    public DatePicker fxDagatal;

    @FXML
    private Label fxDagsetning;

    @FXML
    private RadioButton fxRdoPop, fxRdoPrice;

    private final Query q = new Query();

    private CheckMenuItem lastSelectedMenuItem;
    static final int ISLAND = 108;
    private LocalDate date;
    String loc;
    private static final DateTimeFormatter d = DateTimeFormatter.ofPattern("EEEE dd MM yyyy",
            Locale.forLanguageTag(Locale.getISOCountries()[ISLAND]));
    private char order;
    private String filter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        date = LocalDate.of(2023, 04, 04);
        loc = null;
        order = 'p';
        fxRdoPop.setSelected(true);
        filter = null;
        popularCards();
    }

    @FXML
    private void searchDatabase() {
        makeCards();
    }

    @FXML
    private void filterout() {
        remove();
        ObservableList<MenuItem> menucheck = menu.getItems();

        for (MenuItem m : menucheck) {
            if (m instanceof CheckMenuItem checkMenuItem) {
                if (checkMenuItem.isSelected()) {
                    if (lastSelectedMenuItem != null) {
                        lastSelectedMenuItem.setSelected(false); // deselect the last selected CheckMenuItem
                    }
                    lastSelectedMenuItem = checkMenuItem; // update the lastSelectedMenuItem variable to the currently
                                                          // selected CheckMenuItem
                    filter = checkMenuItem.getText();

                    DayTour[] filter_trips = q.searchTours(loc, date, date, order, filter);
                    Card[] cards = new Card[filter_trips.length];
                    setCard(findCard(cards, filter_trips));

                } else if (checkMenuItem == lastSelectedMenuItem) {
                    lastSelectedMenuItem = null;
                    filter = null;

                    DayTour[] trips = q.searchTours(loc, date, date, order, null);
                    Card[] cards = new Card[trips.length];
                    setCard(findCard(cards, trips));
                }
            }
        }
    }

    @FXML
    private void update() {
        makeCards();
    }

    @FXML
    private void refresh() {
        remove();
        popularCards();
    }

    private void popularCards() {
        fxDagsetning.setText("Allar ferðir");
        DayTour[] top_trips = q.searchTours(null, date, date, 'p', filter);
        Card[] cards = new Card[top_trips.length];
        setCard(findCard(cards, top_trips));
    }

    private void makeCards() {
        /*
         * if(tf.getText().isEmpty()|| fxDagatal.getValue() == null){
         * error.setText("Vantar annadhvort staðsetningu eða dagsetningu");
         * System.out.println(tf.getText());
         * } else {
         */
        error.setText("");
        loc = tf.getText();
        if (fxDagatal.getValue() != null)
            date = fxDagatal.getValue();
        DayTour[] trips = q.searchTours(loc, date, date, order, filter);
        Card[] cards = new Card[trips.length];
        if (trips.length == 0) {
            error.setText("Engin ferð finnst, skoðaðu ef staðfestning er rétt");
        } else {
            remove();
            if (fxDagatal.getValue() != null) {
                date = fxDagatal.getValue();
                fxDagsetning.setText(fxDagatal.getValue().format(d));
            }
            setCard(findCard(cards, trips));
        }
    }
    // }

    private Card nyttCard(DayTour d) {
        Card c = new Card(d, date);
        c.setCard();
        return c;
    }

    private Card[] findCard(Card[] cards, DayTour[] trips) {
        for (int i = 0; i < trips.length; i++) {
            if (trips[i] != null) {
                DayTour d = trips[i];
                cards[i] = nyttCard(d);
            }
        }
        return cards;
    }

    private void setCard(Card[] cards) {
        int k = 0;
        int l = cards.length / 4;
        for (int i = 0; i <= l; i++) {
            for (int j = 0; j < 4; j++) {
                if ((k != cards.length) && (k < 12) && (cards[k] != null)) {
                    grid.add(cards[k], j, i, 1, 1);
                    k++;
                }
            }
        }
    }

    private void remove() {
        grid.getChildren().removeAll(grid.getChildren());
    }

    @FXML
    private void orderHandler(ActionEvent ae) {
        if (ae.getSource().equals(fxRdoPop))
            order = 'p';
        else if (ae.getSource().equals(fxRdoPrice))
            order = 'v';
        else
            order = 't';

        try {
            searchDatabase();
        } catch (Exception ignored) {
        }
    }
}
