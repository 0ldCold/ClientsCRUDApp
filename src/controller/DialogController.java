package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.entity.AccessLvl;
import model.entity.Table;
import tornadofx.control.DateTimePicker;
import java.time.LocalDateTime;

public class DialogController {
    public Stage stage;
    public MainController mainController;

    // Create или Modification
    // выбор функции для кнопки - создание или изменение пользователя
    public String buttonOkFunc = null;

    public Table selectTableLine;

    @FXML
    public Label LabelHeading;

    @FXML
    public TextField TextFieldID;
    public TextField TextFieldLogin;
    public TextField TextFieldPassword;
    public ComboBox ComboBoxAccessLvl;
    public DateTimePicker DatePickerDateOfCreation;
    public DateTimePicker DatePickerDateOfModification;
    public Button ButtonClose;
    public Button ButtonOK;

    public DialogController(){}

    @FXML
    private void handleButtonClose(){
        this.stage.close();
    }

    @FXML
    public void handleButtonOK(){
        if (dataIsEmpty(TextFieldID.getText(), TextFieldLogin.getText(), TextFieldPassword.getText()))
            return;

        if(!Lib.isNumber(TextFieldID.getText())){
            TextFieldID.setText(null);
            return;
        }
        Long id = Long.parseLong(TextFieldID.getText(), 10);
        String login = TextFieldLogin.getText();
        String password = TextFieldPassword.getText();
        AccessLvl accessLvl = (AccessLvl) ComboBoxAccessLvl.getSelectionModel().getSelectedItem();
        Long accessLvlId = accessLvl.getId();
        LocalDateTime dateOfCreation = DatePickerDateOfCreation.getDateTimeValue();
        LocalDateTime dateOfModification = DatePickerDateOfModification.getDateTimeValue();

        if(buttonOkFunc.equals("Create")){
            if(Main.db.getUsersWhereID(id).isEmpty()){
                Main.db.createUser(id, login, password, accessLvlId, dateOfCreation, dateOfModification);
            }
            else{
                TextFieldID.setText("");
                return;
            }
        }
        if(buttonOkFunc.equals("Modification")) {
            if(id.equals(selectTableLine.getID())) id = null;
            if(login.equals(selectTableLine.getLogin())) login = null;
            if(password.equals(selectTableLine.getPassword())) password = null;
            if(accessLvlId.equals(selectTableLine.getAccessLvl())) accessLvlId = null;
            if(dateOfCreation.equals(selectTableLine.getDateOfCreation())) dateOfCreation = null;
            if(dateOfModification.equals(selectTableLine.getDateOfModification())) dateOfModification = null;
            if(id!=null || login!=null || password!=null || accessLvlId!=null || dateOfCreation!=null || dateOfModification!=null){
                Main.db.modificationUser(
                        selectTableLine.getID(),
                        id,
                        login,
                        password,
                        accessLvlId,
                        dateOfCreation,
                        dateOfModification);

            }
        };
        mainController.updateTableData();
        this.stage.close();
    }

    private boolean dataIsEmpty(String id, String login, String password){
        int i = 0;
        TextFieldID.setStyle("-fx-control-inner-background: rgb(255,255,255);");
        TextFieldLogin.setStyle("-fx-control-inner-background: rgb(255,255,255);");
        TextFieldPassword.setStyle("-fx-control-inner-background: rgb(255,255,255);");

        if (id.isEmpty()){
            TextFieldID.setStyle("-fx-control-inner-background: rgb(255,213,213);");
            i++;
        }
        if (login.isEmpty()){
            TextFieldLogin.setStyle("-fx-control-inner-background: rgb(255,213,213);");
            i++;
        }
        if (password.isEmpty()){
            TextFieldPassword.setStyle("-fx-control-inner-background: rgb(255,213,213);");
            i++;
        }
        if (i>0) return true;
        return false;
    }

    @FXML
    private void initialize(){
        DatePickerDateOfCreation.setFormat("yyyy-MM-dd HH:mm:ss");
        DatePickerDateOfModification.setFormat("yyyy-MM-dd HH:mm:ss");
        ObservableList<AccessLvl> list = Main.db.getAccessLvl();
        ComboBoxAccessLvl.setItems(list);
        ComboBoxAccessLvl.getSelectionModel().select(2);
    }
}
