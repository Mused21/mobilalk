package hu.mobilalk.yagta;

import java.util.List;

public class Szett {
    private int sorszam;
    private List<Gyakorlat> gyakorlatok;

    public Szett() {
    }

    public Szett(int sorszam, List<Gyakorlat> gyakorlatok) {
        this.sorszam = sorszam;
        this.gyakorlatok = gyakorlatok;
    }

    public int getSorszam() {
        return sorszam;
    }

    public void setSorszam(int sorszam) {
        this.sorszam = sorszam;
    }

    public List<Gyakorlat> getGyakorlatok() {
        return gyakorlatok;
    }

    public void setGyakorlatok(List<Gyakorlat> gyakorlatok) {
        this.gyakorlatok = gyakorlatok;
    }
}
