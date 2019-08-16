import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

public class UserInterface {
    private Label label_ImAnno = new Label("Image Annotator");

    private Button button_CreateProj = new Button("Create project"),
                    button_OpenProj = new Button("Open project");

    private final double BUTTON_WIDTH = App.WINDOW_WIDTH/4;
    private final double BUTTON_BUFFER = BUTTON_WIDTH/10;
    private double createStart = 20;
    private double openStart = createStart + BUTTON_WIDTH + BUTTON_BUFFER;

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
                executor.createProject(selectedDir, name);
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
