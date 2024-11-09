package managesystem.user;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RunningFirst extends Application {
    private double x = 0;
    private double y = 0;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunningFirst.class.getResource("loginClient.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}