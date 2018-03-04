package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
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
import javafx.stage.WindowEvent;

import java.awt.*;

public class Main extends Application {
    Parent root;
    Scene scene;
    @FXML  AnchorPane anchor;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        root = loader.load();
        Controller controller = (Controller) loader.getController(); //holt controller des fxml
        primaryStage.setTitle("NaoDashboard");
        // Platz der Buttons soll zur Laufzeit berechnet werden:
        // old X Position/1366 = new X Position/primaryScreenBounds.getMinX()
        // also: newXPosition = (oldXPosition*primaryScreenBounds.getMinX)/1366
        scene = new Scene(root,1080,720);
        primaryStage.setScene(scene);
        primaryStage.show();
        controller.setStageAndSetupGuiListeners(primaryStage); // NACHDEM(!) die stage angezeigt wird wird sie an den controller übergeben um dort width&height listener hinzuzufügen die die elemente in abhängigkeit der fenstergröße verschieben(und evtl skalieren) sollen
        primaryStage.getIcons().add(new Image("file:./default_app.png")); // fügt ein icon hinzu, nicht sicher ob das als jar auch funktioniert
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();                       // verhindert, dass das event (stageclose) ausgeführt wird
                if(Controller.session!=null&&Controller.session.isConnected()) Controller.log.write("Please disconnect before exiting. WARN");
                else System.exit(0);
                //wenn hier die methode aus dem controller, sich zu disconnecten aufgerufen wird
                //sind die ganzen elemente aus dem fxml null weswegen es zu einem fehler kommt
                //deswegen einfach meldung an den nutzer dass man disconnecten sollte
                // System.exit um alle threads zu killen
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
