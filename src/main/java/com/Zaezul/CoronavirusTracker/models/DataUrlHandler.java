package com.Zaezul.CoronavirusTracker.models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Handles creation of urls for our updated csv and yesterday's csv.
 */
public class DataUrlHandler {

    private Instant now = Instant.now();
    private Instant yesterday = now.minus(1, ChronoUnit.DAYS);
    private Instant beforeYesterday = yesterday.minus(1, ChronoUnit.DAYS);
    private Instant thirtyDaysAgo = now.minus(30, ChronoUnit.DAYS);

    private static String THIRTY_DAY_DATA_URL;
    private static String PREVIOUS_DAY_DATA_URL;
    private static String CURRENT_DATA_URL;

    /**
     * File names follow a format like: MM-DD-YYYY, so we'll need to format the current month and days if they are
     * greater than 10.
     */
    public void formatDateAndCreateURLs() {
        String currentMonth = yesterday.toString().substring(5, 7);
        String currentDay = yesterday.toString().substring(8, 10);
        String currentYear = yesterday.toString().substring(0, 4);

        String previousMonth = beforeYesterday.toString().substring(5, 7);
        String previousDay = beforeYesterday.toString().substring(8, 10);
        String previousYear = beforeYesterday.toString().substring(0, 4);

        String lastMonth = thirtyDaysAgo.toString().substring(5, 7);
        String lastMonthDay = thirtyDaysAgo.toString().substring(8, 10);
        String lastMonthYear = thirtyDaysAgo.toString().substring(0, 4);

        CURRENT_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/" +
                "csse_covid_19_daily_reports/" + currentMonth + "-" + currentDay + "-" + currentYear + ".csv";

        PREVIOUS_DAY_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/" +
                "csse_covid_19_daily_reports/" + previousMonth + "-" + previousDay + "-" + previousYear + ".csv";

        THIRTY_DAY_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/" +
                "csse_covid_19_daily_reports/" + lastMonth + "-" + lastMonthDay + "-" + lastMonthYear + ".csv";
    }

    public String[] getUrls() {
        return new String[] {THIRTY_DAY_DATA_URL, PREVIOUS_DAY_DATA_URL, CURRENT_DATA_URL};
    }

    public DataUrlHandler() {
        formatDateAndCreateURLs();
    }
}
