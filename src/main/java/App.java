package main.java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
    public final static double WINDOW_WIDTH = 900;
    public final static double WINDOW_HEIGHT = 600;

    public UserInterface theGUI;

    @Override
    public void start(Stage theStage) throws Exception {
        theStage.setTitle("imAnno");

        Pane theRoot = new Pane();

        theGUI = new UserInterface(theRoot);

        Scene theScene = new Scene(theRoot, WINDOW_WIDTH, WINDOW_HEIGHT);

        theStage.setScene(theScene);

        theStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
