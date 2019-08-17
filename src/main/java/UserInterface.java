import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class UserInterface {
    private Label label_ImAnno = new Label("Image Annotator");

    private Button button_CreateProj = new Button("Create project"),
                    button_OpenProj = new Button("Open project");

    private final double BUTTON_WIDTH = App.WINDOW_WIDTH/4;
    private final double BUTTON_BUFFER = BUTTON_WIDTH/10;
    private double createStart = 20;
    private double openStart = createStart + BUTTON_WIDTH + BUTTON_BUFFER;

    private String currentProj;

    private BusinessLogic executor = new BusinessLogic();

    public UserInterface(Pane theRoot) {
        setupLabelUI(label_ImAnno, "Arial", 28, App.WINDOW_WIDTH, Pos.CENTER, 0, 10);

        setupButtonUI(button_CreateProj, "Arial", 15, BUTTON_WIDTH, Pos.CENTER, createStart, 0.75 * App.WINDOW_HEIGHT);
        button_CreateProj.setOnAction((event) -> {
            createProject(theRoot);
        });

        setupButtonUI(button_OpenProj, "Arial", 15, BUTTON_WIDTH, Pos.CENTER, openStart, 0.75 * App.WINDOW_HEIGHT);
        button_OpenProj.setOnAction((event) -> {
            openProject(theRoot);
        });

        theRoot.getChildren().addAll(label_ImAnno, button_CreateProj, button_OpenProj);
    }

    private void createProject(Pane theRoot) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose project location");
        File selectedDir = dc.showDialog(theRoot.getScene().getWindow());
        if (selectedDir != null) {
            System.out.println("Selected: " + selectedDir);
            TextInputDialog tid = new TextInputDialog("defaultProject.json");
            tid.setTitle("Project name");
            tid.setHeaderText("Directory: " + selectedDir);
            tid.setContentText("Please enter a project name:");
            Optional<String> projectName = tid.showAndWait();
            projectName.ifPresent(name -> {
                System.out.println("Name: " + name);
                try {
                    System.out.println("Trying to create project");
                    currentProj = executor.createProject(selectedDir, name);
                    System.out.println("Project creation done - " + currentProj);
                    showProject(theRoot);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void openProject(Pane theRoot) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose project location");
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fc.getExtensionFilters().add(jsonFilter);
        File selectedFile = fc.showOpenDialog(theRoot.getScene().getWindow());
        System.out.println("Selected: " + selectedFile);
        if (selectedFile != null) {
            currentProj = selectedFile.getAbsolutePath();
            showProject(theRoot);
        }
    }

    private void showProject(Pane theRoot) {
        try {
            executor.openProject(new File(currentProj));
            if (executor.getNumImages() > 0) {
                Pane imagePane = new Pane();

                Canvas canvas = new Canvas(App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
                Image image = new Image(new File(executor.getImages().get(0).getPath()).toURI().toURL().toString());

                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.drawImage(image, 0, 0,canvas.getWidth(), canvas.getHeight());
                Rectangle dragBox = new Rectangle(0,0,0,0);
                dragBox.setStroke(Color.BLACK);
                dragBox.setStrokeWidth(1.0);
                dragBox.setFill(Color.TRANSPARENT);
                final double[] dragStartX = new double[1];
                final double[] dragStartY = new double[1];

                canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                    dragStartX[0] = event.getSceneX();
                    dragStartY[0] = event.getSceneY();
                    dragBox.setX(dragStartX[0]);
                    dragBox.setY(dragStartY[0]);
                    dragBox.setWidth(0);
                    dragBox.setHeight(0);
                    dragBox.setVisible(true);
                });

                canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
                    dragBox.setWidth(event.getSceneX() - dragStartX[0]);
                    dragBox.setHeight(event.getSceneY() - dragStartY[0]);
                });

                canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
                    System.out.println(dragBox);
                    ObservableList<String> values = FXCollections.observableArrayList("Bike", "Auto Rickshaw", "Car", "Bus");
                    ComboBox<String> choose = new ComboBox<>(values);

                    dragBox.setVisible(false);
                    dragStartX[0] = 0;
                    dragStartY[0] = 0;
                });

                imagePane.getChildren().add(canvas);
                imagePane.getChildren().add(dragBox);

                Scene imageScene = new Scene(imagePane, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
                Stage imageStage = new Stage();
                imageStage.setTitle("Image");
                imageStage.setScene(imageScene);
                imageStage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**********
     * Private local method to initialize the standard fields for a label
     */
    private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }
}
