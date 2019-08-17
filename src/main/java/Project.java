import java.util.ArrayList;

public class Project {
    private String name = "";
    private ArrayList<Image> images = new ArrayList<>();

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "Project: " + name + ", contains " + images.size() + " images.";
    }
}
