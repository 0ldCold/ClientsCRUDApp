package model.entity;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;

public class Table {
    private final LongProperty id;
    private final StringProperty login;
    private final StringProperty password;
    private final LongProperty accessLvl;
    private final ObjectProperty<LocalDateTime> dateOfCreation;
    private final ObjectProperty<LocalDateTime> dateOfModification;

    public Table(
            Long id,
            String login,
            String password,
            Long accessLvl,
            LocalDateTime dateOfCreation,
            LocalDateTime dateOfModification
    ){
        this.id = new SimpleLongProperty(id);
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
        this.accessLvl = new SimpleLongProperty(accessLvl);
        this.dateOfCreation = new SimpleObjectProperty<LocalDateTime>(dateOfCreation);
        this.dateOfModification = new SimpleObjectProperty<LocalDateTime>(dateOfModification);
    }


    public Long getID() {
        return id.get();
    }
    public void setID(Long v){
        id.set(v);
    }
    public LongProperty idProperty() {
        return id;
    }

    public String getLogin() {
        return login.get();
    }
    public void setLogin(String v){
        login.set(v);
    }
    public StringProperty loginProperty() {
        return login;
    }

    public String getPassword() {
        return password.get();
    }
    public void setPassword(String v){
        password.set(v);
    }
    public StringProperty passwordProperty() {
        return password;
    }

    public Long getAccessLvl() {
        return accessLvl.get();
    }
    public void setAccessLvl(Long v){
        accessLvl.set(v);
    }
    public LongProperty accessLvlProperty() {
        return accessLvl;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation.get();
    }
    public void setDateOfCreation(LocalDateTime v){
        dateOfCreation.set(v);
    }
    public ObjectProperty<LocalDateTime> dateOfCreationProperty() {
        return dateOfCreation;
    }

    public LocalDateTime getDateOfModification() {
        return dateOfModification.get();
    }
    public void setDateOfModification(LocalDateTime v){
        dateOfModification.set(v);
    }
    public ObjectProperty<LocalDateTime> dateOfModificationProperty() {
        return dateOfModification;
    }
}
