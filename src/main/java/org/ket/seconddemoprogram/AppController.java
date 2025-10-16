package org.ket.seconddemoprogram;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class AppController {
    @FXML
    private AnchorPane mainContainer;

    @FXML
    private TextField name;

    @FXML
    private ImageView deleteButton;

    @FXML
    private ListView<String> listNames;

    private ObservableList<String> humans;

    private int selectedName = -1;

    private ArrayList<Change> historyChanges;

    @FXML
    private void initialize() {
        humans = FXCollections.observableArrayList("Тимур", "Ксения", "Стефания", "Денис", "Никита");
        historyChanges = new ArrayList<>();
        listNames.setItems(humans);
        deleteButton.setImage(new Image(getClass().getResourceAsStream("bin.png")));
        setupKeyHandlers();
    }

    @FXML
    private void addNameButtonClicked() {
        String tempName = validateName(name.getText());

        if (!tempName.isEmpty()) {
            if (!checkingNameAvailability(tempName)) {
                humans.add(tempName);
                historyChanges.add(new Change(humans.size() - 1, tempName, Change.OperationType.CREATE));
            } else {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Информация");
                info.setHeaderText("Имя уже имеется");
                info.setContentText("Данное имя уже существует");
                info.showAndWait();
            }
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Ошибка");
            error.setHeaderText("Ошибка ввода имени");
            error.setContentText("Не введено имя");
            error.showAndWait();
        }
    }

    @FXML
    private void deleteNameButtonClicked() {
        if (selectedName != -1 && !humans.isEmpty()) {
            String nameTemp = humans.get(selectedName);
            humans.remove(selectedName);
            historyChanges.add(new Change(selectedName, nameTemp, Change.OperationType.DELETE));

            if (selectedName != 0) {
                selectedName--;
            }
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Ошибка");
            error.setHeaderText("Ошибка удаления");
            error.setContentText("Сначала выберите имя для удаления");
            error.showAndWait();
        }
    }

    @FXML
    private void listNamesOnMouseClicked() {
        name.setText(listNames.getSelectionModel().getSelectedItem());
        selectedName = listNames.getSelectionModel().getSelectedIndex();
    }

    @FXML
    private void listNamesOnKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.UP) || keyEvent.getCode().equals(KeyCode.DOWN)) {
            name.setText(listNames.getSelectionModel().getSelectedItem());
            selectedName = listNames.getSelectionModel().getSelectedIndex();
        }
    }

    private void setupKeyHandlers() {
        mainContainer.sceneProperty().addListener(((observableValue, oldScene, newScene) -> {

            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {

                    if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.Z) {

                        if (!historyChanges.isEmpty()) {
                            undoChange();
                        }
                    }
                });
            }
        }));
    }

    private String validateName(String tempName) {
        tempName = tempName.replaceAll("\\s+", "")
                .toLowerCase();

        if (!tempName.matches("[a-zA-Zа-яА-Я]*")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Предупреждение");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Есть не текстовые символы, очистить?");

            ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);

            if (result == ButtonType.OK) {
                tempName = tempName.replaceAll("[^a-zA-Zа-яА-Я]", "");
            }
        }

        if (!tempName.isEmpty()) {
            tempName = tempName.substring(0,1).toUpperCase() + tempName.substring(1);
        }

        return tempName;
    }

    private boolean checkingNameAvailability(String name) {
        for (String tempName : humans) {

            if (name.equals(tempName)) {
                return true;
            }
        }
        return false;
    }

    private void undoChange() {
        Change lastChange = historyChanges.getLast();

        switch (lastChange.getOperationType()) {
            case CREATE:
                humans.remove(lastChange.getIndexList());
                break;
            case DELETE:
                humans.add(lastChange.getIndexList(), lastChange.getName());
                break;
        }

        historyChanges.removeLast();
    }
}