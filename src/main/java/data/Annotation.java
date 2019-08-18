package data;

public class Annotation {
    private double x, y;
    private double w, h;
    private String name;

    public Annotation() {
        this(-1, 0,0,0,0);
    }

    public Annotation(int index, double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        if (index >= 0 && index < AnnotationValue.values().length)
            this.name = AnnotationValue.values()[index].name();
        else
            this.name = "";
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }
}
