package com.daytour.ui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import com.daytour.processing.Booking;
import com.daytour.processing.DayTourDetails;
import com.daytour.processing.Query;
import javafx.fxml.Initializable;

public class Book implements Initializable {
  @FXML
  private Label fxNafnFerdar;

  @FXML
  private Label fxDagsetning;

  @FXML
  private TextField fxNafn;

  @FXML
  private TextField fxNetfang;

  @FXML
  private ChoiceBox<String> fxTimi;

  @FXML
  private Label fxFjoldi;

  @FXML
  private TextField fxValinnFjoldi;

  @FXML
  private CheckBox fxSkutl;

  @FXML
  private RadioButton fxKredit;

  @FXML
  private RadioButton fxPaypal;

  @FXML
  private Button fxBokaTakki;

  @FXML
  private Label fxEkkiLeyfilegurFjoldi;

  private DayTourDetails day;
  private LocalDate date;
  private String nafn;
  private String netfang;
  private String valinnFjoldi;
  private int fjoldiPlassa;
  private Booking booking;
  private Query query;
  private int lokaverd;
  private String borgunarAdferd;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    fxBokaTakki.setDisable(true);
    fxValinnFjoldi.setDisable(true);
    fxEkkiLeyfilegurFjoldi.setVisible(false);
    query = new Query();
  }

  public void tripBook(DayTourDetails d, LocalDate date) {
    this.day = d;
    this.date = date;
    String[] timi = getTimes();
    fxTimi.getItems().addAll(timi);
    fxTimi.setOnAction(this::getTimi);
    fxNafnFerdar.setText(day.getName());
    fxDagsetning.setText(date.toString());
  }

  public void getTimi(ActionEvent ae) {
    LocalTime time = LocalTime.parse(fxTimi.getValue());
    fjoldiPlassa = getAvailability(time);
    fxFjoldi.setText(getAvailability(time) + " laus pláss");
    fxValinnFjoldi.setDisable(false);
  }

  @FXML
  private void back_to_primary() throws IOException {
    App.setRoot("primary");
  }

  public void keyReleasedProperty() {
    nafn = fxNafn.getText();
    netfang = fxNetfang.getText();
    valinnFjoldi = fxValinnFjoldi.getText();

    boolean isDisabled = ((valinnFjoldi.isEmpty()) || (valinnFjoldi.trim().isEmpty()) || nafn.isEmpty() ||
        nafn.trim().isEmpty())
        || (netfang.isEmpty() || netfang.trim().isEmpty() || !(fxKredit.isSelected() ||
            fxPaypal.isSelected()) ||
            !netfang.matches(
                "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"));

    fxBokaTakki.setDisable(isDisabled);
  }

  @FXML
  public void stadfestaBokun() {
    try {
      int vFjoldi = Integer.parseInt(fxValinnFjoldi.getText());
      int fPlassa = fjoldiPlassa;
      if (vFjoldi < 1 || fPlassa < vFjoldi) {
        fxEkkiLeyfilegurFjoldi.setVisible(true);
      } else {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Staðfesting Bókunar");
        lokaverd = Integer.parseInt(fxValinnFjoldi.getText()) * this.day.getPrice();
        if (fxKredit.isSelected()) {
          borgunarAdferd = "Kredit/Debit";
        } else {
          borgunarAdferd = "PayPal";
        }
        alert.setContentText("Verð: " + lokaverd + " kr" + "\n" + "Viltu staðfesta bókunina?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
          booking = new Booking(day.getID(), nafn, netfang, date, LocalTime.parse(fxTimi.getValue()), vFjoldi,
              fxSkutl.isSelected(), borgunarAdferd, lokaverd);
          query.addBooking(booking);
        }
        alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Kvittun hefur verið send á þetta netfang: " + fxNetfang.getText() + "\n"
            + "Takk fyrir að eiga viðskipti við okkur hjá DayTours");
        alert.showAndWait();
        back_to_primary();
      }
    } catch (Exception e) {
      fxEkkiLeyfilegurFjoldi.setVisible(true);
    }
  }

  private String[] getTimes() {
    LocalTime[] times = this.day.getTimes(this.date);
    String[] s = new String[times.length];
    for (int i = 0; i < times.length; i++) {
      s[i] = String.valueOf(times[i]);
    }
    Arrays.sort(s);
    return s;
  }

  private int getAvailability(LocalTime time) {
    return day.getAvailable(this.date, time);
  }
}
