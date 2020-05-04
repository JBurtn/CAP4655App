package com.example.covidapp.StatModel;

import java.io.Serializable;
import java.util.ArrayList;

public class Region implements Serializable {
    private String totalConfirmedCases;
    private String totalDeaths;
    private String totalRecoveredCases;
    private String ConfirmedCases;
    private String Deaths;
    private String RecoveredCases;

    private ArrayList<RegionHistory> histories;
    public Region(String totalConfirmedCases, String totalDeaths, String totalRecoveredCases, String confirmedCases,
                  String deaths, String recoveredCases) {
        this.totalConfirmedCases = totalConfirmedCases;
        this.totalDeaths = totalDeaths;
        this.totalRecoveredCases = totalRecoveredCases;
        ConfirmedCases = confirmedCases;
        Deaths = deaths;
        RecoveredCases = recoveredCases;
        histories = new ArrayList<>();
    }
    public Region(){

    }
    public boolean hasHistory(){
        return !histories.isEmpty();
    }
    public void addHistory(String date, String confirmed, String deaths, String recovered) {
        histories.add(new RegionHistory(date, confirmed, deaths, recovered));
    }

    public void setTotalConfirmedCases(String totalConfirmedCases) {
        this.totalConfirmedCases = totalConfirmedCases;
    }

    public void setTotalDeaths(String totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public void setTotalRecoveredCases(String totalRecoveredCases) {
        this.totalRecoveredCases = totalRecoveredCases;
    }

    public void setConfirmedCases(String confirmedCases) {
        ConfirmedCases = confirmedCases;
    }

    public void setDeaths(String deaths) {
        Deaths = deaths;
    }

    public void setRecoveredCases(String recoveredCases) {
        RecoveredCases = recoveredCases;
    }

    public void setHistories(ArrayList<RegionHistory> histories) {
        this.histories = histories;
    }

    public String getTotalConfirmedCases() {
        return totalConfirmedCases;
    }

    public String getTotalDeaths() {
        return totalDeaths;
    }

    public String getTotalRecoveredCases() {
        return totalRecoveredCases;
    }

    public String getConfirmedCases() {
        return ConfirmedCases;
    }

    public String getDeaths() {
        return Deaths;
    }

    public String getRecoveredCases() {
        return RecoveredCases;
    }

    public ArrayList<RegionHistory> getHistory() {
        return histories;
    }
}
