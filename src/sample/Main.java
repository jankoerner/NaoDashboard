package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    Parent root;
    Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("NaoDashboard");
        scene = new Scene(root,720,720);
        primaryStage.setScene(scene);
        primaryStage.show();
        Controller controller = new Controller();
        MoveBodyModel moveBodyModel = new MoveBodyModel();

        //Logik für Tastatureingabe
        //Das KeyListener geht, allerdings gibts es einen Fehler -
        //Wenn die Taste zu lang gedrückt wird, kommt eine
        //grosse Menge KEY_PRESSED events an, und das Programm
        //hängt sich kurz auf.
        //Mindestens bewegt sich jetzt den Kopf, laufen kann auch sicherlich
        //leicht über das KeyListener gemacht werden.

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                //logic here

                String keyStroke = event.getText();
                    try {
                        switch (event.getCode()) {
                            case I:
                                controller.moveBody(keyStroke);
                                break;
                            case K:
                                controller.moveBody(keyStroke);
                                break;
                            case J:
                                controller.moveBody(keyStroke);
                                break;
                            case L:
                                controller.moveBody(keyStroke);
                                break;
                            case M:
                                controller.moveBody(keyStroke);
                                //Die Implimentierung von laufen funktioniert noch nicht ganz. Das GUI muss
                                // geändert werden, sodass es moveBody (also die neue Methode) annimmt.
                                //Wie es gerade ist, wird das Programm nicht hochfahren. Es hängt sich immer an der
                                //Linie 17 im Main().
                            case W:
                                controller.moveBody(keyStroke);
                            case A:
                                controller.moveBody(keyStroke);
                            case S:
                                controller.moveBody(keyStroke);
                            case D:
                                controller.moveBody(keyStroke);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getEventType().equals(event.KEY_RELEASED)) {
                    try {
                        controller.moveBody("x");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }




    public static void main(String[] args) {
        launch(args);
    }
}
