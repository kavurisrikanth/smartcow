package main.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.java.data.Annotation;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserInterface {

    private final double BUTTON_WIDTH = App.WINDOW_WIDTH/4;
    private final double BUTTON_BUFFER = BUTTON_WIDTH/10;
    private double createStart = 20;
    private double openStart = createStart + BUTTON_WIDTH + BUTTON_BUFFER;

    private String currentProj;

    private int projX = 0, projY = 0;

    private final double IMAGE_WIDTH = (App.WINDOW_WIDTH - 40)/3;

    private BusinessLogic executor = new BusinessLogic();

    public UserInterface(Pane theRoot) {
        Label label_ImAnno = new Label("Image Annotator");
        setupLabelUI(label_ImAnno, "Arial", 28, App.WINDOW_WIDTH, Pos.CENTER, 0, 10);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 10, 10, 10));

        Button button_CreateProj = new Button("Create project");
        setupButtonUI(button_CreateProj, "Arial", 15, BUTTON_WIDTH);
        button_CreateProj.setOnAction((event) -> {
            createProject(theRoot);
        });

        Button button_OpenProj = new Button("Open project");
        setupButtonUI(button_OpenProj, "Arial", 15, BUTTON_WIDTH);
        button_OpenProj.setOnAction((event) -> {
            openProject(theRoot);
        });

        VBox container = new VBox();
        container.setSpacing(10);
        container.setPadding(new Insets(10, 10, 10, 10));

        hBox.getChildren().addAll(button_CreateProj, button_OpenProj);
        container.getChildren().addAll(label_ImAnno, hBox);
        theRoot.getChildren().add(container);
    }

    private void createProject(Pane theRoot) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose project location");
        File selectedDir = dc.showDialog(theRoot.getScene().getWindow());
        if (selectedDir != null) {
            TextInputDialog tid = new TextInputDialog("default");
            tid.setTitle("Project name");
            tid.setHeaderText("Directory: " + selectedDir);
            tid.setContentText("Please enter a project name:");
            Optional<String> projectName = tid.showAndWait();
            projectName.ifPresent(name -> {
                try {
                    currentProj = executor.createProject(selectedDir, name);
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
        if (selectedFile != null) {
            currentProj = selectedFile.getAbsolutePath();
            showProject(theRoot);
        }
    }

    private void addImageToProjectEditor(main.java.data.Image img, GridPane pane) {
        Image tNail = null;
        try {
            tNail = new Image(new File(img.getPath()).toURI().toURL().toString(), IMAGE_WIDTH, App.WINDOW_HEIGHT/4, false, true);
            ImageView iView = new ImageView(tNail);

            iView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    createImageEditorWindow(img);
                }
            });

            pane.add(iView, projX++, projY);
            if (projX == 3) {
                projY++;
                projX = 0;
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

        projX = 0;
        projY = 0;

        Pane projPane = new Pane();
        Stage projStage = new Stage();
        projStage.setTitle("Project Editor - " + currentProj);
        Scene projScene = new Scene(projPane);

        HBox buttonsBox = new HBox();
        buttonsBox.setPadding(new Insets(10, 10, 10, 10));
        buttonsBox.setSpacing(10);

        VBox container = new VBox();
        container.setPadding(new Insets(10, 10, 10, 10));
        container.setSpacing(10);

        GridPane sPane = new GridPane();
        sPane.setMinWidth(App.WINDOW_WIDTH - 20);
        sPane.setMinHeight(App.WINDOW_HEIGHT - 60);
        sPane.setPadding(new Insets(10, 10, 10, 10));
        sPane.setVgap(5);
        sPane.setHgap(5);
        sPane.setAlignment(Pos.CENTER);

        try {
            for (main.java.data.Image img : executor.getImages()) {
                addImageToProjectEditor(img, sPane);
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
                if (img != null) {
                    executor.addImage(img);
                    addImageToProjectEditor(new main.java.data.Image(img), sPane);
                }
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
                    addImageToProjectEditor(new main.java.data.Image(img), sPane);
                } catch (IllegalAccessException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        button_closeProj.setOnAction(event -> {
            projStage.close();
        });

        setupButtonUI(button_addImage, "Arial", 15, BUTTON_WIDTH);
        setupButtonUI(button_addImages, "Arial", 15, BUTTON_WIDTH);
        setupButtonUI(button_closeProj, "Arial", 15, BUTTON_WIDTH);

        buttonsBox.getChildren().addAll(button_addImage, button_addImages, button_closeProj);
        container.getChildren().addAll(buttonsBox, sPane);

        projPane.getChildren().addAll(container);
        projStage.setScene(projScene);
        projStage.showAndWait();
    }

    private void createImageEditorWindow(main.java.data.Image img) {
        Pane imagePane = new Pane();

        Canvas canvas = new Canvas(App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
        Image image = null;
        try {
            image = new Image(new File(img.getPath()).toURI().toURL().toString());

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.drawImage(image, 0, 0,canvas.getWidth(), canvas.getHeight());

            List<Rectangle> existingAnnos = new ArrayList<>();
            for (Annotation a: img.getAnnotations()) {
                Rectangle cur = new Rectangle(a.getX(), a.getY(), a.getW(), a.getH());
                cur.setVisible(true);
                cur.setStroke(Color.BLACK);
                cur.setStrokeWidth(1.0);
                cur.setFill(Color.TRANSPARENT);
                Tooltip.install(cur, new Tooltip(a.getName()));
                existingAnnos.add(cur);
            }

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
                ObservableList<String> values = FXCollections.observableArrayList(
                        "Bike",
                        "Autorickshaw",
                        "Car",
                        "Bus"
                );

                HBox hBox = new HBox();
                hBox.setSpacing(5);
                hBox.setPadding(new Insets(10, 10, 10, 10));

                ComboBox<String> choose = new ComboBox<>(values);
                Button set = new Button("Set"),
                        cancel = new Button("Cancel");
                setupComboBoxUI(choose, App.WINDOW_WIDTH/10);
                setupButtonUI(set, "Arial", 10, App.WINDOW_WIDTH/15);
                setupButtonUI(cancel, "Arial", 10, App.WINDOW_WIDTH/15);

                hBox.getChildren().addAll(choose, set, cancel);

                Pane comboBoxPane = new Pane();
                comboBoxPane.getChildren().add(hBox);
                Scene comboBoxScene = new Scene(comboBoxPane, 20 + 3*App.WINDOW_WIDTH/15 + 2*App.WINDOW_WIDTH/20, 40);
                Stage comboBoxStage = new Stage();
                comboBoxStage.setTitle("Select Annotation");
                comboBoxStage.setScene(comboBoxScene);

                set.setOnAction(click -> {
                    comboBoxStage.close();
                    if (choose.getSelectionModel().getSelectedIndex() != -1) {
                        // Add annotations to image
                        try {
                            executor.setAnnotation(img, dragBox.getX(), dragBox.getY(), dragBox.getWidth(), dragBox.getHeight(), choose.getSelectionModel().getSelectedIndex());
                            Rectangle cur = new Rectangle(dragBox.getX(), dragBox.getY(), dragBox.getWidth(), dragBox.getHeight());
                            cur.setVisible(true);
                            cur.setStroke(Color.BLACK);
                            cur.setStrokeWidth(1.0);
                            cur.setFill(Color.TRANSPARENT);
                            Tooltip.install(cur, new Tooltip(choose.getSelectionModel().getSelectedItem()));
                            imagePane.getChildren().add(cur);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                cancel.setOnAction(click -> {
                    choose.getSelectionModel().select(-1);
                    comboBoxStage.close();
                });

                comboBoxStage.showAndWait();

                dragBox.setVisible(false);
                dragStartX[0] = 0;
                dragStartY[0] = 0;
            });

            imagePane.getChildren().add(canvas);
            imagePane.getChildren().add(dragBox);
            imagePane.getChildren().addAll(existingAnnos);

            Scene imageScene = new Scene(imagePane, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
            Stage imageStage = new Stage();
            imageStage.setTitle("main.java.data.Image");
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

    private void setupButtonUI(Button b, String ff, double f, double w) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
    }

    /**********
     * Private local method to initialize the standard fields for a text field
     */
    private void setupComboBoxUI(ComboBox<String> list, double w) {
        list.setMinWidth(w);
        list.setMaxWidth(w);
    }
}
