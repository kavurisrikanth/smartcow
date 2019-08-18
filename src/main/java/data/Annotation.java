package data;

public class Annotation {
    private double x, y;
    private double w, h;

    public Annotation() {
        this(0,0,0,0);
    }

    public Annotation(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
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
