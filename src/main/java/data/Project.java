package main.java.data;

import java.util.ArrayList;

public class Project {
    private String name = "";
    private ArrayList<Image> images = new ArrayList<>();
    private String filePath = "";
    private String csvFilePath = "";

    public void addImage(Image i) {
        images.add(i);
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    @Override
    public String toString() {
        return "data.Project: " + name + ", contains " + images.size() + " images.";
    }
}
