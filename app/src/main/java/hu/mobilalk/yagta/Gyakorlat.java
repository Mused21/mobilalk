package hu.mobilalk.yagta;

public class Gyakorlat {

    private int szettIsmetles;
    private String gyakorlatNev;
    private double suly;
    private int gyakorlatIsmetles;

    public Gyakorlat() {
    }

    public Gyakorlat(int szettIsmetles, String gyakorlatNev, double suly, int gyakorlatIsmetles) {
        this.szettIsmetles = szettIsmetles;
        this.gyakorlatNev = gyakorlatNev;
        this.suly = suly;
        this.gyakorlatIsmetles = gyakorlatIsmetles;
    }

    public int getSzettIsmetles() {
        return szettIsmetles;
    }

    public void setSzettIsmetles(int szettIsmetles) {
        this.szettIsmetles = szettIsmetles;
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

    public int getGyakorlatIsmetles() {
        return gyakorlatIsmetles;
    }

    public void setGyakorlatIsmetles(int gyakorlatIsmetles) {
        this.gyakorlatIsmetles = gyakorlatIsmetles;
    }
}
