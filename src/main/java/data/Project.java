package data;

import data.Image;

import java.util.ArrayList;

public class Project {
    private String name = "";
    private ArrayList<Image> images = new ArrayList<>();
    private String filePath = "";

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

    public String getName() {
        return name;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public String toString() {
        return "data.Project: " + name + ", contains " + images.size() + " images.";
    }
}
