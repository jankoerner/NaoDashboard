package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Main extends Application {
    Parent root;
    Scene scene;
    @FXML
    AnchorPane anchor;
    @Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("NaoDashboard");
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        // Platz der Buttons soll zur Laufzeit berechnet werden:
        // old X Position/1366 = new X Position/primaryScreenBounds.getMinX()
        // also: newXPosition = (oldXPosition*primaryScreenBounds.getMinX)/1366
        scene = new Scene(root,1080,720);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.getIcons().add(new Image("file:/home/vl/IdeaProjects/NaoDashboard/default_app.png"));


    }

    public static void main(String[] args) {
        launch(args);
    }
}
