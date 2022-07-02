package com.Zaezul.CoronavirusTracker.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * This class is responsible from fetching raw csv data and parsing it into a readable format.
 * It will get updated data from the CSSEGISandData repository on schedule.
 */
@Service
public class CoronaVirusDataService {

    // Grabbing the previous day as that will have the most recent data.
    private static Date currentDate = new Date();
    private static LocalDate localDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    private static String currentYear  = String.valueOf(localDate.getYear());
    private static String currentMonth = String.valueOf(localDate.getMonthValue());
    private static String currentDay   = String.valueOf(localDate.getDayOfMonth() - 1);
    private static String DATA_URL;

    /**
     * File names follow a format like: MM-DD-YYYY, so we'll need to format the current month and days if they are
     * greater than 10.
     */
    public void formatDateAndCreateURL() {
        if (Integer.parseInt(currentMonth) < 10) {currentMonth = "0" + currentMonth;}
        if (Integer.parseInt(currentDay) < 10) {currentDay = "0" + currentDay;}

        DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/" +
                "csse_covid_19_daily_reports/" + currentMonth + "-" + currentDay + "-" + currentYear + ".csv";
    }

    /**
     * Scheduled method to be run that pulls data from latest csv as an HttpResponse. Using apache's csv library, we
     * can parse through each record with the first containing all of our headers.
     * @throws IOException
     * @throws InterruptedException
     */
    @PostConstruct
    @Scheduled(cron = "* * * * * *")
    public void fetchData() throws IOException, InterruptedException {
        formatDateAndCreateURL();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(DATA_URL)).build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            String state = record.get("Province_State");
            System.out.println(state);
        }
    }

}
