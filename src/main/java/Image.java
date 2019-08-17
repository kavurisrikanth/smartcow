import java.util.ArrayList;

public class Image {
    private String path;
    private ArrayList<Annotation> annotations;

    public Image(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public ArrayList<Annotation> getAnnotations() {
        return annotations;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAnnotations(ArrayList<Annotation> annotations) {
        this.annotations = annotations;
    }
}
