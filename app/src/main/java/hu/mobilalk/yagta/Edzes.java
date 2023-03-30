package hu.mobilalk.yagta;

import java.util.Date;
import java.util.List;

public class Edzes {

    private String email;
    private Date date;
    private List<Szett> szettek;

    public Edzes() {
    }

    public Edzes(String email, Date date, List<Szett> szettek) {
        this.email = email;
        this.date = date;
        this.szettek = szettek;
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

    public List<Szett> getSzettek() {
        return szettek;
    }

    public void setSzettek(List<Szett> szettek) {
        this.szettek = szettek;
    }
}
