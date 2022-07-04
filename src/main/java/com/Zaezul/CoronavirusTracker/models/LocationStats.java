package com.Zaezul.CoronavirusTracker.models;

public class LocationStats {

    private String state;
    private String country;
    private int latestTotalCases;
    private int recoveredCases;
    private double incidentRate;
    private double caseFatalityRatio;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTotalCases() {
        return latestTotalCases;
    }

    public void setTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public int getRecoveredCases() {
        return recoveredCases;
    }

    public void setRecoveredCases(int recoveredCases) {
        this.recoveredCases = recoveredCases;
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

    @Override
    public String toString() {
        return "LocationStats{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", totalCases=" + latestTotalCases +
                ", recoveredCases=" + recoveredCases +
                ", incidentRate=" + incidentRate +
                ", caseFatalityRatio=" + caseFatalityRatio +
                '}';
    }
}
