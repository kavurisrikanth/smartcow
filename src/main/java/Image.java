import java.util.ArrayList;

public class Image {
    private String path;
    private ArrayList<Annotation> annotations;

    public Image(String path) {
        this.path = path;
        annotations = new ArrayList<>();
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

    public void addAnnotation(int index, double x, double y, double w, double h) {
        if (index < 0 || index > AnnotationValue.values().length)
            return;

        annotations.add(new Annotation(x, y, w, h));
    }
}
