package com.example.covidapp.StatModel;


import java.io.Serializable;
import java.util.Date;

public class RegionHistory implements Serializable {
    private String confirmed;
    private String deaths;
    private String recovered;
    private String date;

    public RegionHistory(String date , String confirmed, String deaths, String recovered){

        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.date = date;
    }
    RegionHistory(){
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }

    public String getDate(){
        return date;
    }
    public String getConfirmed() {
        return confirmed;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getRecovered() {
        return recovered;
    }
}
