package fit.biepjv.deadlines;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class DeadlinesAppController implements Initializable {

    @FXML
    RadioButton todayRB, tomorrowRB, thisWeekRB, customRB;

    @FXML
    DatePicker dateFrom, dateTo;

    @FXML
    ListView<Event> listView;

    SQLConnection sqlConnection = new SQLConnection("jdbc:sqlite:identifier.sqlite");
    EventList eventList = new EventList();
    Event selectedEvent;
    Date rangeFrom, rangeTo;
    Boolean edit = false;

    @FXML
    void refreshEvents() throws SQLException {
        if (!customRB.isSelected() || (dateFrom.getValue() != null && dateTo.getValue() != null)) {
            if (customRB.isSelected()) {
                rangeFrom = localDateToDate(dateFrom.getValue());
                rangeTo = localDateToDate(dateTo.getValue());
            }
            eventList.refresh(sqlConnection.getResult(rangeFrom, rangeTo));
            listView.setItems(eventList);
        }
    }

    @FXML
    void addEvent() throws SQLException {
        Dialog<Event> dialog = new Dialog<>();
        dialog.setTitle("Event editor");
        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #e6e6e6;");
        TextField eventTitle = new TextField(); eventTitle.setPromptText("Event name");
        eventTitle.setStyle("-fx-font-family: Gogh;");
        TextArea eventDesc = new TextArea(); eventDesc.setPromptText("Event description");
        eventDesc.setStyle("-fx-font-family: Gogh;");
        DatePicker datePicker = new DatePicker(); datePicker.setPromptText("Due date");
        datePicker.setStyle("-fx-font-family: Gogh; ");

        if (edit) {
            String title = selectedEvent.getTitle();
            String description = selectedEvent.getDescription();
            Date date = (Date) selectedEvent.getDate();
            eventTitle.setText(title);
            eventDesc.setText(description);
            datePicker.setValue(dateToLocalDate(date));
        }
        grid.add(eventTitle, 1, 1);
        grid.add(eventDesc, 1, 2);
        grid.add(datePicker, 1, 3);
        ButtonType buttonTypeOk = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(b -> {
            if (b == buttonTypeOk) {
                try {
                    return new Event(sqlConnection.maxId()+1, eventTitle.getText(), eventDesc.getText(), localDateToDate(datePicker.getValue()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        Optional<Event> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (edit) deleteEvent();
            sqlConnection.insertEventIntoDb(result.get());
            refreshEvents();
        }
        edit = false;
    }

    @FXML
    void deleteEvent() throws SQLException {
        if (selectedEvent == null) return;
        int idToDelete = selectedEvent.getId();
        eventList.remove(idToDelete);
        sqlConnection.deleteEventFromDb(idToDelete);
    }

    @FXML
    void editEvent() throws SQLException {
        if (selectedEvent != null) {
            this.edit = true;
            addEvent();
        }
    }

    @FXML
    public void getRange() throws SQLException {
        if (todayRB.isSelected()) {
            dateFrom.setDisable(true); dateTo.setDisable(true);
            dateFrom.setVisible(false); dateTo.setVisible(false);
            rangeFrom = localDateToDate(LocalDate.now());
            rangeTo = localDateToDate(LocalDate.now());
            refreshEvents();
        } else if (tomorrowRB.isSelected()) {
            dateFrom.setDisable(true); dateTo.setDisable(true);
            dateFrom.setVisible(false); dateTo.setVisible(false);
            rangeFrom = localDateToDate(LocalDate.now().plusDays(1));
            rangeTo = localDateToDate(LocalDate.now().plusDays(1));
            refreshEvents();
        } else if (thisWeekRB.isSelected()) {
            dateFrom.setDisable(true); dateTo.setDisable(true);
            dateFrom.setVisible(false); dateTo.setVisible(false);
            rangeFrom = localDateToDate(LocalDate.now());
            rangeTo = localDateToDate(LocalDate.now().plusDays(7));
            refreshEvents();
        } else if (customRB.isSelected()) {
            dateFrom.setDisable(false); dateTo.setDisable(false);
            dateFrom.setVisible(true); dateTo.setVisible(true);
            if (dateFrom.getValue() != null && dateTo.getValue() != null) {
                rangeFrom = localDateToDate(dateFrom.getValue());
                rangeTo = localDateToDate(dateTo.getValue());
                refreshEvents();
            }
        }
    }

    @FXML
    public void showDescription() {
        if (selectedEvent != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Event description");
            alert.setHeaderText(selectedEvent.getTitle());
            alert.setContentText(selectedEvent.getDescription());
            alert.showAndWait();
        }
    }

    public void initialize(URL arg0, ResourceBundle arg1) {
        rangeFrom = localDateToDate(LocalDate.now());
        rangeTo = localDateToDate(LocalDate.now().plusDays(1));
        System.out.println(rangeFrom.toString());
        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, event, t1) -> selectedEvent = listView.getSelectionModel().getSelectedItem());
    }

    private Date localDateToDate (LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }
    public LocalDate dateToLocalDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

}