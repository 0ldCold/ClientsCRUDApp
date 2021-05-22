package controller;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.entity.AccessLvl;
import model.entity.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController {

    // actualList - все пользователи
    // filteredList - отфильтрованный, берет данные из actualList
    // sortedList - сортированный. FilteredList нельзя сортировать, поэтому filteredList "упаковывается" в SortedList
    // в таблице отображается sortedList
    private ObservableList<Table> actualList;
    private FilteredList<Table> filteredList;
    private SortedList<Table> sortedList;

    public Stage primaryStage;

    public Table selectTableLine;

    public MainController(){}

    @FXML
    TableView<Table> TableView;

    @FXML
    TableColumn<Table, Long> TableColumnID;
    @FXML
    TableColumn<Table, String> TableColumnLogin;
    @FXML
    TableColumn<Table, String> TableColumnPassword;
    @FXML
    TableColumn<Table, Long> TableColumnAccessLvl;
    @FXML
    TableColumn<Table, LocalDateTime> TableColumnDateOfCreation;
    @FXML
    TableColumn<Table, LocalDateTime> TableColumnDateOfModification;

    @FXML
    Label LabelTableValueAll;
    @FXML
    Label LabelTableValueNow;

    @FXML
    TextField TextFieldSearchID;
    @FXML
    TextField TextFieldSearchLogin;
    @FXML
    ComboBox ComboBoxSearchAccessLevel;

    @FXML
    private void initialize(){
        TableColumnID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        TableColumnLogin.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        TableColumnPassword.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        TableColumnAccessLvl.setCellValueFactory(cellData -> cellData.getValue().accessLvlProperty().asObject());
        TableColumnDateOfCreation.setCellValueFactory(cellData -> cellData.getValue().dateOfCreationProperty());
        TableColumnDateOfModification.setCellValueFactory(cellData -> cellData.getValue().dateOfModificationProperty());

        //заполнение таблицы данными
        updateTableData();

        updateLabelTableValueAll();
        updateLabelTableValueNow();

        //Добавление данных в выпадающий список
        ObservableList<AccessLvl> list = Main.db.getAccessLvl();
        list.add(new AccessLvl()); //пустое значение для фильтрации
        ComboBoxSearchAccessLevel.setItems(list);
        ComboBoxSearchAccessLevel.getSelectionModel().select(ComboBoxSearchAccessLevel.getItems().size()-1);


        //Изменение формата отображения даты в таблице
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumnDateOfCreation.setCellFactory(col -> new TableCell<Table, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {

                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(String.format(item.format(formatter)));
            }
        });
        TableColumnDateOfModification.setCellFactory(col -> new TableCell<Table, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {

                super.updateItem(item, empty);
                if (empty)
                    setText(null);
                else
                    setText(String.format(item.format(formatter)));
            }
        });

        TableView.getSelectionModel().selectedItemProperty().addListener(actionEvent ->  {
            selectTableLine = TableView.getSelectionModel().getSelectedItem();
        });
    }


    public void updateTableData(){
        actualList = Main.db.getAllUsers();
        filteredList = new FilteredList<Table>(actualList);
        sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(TableView.comparatorProperty());
        TableView.setItems(sortedList);


        TextFieldSearchID.setText("");
        TextFieldSearchLogin.setText("");
        ComboBoxSearchAccessLevel.getSelectionModel().select(ComboBoxSearchAccessLevel.getItems().size()-1);
    }

    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }

    private DialogController openDialogWindow(String title){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/dialog.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();

            DialogController dialogController = loader.getController();
            dialogController.mainController = this;
            dialogController.selectTableLine = selectTableLine;

            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();
            dialogController.stage = stage;
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
            return dialogController;
        } catch (Exception e){
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public void updateLabelTableValueAll(){
        LabelTableValueAll.setText(String.valueOf(TableView.getItems().size()));
    }
    public void updateLabelTableValueNow(){
        LabelTableValueNow.setText(String.valueOf(TableView.getItems().size()));
    }

    @FXML
    private void handleButtonCreate(){
        DialogController dialogController = openDialogWindow("Создать Пользователя");
        assert dialogController != null;
        dialogController.buttonOkFunc = "Create";
        updateLabelTableValueAll();
        updateLabelTableValueNow();
    }

    @FXML
    private void handleButtonDelete(){
        if(selectTableLine==null) return;
        Main.db.deleteUser(selectTableLine.getID());
        TableView.getSelectionModel().clearSelection();
        updateTableData();
        updateLabelTableValueAll();
        updateLabelTableValueNow();
    }

    @FXML
    private void handleButtonModification(){
        if(selectTableLine==null) return;
        DialogController dialogController = openDialogWindow("Изменить Пользователя");
        assert dialogController != null;

        dialogController.TextFieldID.setText(selectTableLine.getID().toString());
        dialogController.TextFieldLogin.setText(selectTableLine.getLogin());
        dialogController.TextFieldPassword.setText(selectTableLine.getPassword());
        dialogController.ComboBoxAccessLvl.getSelectionModel().select(selectTableLine.getAccessLvl().intValue()-1);
        dialogController.DatePickerDateOfCreation.setDateTimeValue(selectTableLine.getDateOfCreation());
        dialogController.DatePickerDateOfModification.setDateTimeValue(selectTableLine.getDateOfModification());
        dialogController.selectTableLine = selectTableLine;
        TableView.getSelectionModel().clearSelection();

        dialogController.buttonOkFunc = "Modification";

    }

    @FXML
    private void handleButtonClose(){
        primaryStage.close();
    }

    @FXML
    private void handleTextFieldSearchID(){
        filter(filteredList);
        updateLabelTableValueNow();
    }

    @FXML
    private void handleTextFieldSearchLogin(){
        filter(filteredList);
        updateLabelTableValueNow();
    }

    @FXML
    private void handleTextComboBoxSearchAccessLevel(){
        filter(filteredList);
        updateLabelTableValueNow();
    }

    private void filter(FilteredList<Table> filteredList){
        String filterID = TextFieldSearchID.getText();
        String filterLogin = TextFieldSearchLogin.getText();
        AccessLvl accessLvl = (AccessLvl) ComboBoxSearchAccessLevel.getSelectionModel().getSelectedItem();
        Long filterAccessLvl = accessLvl.getId();

        //если фильтры пустые - отображать всех пользователей
        if((filterID == null || filterID.isEmpty())
                && (filterLogin == null || filterLogin.isEmpty())
                && (filterAccessLvl == null))
            filteredList.setPredicate(Table -> true);
        else {
            filteredList.setPredicate(Table -> {
                if (filterID != null && Lib.isNumber(filterID))
                    if (!Table.getID().equals(Long.parseLong(filterID, 10)))
                        return false;
                if (filterLogin != null && filterLogin.length() > 0)
                    if (!Table.getLogin().contains(filterLogin))
                        return false;
                if (filterAccessLvl != null)
                    if (!Table.getAccessLvl().equals(filterAccessLvl))
                        return false;
                return true;
            });
        }
    }
}
