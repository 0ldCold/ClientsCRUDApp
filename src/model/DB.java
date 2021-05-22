package model;

import controller.Lib;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.entity.AccessLvl;
import model.entity.Table;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    String host;
    String login;
    String password;
    String driver;

    public void readProperty() {
        FileInputStream fis;
        Properties property = new Properties();
        try {
            fis = new FileInputStream("./Application.properties");
            property.load(fis);

            String host = property.getProperty("db.host");
            String login = property.getProperty("db.login");
            String password = property.getProperty("db.password");
            String driver = property.getProperty("db.driver");

            this.host = host;
            this.login = login;
            this.password = password;
            this.driver = driver;
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсутствует!");
        }
    }

    private Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(this.driver);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        try {
            dbConnection = DriverManager.getConnection(this.host, this.login,this.password);
            return dbConnection;
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    public ObservableList<Table> getAllUsers(){
        return getUsers("SELECT * FROM \"User\"");
    }
    public ObservableList<Table> getUsersWhereID(Long id){
        if(id==null) return getAllUsers();
        return getUsers("SELECT * FROM \"User\" WHERE id="+id);
    }

    //Заготовка на случай необходимости фильтрации в БД
    private ObservableList<Table> getUsers(String requestSQL) {
        ObservableList<Table> tableData = FXCollections.observableArrayList();
        ResultSet rs = BDRequestWithResponse(requestSQL);
        assert rs != null;
        try {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String login = rs.getString("login");
                String password = rs.getString("password");
                Long accessLvl = rs.getLong("accesslvl");
                LocalDateTime dateOfCreation = rs.getObject("dateofcreation", LocalDateTime.class);
                LocalDateTime dateOfModification = rs.getObject("dateofmodification", LocalDateTime.class);
                tableData.add(new Table(id, login, password, accessLvl, dateOfCreation, dateOfModification));
            }
            return tableData;
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    public ObservableList<AccessLvl> getAccessLvl() {
        ObservableList<AccessLvl> accessLvls = FXCollections.observableArrayList();
        ResultSet rs = BDRequestWithResponse("SELECT * FROM \"AccessLevel\";");
        assert rs != null;
        try {
            while (rs.next()) {
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                accessLvls.add(new AccessLvl(id, title));
            }
            return accessLvls;
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    public void deleteUser(Long id) {
        try {
            BDRequestWithoutResponse("DELETE FROM \"User\" WHERE id="+id);
        }catch (Exception e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    //Так много проверок для того, чтобы запросы (и логи) были более "верными".
    // Т.е. чтобы при изменении Логина в запросе было ТОЛЬКО изменение логина
    public void modificationUser(Long oldId,
                                 Long id,
                                 String login,
                                 String password,
                                 Long accessLvl,
                                 LocalDateTime dateOfCreation,
                                 LocalDateTime dateOfModification){
        assert oldId != null;
        String requestSQL = "UPDATE \"User\" SET ";
        if (id!=null) requestSQL+="id="+id+",";
        if (login!=null) requestSQL+="login='"+login+"',";
        if (password!=null) requestSQL+="\"password\"='"+password+"',";
        if (accessLvl!=null) requestSQL+="accesslvl="+accessLvl+",";
        if (dateOfCreation!=null) requestSQL+="dateofcreation='"+Timestamp.valueOf(dateOfCreation)+"',";
        if (dateOfModification!=null) requestSQL+="dateofmodification='"+Timestamp.valueOf(dateOfModification)+"',";
        requestSQL= Lib.removeLastChar(requestSQL);
        requestSQL+=" WHERE id="+oldId;
        try {
            BDRequestWithoutResponse(requestSQL);
        }catch (Exception e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void createUser(Long id,
                           String login,
                           String password,
                           Long accessLvl,
                           LocalDateTime dateOfCreation,
                           LocalDateTime dateOfModification){
        try {
            BDRequestWithoutResponse("INSERT INTO \"User\" VALUES ("+ id+", " +
                    "'"+login+"', " +
                    "'"+password+"', "+
                    accessLvl+", "+
                    "'"+Timestamp.valueOf(dateOfCreation)+"', "+
                    "'"+Timestamp.valueOf(dateOfModification)+"')");
        }catch (Exception e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private ResultSet BDRequestWithResponse(String requestSQL){
        Logger.getLogger(DB.class.getName()).log(Level.FINE, requestSQL);
        Connection dbConnection = null;
        Statement statement = null;
        try {
            dbConnection = getDBConnection();
            assert dbConnection != null;
            statement = dbConnection.createStatement();
            return statement.executeQuery(requestSQL);
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    private void BDRequestWithoutResponse(String requestSQL){
        Logger.getLogger(DB.class.getName()).log(Level.FINE, requestSQL);
        Connection dbConnection = null;
        Statement statement = null;
        try {
            dbConnection = getDBConnection();
            assert dbConnection != null;
            statement = dbConnection.createStatement();
            statement.execute(requestSQL);
        } catch (SQLException e) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }
    }
