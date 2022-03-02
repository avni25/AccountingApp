package common;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    public static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LoginWin.fxml"));
        window = primaryStage;
        window.setTitle("Login");
        window.setScene(new Scene(root,450, 300));
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
