import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import javax.swing.plaf.multi.MultiFileChooserUI;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserInterface {
    private Label label_ImAnno = new Label("data.Image Annotator");

    private Button button_CreateProj = new Button("Create project"),
                    button_OpenProj = new Button("Open project");

    private final double BUTTON_WIDTH = App.WINDOW_WIDTH/4;
    private final double BUTTON_BUFFER = BUTTON_WIDTH/10;
    private double createStart = 20;
    private double openStart = createStart + BUTTON_WIDTH + BUTTON_BUFFER;

    private String currentProj;

    private double projX = 10, projY = 10;
    private int projIndex = 0;

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
            tid.setTitle("data.Project name");
            tid.setHeaderText("Directory: " + selectedDir);
            tid.setContentText("Please enter a project name:");
            Optional<String> projectName = tid.showAndWait();
            projectName.ifPresent(name -> {
                System.out.println("Name: " + name);
                try {
                    System.out.println("Trying to create project");
                    currentProj = executor.createProject(selectedDir, name);
                    System.out.println("data.Project creation done - " + currentProj);
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

    private void addImageToProjectEditor(data.Image img, Pane pane) {
        projIndex++;
        Image tNail = null;
        try {
            tNail = new Image(new File(img.getPath()).toURI().toURL().toString(), App.WINDOW_WIDTH/4, App.WINDOW_HEIGHT/4, false, false);
            ImageView iView = new ImageView(tNail);

            iView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    createImageEditorWindow(img);
                }
            });

            iView.setLayoutX(projX);
            iView.setLayoutY(projY);
            pane.getChildren().add(iView);

            projX += BUTTON_WIDTH;
            if (projIndex % 3 == 0) {
                projY += BUTTON_WIDTH/4;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void showProject(Pane theRoot) {
        try {
            executor.openProject(new File(currentProj));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Pane projPane = new Pane();
        Stage projStage = new Stage();
        projStage.setTitle("Project Editor");
        Scene projScene = new Scene(projPane);

        List<ImageView> views = new ArrayList<>();

        try {
            for (data.Image img : executor.getImages()) {
                addImageToProjectEditor(img, projPane);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Button button_addImage = new Button("Add image"),
                button_addImages = new Button("Add images"),
                button_closeProj = new Button("Close");

        FileChooser fc = new FileChooser();
        fc.setTitle("Choose image(s)");
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg"),
                pngFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fc.getExtensionFilters().addAll(jpgFilter, pngFilter);

        button_addImage.setOnAction(event -> {
            File img = fc.showOpenDialog(projStage);
            // Add image here
            try {
                executor.addImage(img);
                addImageToProjectEditor(new data.Image(img), projPane);
            } catch (IllegalAccessException | IOException e) {
                e.printStackTrace();
            }
        });
        button_addImages.setOnAction(event -> {
            List<File> images = fc.showOpenMultipleDialog(projStage);
            // Add images here
            for (File img: images) {
                try {
                    executor.addImage(img);
                    addImageToProjectEditor(new data.Image(img), projPane);
                } catch (IllegalAccessException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        button_closeProj.setOnAction(event -> {
            projStage.close();
        });

        setupButtonUI(button_addImage, "Arial", 15, BUTTON_WIDTH, Pos.CENTER, createStart, 0.75 * App.WINDOW_HEIGHT);
        setupButtonUI(button_addImages, "Arial", 15, BUTTON_WIDTH, Pos.CENTER, createStart + BUTTON_WIDTH + BUTTON_BUFFER, 0.75 * App.WINDOW_HEIGHT);
        setupButtonUI(button_closeProj, "Arial", 15, BUTTON_WIDTH, Pos.CENTER, createStart + 2 * BUTTON_WIDTH + 2 * BUTTON_BUFFER, 0.75 * App.WINDOW_HEIGHT);

        projPane.getChildren().addAll(button_addImage, button_addImages, button_closeProj);
        projStage.setScene(projScene);
        projStage.showAndWait();

//        try {
//
//            if (executor.getNumImages() > 0) {
//
//        } catch (IOException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
    }

    private void createImageEditorWindow(data.Image img) {
        Pane imagePane = new Pane();

        Canvas canvas = new Canvas(App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
        Image image = null;
        try {
            image = new Image(new File(img.getPath()).toURI().toURL().toString());

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
                ObservableList<String> values = FXCollections.observableArrayList(
                        "Bike",
                        "Auto Rickshaw",
                        "Car",
                        "Bus"
                );
                ComboBox<String> choose = new ComboBox<>(values);
                Button set = new Button("Set"),
                        cancel = new Button("Cancel");
                setupComboBoxUI(choose, 10, 10, App.WINDOW_WIDTH/10);
                setupButtonUI(set, "Arial", 10, App.WINDOW_WIDTH/15, Pos.CENTER, 10 + App.WINDOW_WIDTH/15 + App.WINDOW_WIDTH/20, 10);
                setupButtonUI(cancel, "Arial", 10, App.WINDOW_WIDTH/15, Pos.CENTER, 10 + 2*App.WINDOW_WIDTH/15 + 2*App.WINDOW_WIDTH/20, 10);

                Pane comboBoxPane = new Pane();
                comboBoxPane.getChildren().addAll(choose, set, cancel);
                Scene comboBoxScene = new Scene(comboBoxPane, 20 + 3*App.WINDOW_WIDTH/15 + 2*App.WINDOW_WIDTH/20, 40);
                Stage comboBoxStage = new Stage();
                comboBoxStage.setTitle("Select data.Annotation");
                comboBoxStage.setScene(comboBoxScene);

                set.setOnAction(click -> {
                    comboBoxStage.close();
                    if (choose.getSelectionModel().getSelectedIndex() != -1) {
                        // Add annotations to image
                        executor.setAnnotation(0, dragBox.getX(), dragBox.getY(), dragBox.getWidth(), dragBox.getHeight(), choose.getSelectionModel().getSelectedIndex());
                    }
                });
                cancel.setOnAction(click -> {
                    choose.getSelectionModel().select(-1);
                    comboBoxStage.close();
                });

                System.out.println(choose.getSelectionModel().getSelectedIndex());
                comboBoxStage.showAndWait();
                System.out.println(choose.getSelectionModel().getSelectedIndex());

                dragBox.setVisible(false);
                dragStartX[0] = 0;
                dragStartY[0] = 0;
            });

            imagePane.getChildren().add(canvas);
            imagePane.getChildren().add(dragBox);

            Scene imageScene = new Scene(imagePane, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
            Stage imageStage = new Stage();
            imageStage.setTitle("data.Image");
            imageStage.setScene(imageScene);
            imageStage.show();
        } catch (MalformedURLException e) {
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

    /**********
     * Private local method to initialize the standard fields for a text field
     */
    private void setupComboBoxUI(ComboBox<String> list, double x, double y, double w) {
        list.setMinWidth(w);
        list.setMaxWidth(w);
        list.setLayoutX(x);
        list.setLayoutY(y);
    }
}
