package com.Zaezul.CoronavirusTracker.services;

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
import java.util.List;

/**
 * This class is responsible from fetching raw csv data and parsing it into a readable format.
 * It will get updated data from the CSSEGISandData repository on schedule.
 */
@Service
public class CoronaVirusDataService {

    // Grabbing the previous day as that will have the most recent data.
    private Instant now = Instant.now();
    private Instant yesterday = now.minus(1, ChronoUnit.DAYS);
    private Instant beforeYesterday = yesterday.minus(1, ChronoUnit.DAYS);

    private static String PREVIOUS_DAY_DATA_URL;
    private static String CURRENT_DATA_URL;

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
     * File names follow a format like: MM-DD-YYYY, so we'll need to format the current month and days if they are
     * greater than 10.
     */
    public void formatDateAndCreateURL() {
        String currentMonth = yesterday.toString().substring(5, 7);
        String currentDay = yesterday.toString().substring(8, 10);
        String currentYear = yesterday.toString().substring(0, 4);

        String previousMonth = beforeYesterday.toString().substring(5, 7);
        String previousDay = beforeYesterday.toString().substring(8, 10);;
        String previousYear = beforeYesterday.toString().substring(0, 4);;

        CURRENT_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/" +
                "csse_covid_19_daily_reports/" + currentMonth + "-" + currentDay + "-" + currentYear + ".csv";

        PREVIOUS_DAY_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/" +
                "csse_covid_19_daily_reports/" + previousMonth + "-" + previousDay + "-" + previousYear + ".csv";
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
        formatDateAndCreateURL();

        List<LocationStats> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(CURRENT_DATA_URL)).build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();

            locationStat.setState(record.get("Province_State"));
            locationStat.setCountry(record.get("Country_Region"));
            locationStat.setTotalCases(Integer.parseInt(record.get("Confirmed")));

            if (record.get("Recovered") == "") {
                locationStat.setRecoveredCases(0);
            } else {
                locationStat.setRecoveredCases(Integer.parseInt(record.get("Recovered")));
            }

            if (record.get("Incident_Rate") == "") {
                locationStat.setIncidentRate(0);
            } else {
                locationStat.setIncidentRate(round(Double.parseDouble(record.get("Incident_Rate")), 2));
            }

            if (record.get("Case_Fatality_Ratio") == "") {
                locationStat.setCaseFatalityRatio(0);
            } else {
                locationStat.setCaseFatalityRatio(round(Double.parseDouble(record.get("Case_Fatality_Ratio")), 2));
            }

            //System.out.println(locationStat);
            newStats.add(locationStat);
        }
        //System.out.println(PREVIOUS_DAY_DATA_URL);
        //System.out.println(CURRENT_DATA_URL);
        this.allStats = newStats;
    }

}
