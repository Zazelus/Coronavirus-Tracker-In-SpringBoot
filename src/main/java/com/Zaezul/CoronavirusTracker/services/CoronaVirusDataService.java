package com.Zaezul.CoronavirusTracker.services;

import com.Zaezul.CoronavirusTracker.models.DataUrls;
import com.Zaezul.CoronavirusTracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.stream.Location;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This class is responsible from fetching raw csv data and parsing it into a readable format.
 * It will get updated data from the CSSEGISandData repository on schedule.
 */
@Service
public class CoronaVirusDataService {

    private List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    /**
     * Better rounding function for large decimal places.
     * @param value value to be rounded
     * @param places the amount of places to round to
     * @return the new decimal value
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Creates an Iterable of CSV records by sending a request for the appropriate csv file.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public Iterable<CSVRecord> createCsvIterable(HttpClient client, HttpRequest request)
            throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(response.body());

        return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
    }

    /**
     * Scheduled method to be run that pulls data from latest csv as an HttpResponse. Using apache's csv library, we
     * can parse through each record with the first containing all of our headers.
     * @throws IOException
     * @throws InterruptedException
     */
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchData() throws IOException, InterruptedException {
        DataUrls dataUrls = new DataUrls();
        String[] urls = dataUrls.getUrls();

        List<LocationStats> newStats = new ArrayList<>();

        HttpClient prevDataClient = HttpClient.newHttpClient();
        HttpClient currentClient = HttpClient.newHttpClient();

        HttpRequest prevDayDataRequest = HttpRequest.newBuilder().uri(URI.create(urls[0])).build();
        HttpRequest currentDayDataRequest = HttpRequest.newBuilder().uri(URI.create(urls[1])).build();

        Iterable<CSVRecord> prevRecords = createCsvIterable(prevDataClient, prevDayDataRequest);
        Iterable<CSVRecord> records = createCsvIterable(currentClient, currentDayDataRequest);

        // Go through each record and add it to our LocationStats model.
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            int totalCases = Integer.parseInt(record.get("Confirmed"));

            locationStat.setState(record.get("Province_State"));
            locationStat.setCountry(record.get("Country_Region"));
            locationStat.setTotalCases(totalCases);

            if (record.get("Incident_Rate").equals("")) {
                locationStat.setIncidentRate(0);
            } else {
                locationStat.setIncidentRate(round(Double.parseDouble(record.get("Incident_Rate")), 2));
            }

            if (record.get("Case_Fatality_Ratio").equals("")) {
                locationStat.setCaseFatalityRatio(0);
            } else {
                locationStat.setCaseFatalityRatio(round(Double.parseDouble(record.get("Case_Fatality_Ratio")),
                        2));
            }

            if (prevRecords.iterator().hasNext()) {
                int prevTotalCases = Integer.parseInt(prevRecords.iterator().next().get("Confirmed"));
                locationStat.setDiffFromPrevDay(totalCases - prevTotalCases);
            }

            newStats.add(locationStat);
        }

        this.allStats = newStats;
    }

}
