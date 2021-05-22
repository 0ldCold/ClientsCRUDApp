package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DB;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    public static DB db;
    public static MainController mainController;
    public static DialogController dialogController;

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            // подключение БД
            db = new DB();
            db.readProperty();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("../view/sample.fxml"));
            VBox vBox = (VBox) loader.load();

            mainController = new MainController();
            mainController = loader.getController();
            mainController.setPrimaryStage(primaryStage);

            Scene scene = new Scene(vBox);
            primaryStage.setScene(scene);
            primaryStage.setTitle("ClientsCRUDApp");
            primaryStage.show();
        } catch (Exception e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
