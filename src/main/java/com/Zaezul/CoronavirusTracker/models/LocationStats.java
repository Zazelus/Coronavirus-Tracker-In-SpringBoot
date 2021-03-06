package com.Zaezul.CoronavirusTracker.models;

public class LocationStats {

    private String country;
    private String state;
    private int latestTotalCases;
    private double incidentRate;
    private double caseFatalityRatio;
    private int diffFromPrevDay;
    private int diffFromPrevMonth;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getTotalCases() {
        return latestTotalCases;
    }

    public void setTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public double getIncidentRate() {
        return incidentRate;
    }

    public void setIncidentRate(double incidentRate) {
        this.incidentRate = incidentRate;
    }

    public double getCaseFatalityRatio() {
        return caseFatalityRatio;
    }

    public void setCaseFatalityRatio(double caseFatalityRatio) {
        this.caseFatalityRatio = caseFatalityRatio;
    }

    public int getDiffFromPrevDay() {
        return diffFromPrevDay;
    }

    public void setDiffFromPrevDay(int diffFromPrevDay) {
        this.diffFromPrevDay = diffFromPrevDay;
    }

    public int getDiffFromPrevMonth() {
        return diffFromPrevMonth;
    }

    public void setDiffFromPrevMonth(int diffFromPrevMonth) {
        this.diffFromPrevMonth = diffFromPrevMonth;
    }

    @Override
    public String toString() {
        return "LocationStats{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", totalCases=" + latestTotalCases +
                ", incidentRate=" + incidentRate +
                ", caseFatalityRatio=" + caseFatalityRatio +
                '}';
    }
}
