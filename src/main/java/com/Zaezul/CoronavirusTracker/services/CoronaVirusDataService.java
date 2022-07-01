package com.Zaezul.CoronavirusTracker.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class CoronaVirusDataService {

    private static String DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/06-30-2022.csv";

    public void fetchData() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(DATA_URL)).build();
    }

}
