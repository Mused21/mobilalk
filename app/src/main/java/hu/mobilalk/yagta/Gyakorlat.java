package hu.mobilalk.yagta;

public class Gyakorlat {

    private int id;
    private String gyakorlatNev;
    private double suly;
    private int ismetles;

    public Gyakorlat() {
    }

    public Gyakorlat(int id, String gyakorlatNev, double suly, int ismetles) {
        this.id = id;
        this.gyakorlatNev = gyakorlatNev;
        this.suly = suly;
        this.ismetles = ismetles;
    }

    public String getGyakorlatNev() {
        return gyakorlatNev;
    }

    public void setGyakorlatNev(String gyakorlatNev) {
        this.gyakorlatNev = gyakorlatNev;
    }

    public double getSuly() {
        return suly;
    }

    public void setSuly(double suly) {
        this.suly = suly;
    }

    public int getIsmetles() {
        return ismetles;
    }

    public void setIsmetles(int ismetles) {
        this.ismetles = ismetles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
