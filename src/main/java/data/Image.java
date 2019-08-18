package main.java.data;

import java.io.File;
import java.util.ArrayList;

public class Image {
    private String path, csvFilePath;
    private ArrayList<Annotation> annotations;

    public Image() {
        path = "";
        csvFilePath = "";
        annotations = new ArrayList<>();
    }

    public Image(File file) {
        String name = file.getName();
        String csvName = new File(file.getParentFile().getAbsolutePath(), name + ".csv").getAbsolutePath();
        init(file.getAbsolutePath(), csvName);
    }

    private void init(String path, String csvFilePath) {
        this.path = path;
        this.csvFilePath = csvFilePath;
        annotations = new ArrayList<>();
    }

    public String getPath() {
        return path;
    }

    public ArrayList<Annotation> getAnnotations() {
        return annotations;
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAnnotations(ArrayList<Annotation> annotations) {
        this.annotations = annotations;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public void addAnnotation(int index, double x, double y, double w, double h) {
        annotations.add(new Annotation(index, x, y, w, h));
    }
}
