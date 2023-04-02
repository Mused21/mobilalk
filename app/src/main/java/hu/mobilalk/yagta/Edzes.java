package hu.mobilalk.yagta;

import java.util.Date;
import java.util.List;

public class Edzes {

    private String email;
    private Date date;
    private List<Gyakorlat> gyakorlatok;

    public Edzes() {
    }

    public Edzes(String email, Date date, List<Gyakorlat> gyakorlatok) {
        this.email = email;
        this.date = date;
        this.gyakorlatok = gyakorlatok;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Gyakorlat> getGyakorlatok() {
        return gyakorlatok;
    }

    public void setSzettek(List<Gyakorlat> gyakorlatok) {
        this.gyakorlatok = gyakorlatok;
    }
}
